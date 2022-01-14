package uni.sd.data;

import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.reservas.Reserva;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IDados {
    // Métodos do IUtilizadoresDAO
    void saveUtilizador(Utilizador u) throws SQLException, UtilizadorExisteException;
    Utilizador getUtilizador(String email) throws UtilizadorInexistenteException, SQLException;
    void updateUtilizador(Utilizador u) throws SQLException, UtilizadorInexistenteException;
    void removeUtilizador(String email) throws SQLException, UtilizadorInexistenteException;

    // Métodos do IReservasDAO
    int saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException;
    Reserva getReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
    Reserva getReservaPorID(int id) throws SQLException, ReservaInexistenteException, UtilizadorInexistenteException, VooInexistenteException;
    int getIDReserva(String email, String partida, String destino, LocalDateTime data) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
    Map<Integer, Reserva> getTodasReservasUtilizador(String email) throws SQLException, UtilizadorInexistenteException, VooInexistenteException;
    void removeReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;

    // Métodos do IVoosDAO
    void saveVoo(Voo v) throws SQLException, VooExisteException;
    Voo getVoo(String partida, String destino) throws SQLException, VooInexistenteException;
    Map<String, Voo> getVooPorPartida(String partida) throws SQLException, VooInexistenteException;
    Map<String, Voo> getVooPorDestino(String destino) throws SQLException, VooInexistenteException;
    void updateVoo(Voo v) throws VooInexistenteException, SQLException;
    void removeVoo(String partida, String destino) throws SQLException, VooInexistenteException;
    List<Voo> getTodosVoos() throws SQLException;
}
