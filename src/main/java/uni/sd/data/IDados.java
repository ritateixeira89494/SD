package uni.sd.data;

import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.util.Map;

public interface IDados {
    void saveUtilizadores(Map<String, Utilizador> us);

    Map<String, Utilizador> getUtilizadores();

    void saveVoos(Map<String, Voo> vs);

    Map<String, Voo> getVoos();
}
