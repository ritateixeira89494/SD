package uni.sd.ui.client;

import uni.sd.ln.client.ILN;
import uni.sd.ln.client.LN;
import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.utils.Pair;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private final ILN model;
    private final Scanner scin;

    public MenuPrincipal() throws IOException {
        this.model = new LN();
        scin = new Scanner(System.in);
    }

    public void run() throws IOException {
        System.out.println("Bem vindo!");

        Menu menu = new Menu(new String[]{
            "Entrar no sistema, usando credenciais",
            "Registrar novo utilizador"
        });
        menu.setHandler(1, this::login);
        menu.setHandler(2, this::registar);

        menu.run();

        System.out.println();
        System.out.println("Tenha um bom dia!");
    }

    private void login() throws IOException {
        System.out.println("Email : ");
        String email = scin.nextLine();
        System.out.println();
        System.out.println("Password : ");
        String password = scin.nextLine();
        System.out.println();
        try {
            Pair<String, Integer> info = model.autenticar(email,password);
            redirecionarMenu(info.getLeft(), info.getRight());
        } catch (CredenciaisErradasException e) {
            System.out.println();
            System.out.println("As credenciais encontram-se incorretas");
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }
    }

    private void redirecionarMenu(String username, int authority) throws IOException {
        switch (authority) {
            case 1:
                menuPrincipalAdministrador(username);
                break;
            case 0:
                menuPrincipalNormal(username);
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
            if (autoridade == 1) {
                System.out.println();
                System.out.println("Administrador registado com sucesso");
            } else {
                System.out.println();
                System.out.println("Utilizador registado com sucesso");
            }
        } catch (UtilizadorExisteException e) {
            System.out.println();
            System.out.println("O utilizador já existe");
        } catch (UsernameInvalidoException e) {
            System.out.println();
            System.out.println("O username é inválido");
        } catch (PasswordInvalidaException e) {
            System.out.println();
            System.out.println("A password é inválida");
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }
    }

    private void menuPrincipalNormal(String username) throws IOException {
        System.out.println("----------------------------");
        System.out.println("Bem vindo " + username);
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
        System.out.println();
        System.out.println("Destino : ");
        String destino = scin.nextLine();
        System.out.println();
        System.out.println("Data do voo : (Formato: dd-MM-yyyy HH:mm)");
        LocalDateTime dia = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        try {
            int id = model.reservarVoo(origem,destino,dia);
            System.out.println("Voo reservado com sucesso");
            System.out.println("ID da reserva : " + id);
        } catch (VooInexistenteException e) {
            System.out.println();
            System.out.println("O voo não existe");
        } catch (UtilizadorInexistenteException e) {
            System.out.println();
            System.out.println("Utilizador não existe");
        } catch (ReservaExisteException e) {
            System.out.println();
            System.out.println("Esta reserva já existe");
        } catch (ReservaInexistenteException e) {
            System.out.println();
            System.out.println("A reserva não foi adicionada corretamente");
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        } catch (SemReservaDisponivelException e) {
            System.out.println("Já não existem reservas disponíveis para este voo");

        }

    }

    private void cancelarVoo() throws IOException {
        System.out.println("ID da viagem : ");
        String id = scin.nextLine();
        System.out.println();
        try {
            model.cancelarVoo(Integer.parseInt(id));
            System.out.println("O voo foi cancelado com sucesso");
        } catch (ReservaInexistenteException e) {
            System.out.println();
            System.out.println("Esta reserva não existe");
        } catch (VooInexistenteException e) {
            System.out.println();
            System.out.println("O voo não existe");
        } catch (UtilizadorInexistenteException e) {
            System.out.println();
            System.out.println("O utilizador não se encontra neste voo");
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }
    }

    private void reservarVooPorPercurso() throws IOException {
        System.out.println("Indique aqui os voos que deseja realizar, separadas por vírgulas : ");
        List<String> localizacoes = List.of((scin.nextLine()).split(","));
        System.out.println();
        System.out.println("Data de início : (Formato: dd-MM-yyyy HH:mm)");
        LocalDateTime start = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        System.out.println();
        System.out.println("Data de fim : (Formato: dd-MM-yyyy HH:mm)");
        LocalDateTime finish = LocalDateTime.parse(scin.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        System.out.println();
        try {
            model.reservarVooPorPercurso(localizacoes,start,finish);
            System.out.println("Os voos foram reservados com sucesso");
        } catch (VooInexistenteException e) {
            System.out.println();
            System.out.println("Não é possível obter este percurso, devido a um dos voos não existir");
        } catch (DataInvalidaException e) {
            System.out.println();
            System.out.println("Uma das datas específicadas não é válida");
        } catch (SemReservaDisponivelException e) {
            System.out.println();
            System.out.println("Não existe uma reserva possível para o percurso especificado");
        } catch (UtilizadorInexistenteException e) {
            System.out.println();
            System.out.println("O utilizador não existe");
        } catch (ReservaExisteException e) {
            System.out.println();
            System.out.println("Existe uma reserva possível");
        } catch (ReservaInexistenteException e) {
            System.out.println();
            System.out.println("Não é possível efetuar a reserva");
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }
    }

    private void obterListaVoo() throws IOException {
        System.out.println();
        try {
            List<Voo> voos = model.obterListaVoo();
            for(Voo v: voos) {
                System.out.println(
                        "Partida: " + v.getPartida() + ";" + " " +
                                "Destino: " + v.getDestino() + ";" + " " +
                                "Capacidade: " + v.getCapacidade() + ";" + " " +
                                "Duração: " + v.getDuracao() + "."
                );
            }
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }

    }

    // Issue
    private void obterPercursosPossiveis() throws IOException {
        System.out.println("Indique aqui a origem do voo : ");
        String origem = scin.nextLine();
        System.out.println();
        System.out.println("Indique aqui o destino do voo : ");
        String destino = scin.nextLine();
        try {
            System.out.println();
            List<List<String>> caminhos = model.obterPercursosPossiveis(origem,destino);
            int i;
            for(i = 0; i < caminhos.size(); i++) {
                System.out.println("Caminho: " + (i+1));
                List<String> caminho = caminhos.get(i);
                int j;
                for(j = 0; j < caminho.size() - 1; j++) {
                    System.out.print(caminho.get(j) + "->");
                }
                System.out.println(caminho.get(j));
            }
        } catch (DiaJaEncerradoException e) {
            System.out.println("O dia já foi encerrado. Tente novamente amanhã");
        }
    }

    private void menuPrincipalAdministrador(String username) throws IOException {
        System.out.println("----------------------------");
        System.out.println("Bem vindo " + username);
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
        System.out.println();
        System.out.println("Destino do voo : ");
        String destino = scin.nextLine();
        System.out.println();
        System.out.println("Quantos passageiros podem ir no voo? : ");
        int passa = Integer.parseInt(scin.nextLine());
        System.out.println();
        System.out.println("Quanto tempo dura o voo? : ");
        int duracao = Integer.parseInt(scin.nextLine());
        System.out.println();
        try {
            model.addInfo(origem,destino,passa,duracao);
            System.out.println("Informação sobre voo adicionada com sucesso");
        } catch (VooExisteException e) {
            System.out.println();
            System.out.println("O voo já existe");
        } catch (CapacidadeInvalidaException e) {
            System.out.println();
            System.out.println("Não é possível adicionar essa quantidade de passageiros");
        } catch (PartidaDestinoIguaisException e) {
            System.out.println();
            System.out.println("A origem e o destino é a mesma localização");
        } catch (DuracaoInvalidaException e) {
            System.out.println();
            System.out.println("A duração da viagem não é válida");
        }
    }

    private void encerrarDia() throws IOException {
        try {
            model.encerrarDia();
            System.out.println();
            System.out.println("O dia foi encerrado para reservas");
        } catch (DiaJaEncerradoException e) {
            System.out.println();
            System.out.println("O dia já se encontra encerrado");
        }
    }

    private void abrirDia() throws IOException {
        try {
            model.abrirDia();
            System.out.println();
            System.out.println("O dia foi aberto para reservas");
        } catch (DiaJaAbertoException e) {
            System.out.println();
            System.out.println("O dia já se encontra aberto");
        }
    }

}