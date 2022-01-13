package uni.sd.ln;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssutilizadores.ISSUtilizador;
import uni.sd.ln.ssutilizadores.SSUtilizadorFacade;
import uni.sd.ln.ssutilizadores.exceptions.*;

import uni.sd.ln.ssvoos.ISSVoo;
import uni.sd.ln.ssvoos.SSVooFacade;
import uni.sd.ln.ssvoos.exceptions.*;
import uni.sd.ln.ssvoos.voos.Voo;

public class LN implements Iln {
    private String email;
    ISSUtilizador userFacade = new SSUtilizadorFacade();
    ISSVoo vooFacade = new SSVooFacade(email);

    public LN() throws SQLException {
    }

    @Override
    public boolean autenticar(String username, String password) throws CredenciaisErradasException {
        return userFacade.autenticar(username, password);
    }

    @Override
    public void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException {
        userFacade.registar(email, username, password, authority);
    }

    @Override
    public int reservarVoo(String partida, String destino, LocalDate data)
            throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException,
            ReservaInexistenteException {

        return vooFacade.reservarVoo(partida, destino, data);
    }

    @Override
    public void cancelarVoo(int id) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException {
        vooFacade.cancelarVoo(id);
    }

    @Override
    public void addInfo(String partida, String destino, int capacidade)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException {
        vooFacade.addInfo(partida, destino, capacidade);
    }

    @Override
    public void encerrarDia() throws DiaJaEncerradoException {
        vooFacade.encerrarDia();
    }

    @Override
    public void abrirDia() throws DiaJaAbertoException {
        vooFacade.abrirDia();      
    }

    @Override
    public List<Integer> reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim)
            throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        return vooFacade.reservarVooPorPercurso(voos, dataInicio, dataFim);
    }

    @Override
    public List<Voo> obterListaVoo() throws SQLException {
        return vooFacade.obterListaVoo();
    }

    @Override
    public List<Integer> obterPercursosPossiveis(String partida, String destino) {
        return vooFacade.obterPercursosPossiveis(partida, destino);
    }
    
}
