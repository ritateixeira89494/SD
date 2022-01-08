package uni.sd.ln;

import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssutilizadores.ISSUtilizador;
import uni.sd.ln.ssutilizadores.SSUtilizadorFacade;
import uni.sd.ln.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.ssutilizadores.exceptions.UtilizadorExisteException;

import uni.sd.ln.ssvoos.ISSVoo;
import uni.sd.ln.ssvoos.SSVooFacade;
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

public class LN implements Iln {

    ISSUtilizador userFacade = new SSUtilizadorFacade();
    ISSVoo vooFacade = new SSVooFacade();

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
    public void reservarVoo(String id, LocalDateTime data) throws VooInexistenteException {
        vooFacade.reservarVoo(id, data);
    }

    @Override
    public void cancelarVoo(String id) throws ReservaInexistenteException {
        vooFacade.cancelarVoo(id);
    }

    @Override
    public void addInfo(String partida, String destino, int capacidade)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException {
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
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException {
        vooFacade.reservarVooPorPercurso(voos, dataInicio, dataFim);
    }

    @Override
    public List<Voo> obterListaVoo() {
        return vooFacade.obterListaVoo();
    }

    @Override
    public List<Voo> obterPercursosPossiveis(String partida, String destino) {
        return vooFacade.obterPercursosPossiveis(partida, destino);
    }
    
}
