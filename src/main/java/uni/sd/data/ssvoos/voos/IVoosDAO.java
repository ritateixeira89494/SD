package uni.sd.data.ssvoos.voos;

import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IVoosDAO {
    void saveVoo(Voo v) throws SQLException, VooExisteException;

    Voo getVoo(String partida, String destino) throws SQLException, VooInexistenteException;

    Map<String, Voo> getVooPorPartida(String partida) throws SQLException, VooInexistenteException;

    Map<String, Voo> getVooPorDestino(String destino) throws SQLException, VooInexistenteException;

    void updateVoo(Voo v) throws VooInexistenteException, SQLException;

    void removeVoo(String partida, String destino) throws SQLException, VooInexistenteException;

    List<Voo> getTodosVoos() throws SQLException;
}
