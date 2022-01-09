package uni.sd.ln;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssutilizadores.exceptions.*;

import uni.sd.ln.ssvoos.exceptions.*;
import uni.sd.ln.ssvoos.voos.Voo;

public interface Iln { 
    public boolean autenticar(String username, String password) throws CredenciaisErradasException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;

    public void reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException;
    public void cancelarVoo(int id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
