package uni.sd.ln.server;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.server.ssutilizadores.ISSUtilizador;
import uni.sd.ln.server.ssutilizadores.SSUtilizadorFacade;
import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;

import uni.sd.ln.server.ssvoos.ISSVoo;
import uni.sd.ln.server.ssvoos.SSVooFacade;
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

public class LN implements Iln {

    ISSUtilizador userFacade = new SSUtilizadorFacade();
    ISSVoo vooFacade = new SSVooFacade();

    @Override
    public int autenticar(String username, String password) throws CredenciaisErradasException {
        return userFacade.autenticar(username, password);
    }

    @Override
    public void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException {
        userFacade.registar(email, username, password, authority);
    }

    @Override
    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException {
        return vooFacade.reservarVoo(partida, destino, data);
    }

    @Override
    public void cancelarVoo(int id) throws ReservaInexistenteException {
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
    public void reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim)
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
