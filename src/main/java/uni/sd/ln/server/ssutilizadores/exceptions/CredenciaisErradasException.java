package uni.sd.ln.server.ssutilizadores.exceptions;

public class CredenciaisErradasException extends Exception {
    public static final String Tipo = "CredenciaisErradas";

    public CredenciaisErradasException() {
        super();
    }
    public CredenciaisErradasException(String msg) {
        super(msg);
    }
}
