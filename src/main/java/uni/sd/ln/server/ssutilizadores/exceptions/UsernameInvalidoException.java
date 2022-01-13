package uni.sd.ln.server.ssutilizadores.exceptions;

public class UsernameInvalidoException extends Exception {
    public static final String Tipo = "UsernameInvalido";

    public UsernameInvalidoException() {
        super();
    }
    public UsernameInvalidoException(String msg) {
        super(msg);
    }
}
