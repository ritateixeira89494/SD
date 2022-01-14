package uni.sd.ln.server.ssvoos;

import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISSVoo {
    int reservarVoo(String email, String partida, String destino, LocalDateTime data) throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;

    void cancelarVoo(int id) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException;

    void addInfo(String partida, String destino, int capacidade, int duracao) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException;

    void encerrarDia() throws DiaJaEncerradoException;

    void abrirDia() throws DiaJaAbertoException;

    List<Integer> reservarVooPorPercurso(String email, List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;

    List<Voo> obterListaVoo() throws SQLException;

    List<Integer> obterPercursosPossiveis(String partida, String destino);
}
