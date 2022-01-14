package uni.sd.ln.server.ssvoos.exceptions;

public class VooInexistenteException extends Exception {
    public static final String Tipo = "VooInexistente";

    public VooInexistenteException() {
        super();
    }
    public VooInexistenteException(String msg) {
        super(msg);
    }
}
