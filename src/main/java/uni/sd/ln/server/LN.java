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

    private boolean diaAberto = true;

    public LN(IDados daos) throws SQLException {
        userFacade = new SSUtilizadorFacade(daos);
        vooFacade = new SSVooFacade(daos);
    }

    @Override
    public Pair<String, Integer> autenticar(String username, String password) throws CredenciaisErradasException, SQLException, UtilizadorInexistenteException, DiaJaEncerradoException {
        Pair<String, Integer> info = userFacade.autenticar(username, password);
        if(!diaAberto && info.getRight() < 1) {
            throw new DiaJaEncerradoException();
        }
        return info;
    }

    @Override
    public void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, SQLException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        userFacade.registar(email, username, password, authority);
    }

    @Override
    public int reservarVoo(String email, String partida, String destino, LocalDateTime data)
            throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException,
            ReservaInexistenteException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        return vooFacade.reservarVoo(email, partida, destino, data);
    }

    @Override
    public void cancelarVoo(int id) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        vooFacade.cancelarVoo(id);
    }

    @Override
    public void addInfo(String partida, String destino, int capacidade, int duracao)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException, DuracaoInvalidaException {
        vooFacade.addInfo(partida, destino, capacidade, duracao);
    }

    /**
     * Encerra o dia.
     * Se o dia já estiver encerrado atira um DiaJaEncerradoException.
     */
    @Override
    public void encerrarDia() throws DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        diaAberto = false;
    }

    /**
     * Abre o dia.
     * Se o dia já estiver aberto atira um DiaJaAbertoException.
     */
    @Override
    public void abrirDia() throws DiaJaAbertoException {
        if(diaAberto) {
            throw new DiaJaAbertoException();
        }
        diaAberto = true;
    }

    @Override
    public List<Integer> reservarVooPorPercurso(String email, List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        return vooFacade.reservarVooPorPercurso(email, voos, dataInicio, dataFim);
    }

    @Override
    public List<Voo> obterListaVoo() throws SQLException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        return vooFacade.obterListaVoo();
    }

    @Override
    public List<List<String>> obterPercursosPossiveis(String partida, String destino) throws VooInexistenteException, SQLException, DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        return vooFacade.obterPercursosPossiveis(partida, destino);
    }

}
