package uni.sd.ln.server.ssvoos.exceptions;

public class SemReservaDisponivelException extends Exception {
    public static final String Tipo = "SemReservaDisponivel";

    public SemReservaDisponivelException() {
        super();
    }
    public SemReservaDisponivelException(String msg) {
        super(msg);
    }
}
