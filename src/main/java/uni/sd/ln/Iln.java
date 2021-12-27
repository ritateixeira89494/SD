package uni.sd.ln;

import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.ssutilizadores.exceptions.UtilizadorExisteException;

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

public interface Iln { 
    public boolean autenticar(String username, String password) throws CredenciaisErradasException;
    public void registar(String username, String password, int authority) throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;
    
    public void reservarVoo(String id, LocalDateTime data) throws VooInexistenteException;
    public void cancelarVoo(String id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
