package uni.sd.ln.server.ssutilizadores.exceptions;

public class PasswordInvalidaException extends Exception {
    public static final String Tipo = "PasswordInvalida";

    public PasswordInvalidaException() {
        super();
    }
    public PasswordInvalidaException(String msg) {
        super(msg);
    }
}
