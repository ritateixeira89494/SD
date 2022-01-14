package uni.sd.ln.server.ssvoos.exceptions;

public class DataInvalidaException extends Exception {
    public static final String Tipo = "DataInvalida";

    public DataInvalidaException() {
        super();
    }
    public DataInvalidaException(String msg) {
        super(msg);
    }
}
