package uni.sd.data;

import java.util.Map;

import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.ssvoos.voos.Voo;

public interface IDados {
    public void saveUtilizadores(Map<String,Utilizador> us);
    public Map<String,Utilizador> getUtilizadores();

    public void saveVoos(Map<String,Voo> vs);
    public Map<String,Voo> getVoos();
}
