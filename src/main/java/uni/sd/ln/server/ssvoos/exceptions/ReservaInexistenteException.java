package uni.sd.ln.server.ssvoos.exceptions;

public class ReservaInexistenteException extends Exception {
    public static final String Tipo = "ReservaInexistente";

    public ReservaInexistenteException() {
        super();
    }
    public ReservaInexistenteException(String msg) {
        super(msg);
    }
}
