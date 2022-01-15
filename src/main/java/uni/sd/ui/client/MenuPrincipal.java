package uni.sd.ui.client;

import uni.sd.ln.client.ILN;
import uni.sd.ln.client.LN;
import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    // O model tem a 'lógica de negócio'.
    private final ILN model;

    // Scanner para leitura
    private final Scanner scin;

    /**
     * Construtor.
     * <p>
     * Cria os menus e a camada de negócio.
     */
    public MenuPrincipal() throws IOException {

        this.model = new LN();
        scin = new Scanner(System.in);
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() throws IOException {
        System.out.println("Bem vindo!");

        Menu menu = new Menu(new String[]{
            "Entrar",
            "Registrar"
        });
        menu.setHandler(1, this::login);
        menu.setHandler(2, this::registar);
        menu.run();

        System.out.println("Tenha um bom dia!");
    }

    private void login() throws IOException {
        System.out.println("Username : ");
        String username = scin.nextLine();
        System.out.println("Password : ");
        String password = scin.nextLine();
        try {
            int authority = model.autenticar(username,password);
            redirecionarMenu(authority);
        } catch (CredenciaisErradasException e) {
            System.out.println("As credenciais encontram-se incorretas");
        }
    }

    private void redirecionarMenu(int authority) throws IOException {
        switch (authority) {
            case 1:
                menuPrincipalAdministrador();
                break;
            case 0:
                menuPrincipalNormal();
                break;
        }
    }

    private void registar() throws IOException {
        System.out.println("Email : ");
        String email = scin.nextLine();
        System.out.println("Username : ");
        String username = scin.nextLine();
        System.out.println("Password : ");
        String password = scin.nextLine();
        System.out.println("Autoridade : ");
        int autoridade = Integer.parseInt(scin.nextLine());
        try {
            model.registar(email,username,password,autoridade);
        } catch (UtilizadorExisteException e) {
            System.out.println("O utilizador já existe");;
        } catch (UsernameInvalidoException e) {
            System.out.println("O username é inválido");
        } catch (PasswordInvalidaException e) {
            System.out.println("A password é inválida");
        }
    }

    private void menuPrincipalNormal() throws IOException {
        Menu menu = new Menu(new String[]{
                "Fazer uma reserva de voo",
                "Cancelar uma das reservas de voo",
                "Reservar um voo através de um percurso específico",
                "Obter uma lista de todos os voos",
                "Obter uma lista de percursos possíveis",
        });

        menu.setHandler(1, this::reservarVoo);
        menu.setHandler(2, this::cancelarVoo);
        menu.setHandler(3, this::reservarVooPorPercurso);
        menu.setHandler(4, this::obterListaVoo);
        menu.setHandler(5, this::obterPercursosPossiveis);

        menu.run();
    }

    private void reservarVoo() throws IOException {
        System.out.println("Origem : ");
        String origem = scin.nextLine();
        System.out.println("Destino : ");
        String destino = scin.nextLine();
        System.out.println("Data do voo : (Formato: dd-MM-yyyy HH:mm)");
        LocalDateTime dia = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        try {
            model.reservarVoo(origem,destino,dia);
        } catch (VooInexistenteException e) {
            System.out.println("O voo não existe");
        } catch (UtilizadorInexistenteException e) {
            System.out.println("Utilizador não existe");
        } catch (ReservaExisteException e) {
            System.out.println("Esta reserva já existe");
        } catch (ReservaInexistenteException e) {
            System.out.println("A reserva não foi adicionada corretamente");
        }
    }

    private void cancelarVoo() throws IOException {
        System.out.println("ID da viagem : ");
        String id = scin.nextLine();
        try {
            model.cancelarVoo(Integer.parseInt(id));
        } catch (ReservaInexistenteException e) {
            System.out.println("Esta reserva não existe");
        } catch (VooInexistenteException e) {
            System.out.println("O voo não existe");
        } catch (UtilizadorInexistenteException e) {
            System.out.println("O utilizador não se encontra neste voo");
        }
    }

    private void reservarVooPorPercurso() throws IOException {
        System.out.println("Indique aqui os voos que deseja realizar, separadas por vírgulas : ");
        List<String> localizacoes = List.of((scin.nextLine()).split(","));
        System.out.println("Hora de início : ");
        LocalDateTime start = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        System.out.println("Hora de fim : ");
        LocalDateTime finish = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        try {
            model.reservarVooPorPercurso(localizacoes,start,finish);
        } catch (VooInexistenteException e) {
            System.out.println("Não é possível obter este percurso, devido a um dos voos não existir");
        } catch (DataInvalidaException e) {
            System.out.println("Uma das datas específicadas não é válida");
        } catch (SemReservaDisponivelException e) {
            System.out.println("Não existe uma reserva possível para o percurso especificado");
        } catch (UtilizadorInexistenteException e) {
            System.out.println("O utilizador não existe");
        } catch (ReservaExisteException e) {
            System.out.println("Existe uma reserva possível");
        } catch (ReservaInexistenteException e) {
            System.out.println("Não é possível efetuar a reserva");
        }
    }

    private void obterListaVoo() throws IOException {
        List<Voo> voos = model.obterListaVoo();

        for(Voo v: voos) {
            System.out.println(
                    "Partida: " + v.getPartida() + " " +
                            "Destino: " + v.getDestino() + " " +
                            "Capacidade: " + v.getCapacidade() + " " +
                            "Ocupação: " + v.getOcupacao() + " " +
                            "Duração: " + v.getOcupacao()
                    );
        }
    }

    private void obterPercursosPossiveis() throws IOException {
        System.out.println("Indique aqui a origem do voo : ");
        String origem = scin.nextLine();
        System.out.println("Indique aqui o destino do voo : ");
        String destino = scin.nextLine();
        model.obterPercursosPossiveis(origem,destino);
    }

    private void menuPrincipalAdministrador() throws IOException {
        Menu menu = new Menu(new String[]{
                "Adicionar informação sobre um novo voo",
                "Encerrar o dia, não permitindo novas reservas",
                "Reabre o dia, voltando a permitir novas reservas",
        });

        menu.setHandler(1, this::addInfo);
        menu.setHandler(2, this::encerrarDia);
        menu.setHandler(3, this::abrirDia);

        menu.run();
    }

    private void addInfo() throws IOException {
        System.out.println("Origem do voo : ");
        String origem = scin.nextLine();
        System.out.println("Destino do voo : ");
        String destino = scin.nextLine();
        System.out.println("Quantos passageiros podem ir no voo? : ");
        int passa = Integer.parseInt(scin.nextLine());
        System.out.println("Quanto tempo dura o voo? : ");
        int duracao = Integer.parseInt(scin.nextLine());
        try {
            model.addInfo(origem,destino,passa,duracao);
        } catch (VooExisteException e) {
            System.out.println("O voo existe");
        } catch (CapacidadeInvalidaException e) {
            System.out.println("Não é possível adicionar essa quantidade de passageiros");
        } catch (PartidaDestinoIguaisException e) {
            System.out.println("A origem e o destino é a mesma localização");
        } catch (DuracaoInvalidaException e) {
            System.out.println("A duração da viagem não é válida");
        }
    }

    private void encerrarDia() throws IOException {
        try {
            model.encerrarDia();
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já se encontra encerrado");
        }
    }

    private void abrirDia() throws IOException {
        try {
            model.abrirDia();
        } catch (DiaJaAbertoException e) {
            System.out.println("O dia já se encontra aberto");
        }
    }

}