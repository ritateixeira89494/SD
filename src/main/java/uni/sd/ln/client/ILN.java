package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ILN {
    public int autenticar(String username, String password) throws CredenciaisErradasException, IOException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException;

    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, IOException;
    public void cancelarVoo(String id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
