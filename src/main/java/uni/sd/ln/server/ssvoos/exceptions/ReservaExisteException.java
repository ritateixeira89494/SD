package uni.sd.ln.server.ssvoos.exceptions;

public class ReservaExisteException extends Exception {
    public static final String Tipo = "ReservaExiste";

    public ReservaExisteException() {
        super();
    }

    public ReservaExisteException(String message) {
        super(message);
    }
}
