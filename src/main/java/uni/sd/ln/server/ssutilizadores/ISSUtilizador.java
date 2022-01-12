package uni.sd.ln.server.ssutilizadores;

import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;

public interface ISSUtilizador {
    public int autenticar(String username, String password) throws CredenciaisErradasException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;
}