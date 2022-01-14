package uni.sd.ln.server.ssvoos.voos;


import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ISSVoo {
    int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;

    void cancelarVoo(int idReserva) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException;

    void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException;

    void encerrarDia() throws DiaJaEncerradoException;

    void abrirDia() throws DiaJaAbertoException;

    List<Integer> reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;

    List<Voo> obterListaVoo() throws SQLException;

    List<Integer> obterPercursosPossiveis(String partida, String destino);
}
