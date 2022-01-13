package uni.sd.ln.server.ssvoos.exceptions;

public class DiaJaEncerradoException extends Exception {
    public static final String Tipo = "DiaJaEncerrado";

    public DiaJaEncerradoException() {
        super();
    }
    public DiaJaEncerradoException(String msg) {
        super(msg);
    }
}
