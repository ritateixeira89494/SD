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
import java.util.Objects;


public class SSVooFacade implements ISSVoo {
    IDados daos;

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
        List<Integer> res = new ArrayList<>();
        int i;
        for (i = 0; i < voos.size() - 1; i++) {
            String novaPartida = voos.get(i);
            String novoDestino = voos.get(i+1);
            Voo v = daos.getVoo(novaPartida, novoDestino);
            dataInicio = dataInicio.minusMinutes(v.getDuracao());
            if(dataFim.isBefore(dataInicio)){
                System.out.println("Não foi possível realizar a reserva com esse time frame.");
                return null;
            }
            int idReserva = reservarVoo(email, novaPartida, novoDestino, dataInicio);
            res.add(idReserva);
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


    static class Node {
        String cidade; // cidade da qual iremos partir
        List<Node> caminho; // lista de destinos a partir da cidade dada


        Node(String cidade, List<Node> caminho){
            this.cidade = cidade;
            this.caminho = caminho;
        }
    }

    /**
     * Retorna uma lista com todos os percursos possíveis
     * que começem em partida e acabem em destino
     * com um máximo de 3 saltos.
     *
     * @param partida Local de partida
     * @param destinoFinal Local de destino
     * @return Lista com todos os caminhos possíveis começados em partida
     * e acabados em destino com um máximo de 3 saltos
     */

    @Override
    public List<List<String>> obterPercursosPossiveis(String partida, String destinoFinal) throws VooInexistenteException, SQLException {
        List<Node> caminho = new ArrayList<>();
        Node arv = new Node(partida, caminho);

        int saltos = 0;
        arv = preecherArvore(arv, partida, saltos);

        List<List<String>> res = new ArrayList<>();
        guardarPercursosPossiveis(arv, res, destinoFinal);

        return res;
    }

    public Node preecherArvore(Node arv, String partida, int saltos) throws VooInexistenteException, SQLException {
        //EU ACHO QUE ISTO ESTÁ BEM MAS NÃO TENHO A CERTEZA!!!!!!!!!!!
        //QUANTO MAIS OLHO PARA ISTO, MAIS ERRADO PARECE
        arv.cidade = partida;

        Map<String, Voo> destinos = daos.getVooPorPartida(partida);
        List<String> novasPartidas = new ArrayList<>(destinos.keySet());
        for(String nPartida : novasPartidas) {
            if(saltos <= 4) {
                saltos++;
                arv.caminho.add(preecherArvore(arv, nPartida, saltos));
            }
        }

        return arv;
    }


    public List<List<String>> guardarPercursosPossiveis(Node arv, List<List<String>> res, String destinoFinal){
        int count = 0;
        List<String> umPercurso = new ArrayList<>();
        for(int i = 0; i < arv.caminho.size(); i++){
            umPercurso = guardarPercurso(arv, res.get(i), destinoFinal, count);
            res.add(umPercurso);
        }
        return res;
    }

    public List<String> guardarPercurso(Node arv, List<String> percurso, String destinoFinal, int count){
        if(percurso.size() < 4 && Objects.equals(arv.cidade, destinoFinal)){
            percurso.add(arv.cidade);
            return percurso;
        }
        else if(percurso.size() < 4){
            percurso.add(arv.cidade);
            count++;
            guardarPercurso(arv.caminho.get(count), percurso, destinoFinal, count);
        }
        return null;
    }


}
