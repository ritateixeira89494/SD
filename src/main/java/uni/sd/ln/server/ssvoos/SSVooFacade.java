package uni.sd.ln.server.ssvoos;

import uni.sd.data.IDados;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.reservas.Reserva;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SSVooFacade implements ISSVoo {
    IDados daos;
    private boolean diaAberto = true;

    public SSVooFacade(IDados daos) {
        this.daos = daos;
    }

    /**
     * Reserva um voo de acordo com um id e uma data.
     *
     * @param partida origem do voo
     * @param destino destino do voo
     * @param data    Data do voo a reservar
     */
    @Override
    public int reservarVoo(String email, String partida, String destino, LocalDateTime data) throws VooInexistenteException,
            SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        Voo v = daos.getVoo(partida, destino);
        LocalDateTime dataNow = LocalDateTime.now();
        Reserva r = new Reserva(email, partida, destino, data, dataNow);
        daos.saveReserva(r);

        return daos.getIDReserva(email, partida, destino, data);
    }

    /**
     * Cancela um voo já reservado.
     *
     * @param idReserva ID da reserva
     */
    @Override
    public void cancelarVoo(int idReserva) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException {
        Reserva r = daos.getReservaPorID(idReserva);

        String email = r.getEmailUtilizador();
        String partida = r.getPartida();
        String destino = r.getDestino();
        LocalDateTime data = r.getDataVoo();

        daos.removeReserva(email, partida, destino, data);
    }

    /**
     * Insere um novo voo na lista de voos fazendo todas as verificações necessárias.
     * Para executar este método é necessário um nível de autoridade igual ou superior a 1 (Administrador).
     *
     * @param partida    Local de partida
     * @param destino    Local de chegada
     * @param capacidade Capacidade do voo
     *                   <p>
     *                   TODO: Adicionar uma verificação para o nível de permissões do utilizador.
     */
    @Override
    public void addInfo(String partida, String destino, int capacidade, int duracao)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException, DuracaoInvalidaException {
        if (capacidade <= 0) {
            throw new CapacidadeInvalidaException("Capacidade: " + capacidade);
        }
        if (partida.equals(destino)) {
            throw new PartidaDestinoIguaisException("Partida: " + partida + " | Destino: " + destino);
        }
        if (duracao <= 0) {
            throw new DuracaoInvalidaException();
        }

        Voo novoVoo = new Voo(partida, destino, capacidade, 0, duracao);
        daos.saveVoo(novoVoo);
    }

    /**
     * Encerra o dia.
     * Se o dia já estiver encerrado atira um DiaJaEncerradoException.
     */
    @Override
    public void encerrarDia() throws DiaJaEncerradoException {
        if (!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        diaAberto = false;
    }

    /**
     * Abre o dia.
     * Se o dia já estiver aberto atira um DiaJaAbertoException.
     */
    @Override
    public void abrirDia() throws DiaJaAbertoException {
        if (diaAberto) {
            throw new DiaJaAbertoException();
        }
        diaAberto = true;
    }

    /**
     * Reserva uma série de voos de acordo com uma percurso,
     * uma data de início e fim.
     * Ou pelo menos foi o que percebi do exercício.
     *
     * @param voos       Lista de voos
     * @param dataInicio Limite inferior da data de reserva
     * @param dataFim    Limite superior da data de reserva
     */
    @Override
    public List<Integer> reservarVooPorPercurso(String email, List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        // TODO EU ESTOU A PÔR A FAZER NOVA RESERVA COM INTERVALOS DE 1 DIA MAS NÃO ACHO BEM POR CAUSA DA DATA DE FIM
        List<Integer> res = new ArrayList<>();
        int hours = 0, i;
        for (i = 0; i < voos.size() - 1; i++) {
            int idReserva = reservarVoo(email, voos.get(i), voos.get(i + 1), dataInicio.plusDays(1));
            res.add(idReserva);
            hours += 12;
        }
        return res;
    }

    /**
     * Retorna a lista de todos os voos registados no sistema.
     *
     * @return Lista com todos os voos do sistema.
     */
    @Override
    public List<Voo> obterListaVoo() throws SQLException {
        return daos.getTodosVoos();
    }

    /**
     * Retorna uma lista com todos os percursos possíveis
     * que começem em partida e acabem em destino
     * com um máximo de 3 saltos.
     *
     * @param partida Local de partida
     * @param destino Local de destino
     * @return Lista com todos os caminhos possíveis começados em partida
     * e acabados em destino com um máximo de 3 saltos
     */

    static class Node {
        String data;

        // List of children
        Node children[];

        Node(int n, String data)
        {
            children = new Node[n];
            this.data = data;
        }
    }

    @Override
    public List<List<String>> obterPercursosPossiveis(String partida, String destino) throws VooInexistenteException, SQLException {
        int n = 4;
        Node root = new Node(n, partida);

        int i = 0;

        Map<String, Voo> destinos = daos.getVooPorPartida(partida);
        for (String dest : destinos.keySet()){
            root.children[i] = new Node(n, dest);
            i++;
        }

        root = preencherFilhos(root, i);

        // TODO AGORA TENHO QUE PERCORRER A ÁRVORE MAS... COMO?
        List<String> res = new ArrayList<String>();



        for(int l = 0; l < 4; l++){

        }

        return null;
    }


    public Node preencherFilhos(Node root, int i) throws VooInexistenteException, SQLException {
        // TODO COMO É QUE METO ISTO RECURSIVO?? PARA JÁ SÓ ESTÁ A FAZER PARA O PRIMEIRO SALTO...
        int j;
        for(j = 0; j < i; j++) {
            Map<String, Voo> novosDestinos = daos.getVooPorPartida(String.valueOf(root.children[j]));
            for (String novoDest : novosDestinos.keySet()) {
                root.children[j].children[0] = new Node(4, novoDest);
            }
        }

        return root;
    }

}
