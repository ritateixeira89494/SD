package uni.sd.ln.server.ssvoos.exceptions;

public class VooExisteException extends Exception{
    public static final String Tipo = "VooExiste";

    public VooExisteException() {
        super();
    }
    public VooExisteException(String msg) {
        super(msg);
    }
}
