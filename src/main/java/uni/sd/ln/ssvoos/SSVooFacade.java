package uni.sd.ln.ssvoos;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uni.sd.ln.ssvoos.exceptions.CapacidadeInvalidaException;
import uni.sd.ln.ssvoos.exceptions.DataInvalidaException;
import uni.sd.ln.ssvoos.exceptions.DiaJaAbertoException;
import uni.sd.ln.ssvoos.exceptions.DiaJaEncerradoException;
import uni.sd.ln.ssvoos.exceptions.PartidaDestinoIguaisException;
import uni.sd.ln.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.voos.Voo;

public class SSVooFacade implements ISSVoo {
    private Map<String, Voo> voos;
    private boolean diaAberto = true;

    public SSVooFacade() {
        voos = new HashMap<>();
        /**
         *  Adicionei isto para testes. Este comentário serve
         *  para o caso me ter esquecido de remover isto antes de
         *  dar commit. 
         */
        voos.put("t", new Voo("Braga", "Madrid", 150));
        voos.put("haha", new Voo("Braga", "Munique", 200));
        voos.put("o", new Voo("Nova Iorque", "Lisboa", 20));
    }

    /**
     * Reserva um voo de acordo com um id e uma data.
     * 
     * @param id ID do voo
     * @param data Data do voo a reservar
     */
    @Override
    public void reservarVoo(String id, LocalDateTime data) throws VooInexistenteException {
        // TODO Implementar este método
        
    }

    /**
     * Cancela um voo já reservado.
     * 
     * @param id ID da reserva
     */
    @Override
    public void cancelarVoo(String id) throws ReservaInexistenteException {
        // TODO Implementar este método
        
    }

    /**
     * Insere um novo voo na lista de voos fazendo todas as verificações necessárias. 
     * Para executar este método é necessário um nível de autoridade igual ou superior a 1 (Administrador).
     * 
     * @param partida Local de partida
     * @param destino Local de chegada
     * @param capacidade Capacidade do voo
     * 
     * TODO: Adicionar uma verificação para o nível de permissões do utilizador.
     */
    @Override
    public void addInfo(String partida, String destino, int capacidade)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException {
        if(capacidade <= 0) {
            throw new CapacidadeInvalidaException("Capacidade: " + capacidade);
        }
        if(partida.equals(destino)) {
            throw new PartidaDestinoIguaisException("Partida: " + partida + " | Destino: " + destino);
        }

        String id = partida + ":" + destino;
        if(voos.containsKey(id)) {
            throw new VooExisteException("ID: " + id + " já existe!");
        }

        Voo novoVoo = new Voo(partida,destino,capacidade);
        voos.put(id, novoVoo);
    }

    /**
     * Encerra o dia.
     * Se o dia já estiver encerrado atira um DiaJaEncerradoException.
     */
    @Override
    public void encerrarDia() throws DiaJaEncerradoException {
        if(!diaAberto) {
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
        if(diaAberto) {
            throw new DiaJaAbertoException();
        }
        diaAberto = true;
    }

    /**
     * Reserva uma série de voos de acordo com uma percurso,
     * uma data de início e fim.
     * Ou pelo menos foi o que percebi do exercício.
     * 
     * @param voos Lista de voos
     * @param dataInicio Limite inferior da data de reserva
     * @param dataFim Limite superior da data de reserva
     */
    @Override
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException {
        // TODO Implementar este método
        
    }

    /**
     * Retorna a lista de todos os voos registados no sistema.
     * 
     * @return Lista com todos os voos do sistema.
     */
    @Override
    public List<Voo> obterListaVoo() {
        return voos.values().stream()
                            .map(v -> new Voo(v))
                            .collect(Collectors.toList());
    }

    /**
     * Retorna uma lista com todos os percursos possíveis
     * que começem em partida e acabem em destino
     * com um máximo de 3 saltos.
     * 
     * @param partida Local de partida
     * @param destino Local de destino
     * @return Lista com todos os caminhos possíveis começados em partida
     *         e acabados em destino com um máximo de 3 saltos
     */
    @Override
    public List<Voo> obterPercursosPossiveis(String partida, String destino) {
        // TODO Implementar este método
        return null;
    }
    
}
