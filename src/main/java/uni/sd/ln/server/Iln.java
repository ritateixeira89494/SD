package uni.sd.ln.server;

import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.time.LocalDate;
import java.util.List;

public interface Iln { 
    int autenticar(String username, String password) throws CredenciaisErradasException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;

    int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException;
    void cancelarVoo(int id) throws ReservaInexistenteException;
    void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    void encerrarDia() throws DiaJaEncerradoException;
    void abrirDia() throws DiaJaAbertoException;
    void reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    List<Voo> obterListaVoo();
    List<Voo> obterPercursosPossiveis(String partida, String destino);
}
