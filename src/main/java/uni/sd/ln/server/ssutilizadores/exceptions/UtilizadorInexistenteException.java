package uni.sd.ln.server.ssutilizadores.exceptions;

public class UtilizadorInexistenteException extends Exception {
    public static final String Tipo = "UtilizadorInexistente";

    public UtilizadorInexistenteException() {
    }

    public UtilizadorInexistenteException(String message) {
        super(message);
    }
}
