package uni.sd.ln.server.ssvoos.exceptions;

public class CapacidadeInvalidaException extends Exception {
    public static final String Tipo = "CapacidadeInvalida";

    public CapacidadeInvalidaException() {
        super();
    }
    public CapacidadeInvalidaException(String msg) {
        super(msg);
    }
}
