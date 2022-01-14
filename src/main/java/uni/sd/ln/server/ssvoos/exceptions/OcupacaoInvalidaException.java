package uni.sd.ln.server.ssvoos.exceptions;

public class OcupacaoInvalidaException extends Exception {
    public static final String Tipo = "OcupacaoInvalida";

    public OcupacaoInvalidaException() {
        super();
    }

    public OcupacaoInvalidaException(String message) {
        super(message);
    }
}
