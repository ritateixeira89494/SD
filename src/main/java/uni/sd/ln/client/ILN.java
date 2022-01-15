package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.utils.Pair;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ILN {
    Pair<String, Integer> autenticar(String username, String password) throws CredenciaisErradasException, IOException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException;
            // Sign up

    int reservarVoo(String partida, String destino, LocalDateTime data) throws VooInexistenteException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    // Normal
    void cancelarVoo(int id) throws ReservaInexistenteException, IOException, VooInexistenteException, UtilizadorInexistenteException;
    // Normal
    void addInfo(String partida, String destino, int capacidade, int duracao) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException, DuracaoInvalidaException;
    // Administrador
    void encerrarDia() throws DiaJaEncerradoException, IOException;
    // Administrador
    void abrirDia() throws DiaJaAbertoException, IOException;
    // Administrador
    void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    // Normal
    List<Voo> obterListaVoo() throws IOException;
    // Normal
    List<Voo> obterPercursosPossiveis(String partida, String destino) throws IOException;
    // Normal
}
