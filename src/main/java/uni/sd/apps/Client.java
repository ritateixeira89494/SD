package uni.sd.apps;

import uni.sd.ln.client.LN;
import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ui.client.Login;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException, CredenciaisErradasException, UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException {
        LN ln = new LN();

        ln.registar("marco@email.com", "Marquinho", "password123", 0);
        int i = ln.autenticar("marco@email.com", "password123");
        //new Login(new LN());
    }
}
