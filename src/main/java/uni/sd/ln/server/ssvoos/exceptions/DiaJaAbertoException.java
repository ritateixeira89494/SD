package uni.sd.ln.server.ssvoos.exceptions;

public class DiaJaAbertoException extends Exception {
    public static final String Tipo = "DiaJaAberto";

    public DiaJaAbertoException() {
        super();
    }
    public DiaJaAbertoException(String msg) {
        super(msg);
    }
}
