package uni.sd.data.ssvoos.reservas;

import uni.sd.ln.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.reservas.Reserva;

import java.sql.SQLException;
import java.time.LocalDate;

public interface IReservasDAO {
    void saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException;
    Reserva getReserva(String email, String partida, String destino, LocalDate dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
    void removeReserva(String email, String partida, String destino, LocalDate dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
}
