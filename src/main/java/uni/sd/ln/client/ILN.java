package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ILN {
    int autenticar(String username, String password) throws CredenciaisErradasException, IOException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException;

    int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    void cancelarVoo(int id) throws ReservaInexistenteException, IOException, VooInexistenteException, UtilizadorInexistenteException;
    void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException;
    void encerrarDia() throws DiaJaEncerradoException, IOException;
    void abrirDia() throws DiaJaAbertoException, IOException;
    void reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    List<Voo> obterListaVoo() throws IOException;
    List<Voo> obterPercursosPossiveis(String partida, String destino) throws IOException;
}
