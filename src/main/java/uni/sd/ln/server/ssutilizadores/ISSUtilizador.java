package uni.sd.ln.server.ssutilizadores;

import uni.sd.ln.server.ssutilizadores.exceptions.*;

import java.sql.SQLException;

public interface ISSUtilizador {
    int autenticar(String username, String password) throws CredenciaisErradasException, SQLException, UtilizadorInexistenteException;

    void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, SQLException;
}