package uni.sd.ln.ssutilizadores;

import uni.sd.ln.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.data.ssutilizadores.exceptions.UtilizadorExisteException;

public interface ISSUtilizador {
    public boolean autenticar(String username, String password) throws CredenciaisErradasException;
    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;
}