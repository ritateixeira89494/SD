package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.utils.Pair;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ILN {
    // Normal/Administrador
    Pair<String, Integer> autenticar(String username, String password) throws CredenciaisErradasException, IOException, DiaJaEncerradoException;
    // Normal/Administrador
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException, DiaJaEncerradoException;
    // Normal
    int reservarVoo(String partida, String destino, LocalDateTime data) throws VooInexistenteException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException, DiaJaEncerradoException, SemReservaDisponivelException;
    // Normal
    void cancelarVoo(int id) throws ReservaInexistenteException, IOException, VooInexistenteException, UtilizadorInexistenteException, DiaJaEncerradoException;
    // Administrador
    void addInfo(String partida, String destino, int capacidade, int duracao) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException, DuracaoInvalidaException;
    // Administrador
    void encerrarDia() throws DiaJaEncerradoException, IOException;
    // Administrador
    void abrirDia() throws DiaJaAbertoException, IOException;
    // Normal
    void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException, DiaJaEncerradoException;
    // Normal
    List<Voo> obterListaVoo() throws IOException, DiaJaEncerradoException;
    // Normal
    List<Voo> obterPercursosPossiveis(String partida, String destino) throws IOException, DiaJaEncerradoException;
}
