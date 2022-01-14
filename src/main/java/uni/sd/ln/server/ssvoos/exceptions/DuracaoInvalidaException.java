package uni.sd.ln.server.ssvoos.exceptions;

public class DuracaoInvalidaException extends Exception {
    public static final String Tipo = "DuracaoInvalida";

    public DuracaoInvalidaException() {
        super();
    }

    public DuracaoInvalidaException(String message) {
        super(message);
    }
}
