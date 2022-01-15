package uni.sd.ln.server;

import uni.sd.data.IDados;
import uni.sd.ln.server.ssutilizadores.ISSUtilizador;
import uni.sd.ln.server.ssutilizadores.SSUtilizadorFacade;
import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.ISSVoo;
import uni.sd.ln.server.ssvoos.SSVooFacade;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.utils.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class LN implements Iln {
    ISSUtilizador userFacade;
    ISSVoo vooFacade;

    public LN(IDados daos) throws SQLException {
        userFacade = new SSUtilizadorFacade(daos);
        vooFacade = new SSVooFacade(daos);
    }

    @Override
    public Pair<String, Integer> autenticar(String username, String password) throws CredenciaisErradasException, SQLException, UtilizadorInexistenteException {
        return userFacade.autenticar(username, password);
    }

    @Override
    public void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, SQLException {
        userFacade.registar(email, username, password, authority);
    }

    @Override
    public int reservarVoo(String email, String partida, String destino, LocalDateTime data)
            throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException,
            ReservaInexistenteException {
        return vooFacade.reservarVoo(email, partida, destino, data);
    }

    @Override
    public void cancelarVoo(int id) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException {
        vooFacade.cancelarVoo(id);
    }

    @Override
    public void addInfo(String partida, String destino, int capacidade, int duracao)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException, DuracaoInvalidaException {
        vooFacade.addInfo(partida, destino, capacidade, duracao);
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
    public List<Integer> reservarVooPorPercurso(String email, List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        return vooFacade.reservarVooPorPercurso(email, voos, dataInicio, dataFim);
    }

    @Override
    public List<Voo> obterListaVoo() throws SQLException {
        return vooFacade.obterListaVoo();
    }

    @Override
    public List<List<String>> obterPercursosPossiveis(String partida, String destino) throws VooInexistenteException, SQLException {
        return vooFacade.obterPercursosPossiveis(partida, destino);
    }

}
