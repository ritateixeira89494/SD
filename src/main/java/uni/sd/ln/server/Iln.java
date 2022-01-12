package uni.sd.ln.server;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;

import uni.sd.ln.server.ssvoos.exceptions.CapacidadeInvalidaException;
import uni.sd.ln.server.ssvoos.exceptions.DataInvalidaException;
import uni.sd.ln.server.ssvoos.exceptions.DiaJaAbertoException;
import uni.sd.ln.server.ssvoos.exceptions.DiaJaEncerradoException;
import uni.sd.ln.server.ssvoos.exceptions.PartidaDestinoIguaisException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.voos.Voo;

public interface Iln { 
    public int autenticar(String username, String password) throws CredenciaisErradasException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;

    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException;
    public void cancelarVoo(String id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
