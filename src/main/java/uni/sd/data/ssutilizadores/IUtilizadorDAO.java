package uni.sd.data.ssutilizadores;

import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;

public abstract class IUtilizadorDAO {
    public static final String FILEPATH = "utilizadores.csv";

    public abstract void saveUtilizador(Utilizador u);
    public abstract Utilizador getUtilizador();
}
