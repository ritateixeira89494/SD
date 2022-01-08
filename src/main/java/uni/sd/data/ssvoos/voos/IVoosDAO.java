package uni.sd.data.ssvoos.voos;

import uni.sd.ln.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.voos.Voo;

import java.sql.SQLException;
import java.util.Map;

public interface IVoosDAO {
    void saveVoo(Voo v) throws SQLException, VooExisteException;
    Map<String, Voo> getVooPorPartida(String partida) throws SQLException, VooInexistenteException;
    Map<String, Voo> getVooPorDestino(String destino) throws SQLException, VooInexistenteException;
    void updateVoo(Voo v) throws VooInexistenteException, SQLException;
}
