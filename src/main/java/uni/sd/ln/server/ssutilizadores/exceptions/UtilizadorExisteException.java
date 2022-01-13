package uni.sd.ln.server.ssutilizadores.exceptions;

public class UtilizadorExisteException extends Exception {
    public static final String Tipo = "UtilizadorExiste";

    public UtilizadorExisteException() {
        super();
    }
    public UtilizadorExisteException(String msg) {
        super(msg);
    }
}
