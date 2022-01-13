package uni.sd.ln.server.ssvoos.exceptions;

public class PartidaDestinoIguaisException extends Exception {
    public static final String Tipo = "PartidaDestinoIguais";

    public PartidaDestinoIguaisException() {
        super();
    }
    public PartidaDestinoIguaisException(String msg) {
        super(msg);
    }
}
