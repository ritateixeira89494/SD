package uni.sd.ln.server;

import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.utils.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface Iln {
    Pair<String, Integer> autenticar(String username, String password) throws CredenciaisErradasException, SQLException, UtilizadorInexistenteException, DiaJaEncerradoException;

    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, SQLException, DiaJaEncerradoException;

    int reservarVoo(String email, String partida, String destino, LocalDateTime data) throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException, DiaJaEncerradoException;

    void cancelarVoo(int id) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException, DiaJaEncerradoException;

    void addInfo(String partida, String destino, int capacidade, int duracao) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException, DuracaoInvalidaException;

    void encerrarDia() throws DiaJaEncerradoException;

    void abrirDia() throws DiaJaAbertoException;

    List<Integer> reservarVooPorPercurso(String email, List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException, DiaJaEncerradoException;

    List<Voo> obterListaVoo() throws SQLException, DiaJaEncerradoException;

    List<List<String>> obterPercursosPossiveis(String partida, String destino) throws VooInexistenteException, SQLException, DiaJaEncerradoException;
}
