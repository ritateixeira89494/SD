package uni.sd.data;

import uni.sd.data.ssutilizadores.IUtilizadoresDAO;
import uni.sd.data.ssutilizadores.UtilizadoresDAO;
import uni.sd.data.ssvoos.reservas.IReservasDAO;
import uni.sd.data.ssvoos.reservas.ReservasDAO;
import uni.sd.data.ssvoos.voos.IVoosDAO;
import uni.sd.data.ssvoos.voos.VoosDAO;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.reservas.Reserva;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DadosDAO implements IDados {
    private final IUtilizadoresDAO udao;
    private final IVoosDAO vdao;
    private final IReservasDAO rdao;

   public DadosDAO() throws SQLException {
       Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd_db", "sd_user", "");
       Lock reservaLock = new ReentrantLock();
       Lock userLock = new ReentrantLock();
       Lock vooLock = new ReentrantLock();

       udao = new UtilizadoresDAO(conn, userLock);
       vdao = new VoosDAO(conn, vooLock);
       rdao = new ReservasDAO(conn, reservaLock, userLock, vooLock);
   }

   // Métodos do IUtilizadoresDAO
    @Override
    public void saveUtilizador(Utilizador u) throws SQLException, UtilizadorExisteException {
        udao.saveUtilizador(u);
    }
    @Override
    public Utilizador getUtilizador(String email) throws UtilizadorInexistenteException, SQLException {
        return udao.getUtilizador(email);
    }
    @Override
    public void updateUtilizador(Utilizador u) throws SQLException, UtilizadorInexistenteException {
        udao.updateUtilizador(u);
    }
    @Override
    public void removeUtilizador(String email) throws SQLException, UtilizadorInexistenteException {
        udao.removeUtilizador(email);
    }

    // Métodos do IReservasDAO
    @Override
    public int saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException, SemReservaDisponivelException {
        return rdao.saveReserva(r);
    }
    @Override
    public Reserva getReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        return rdao.getReserva(email, partida, destino, dataVoo);
    }
    @Override
    public Reserva getReservaPorID(int id) throws SQLException, ReservaInexistenteException, UtilizadorInexistenteException, VooInexistenteException {
        return rdao.getReservaPorID(id);
    }
    @Override
    public int getIDReserva(String email, String partida, String destino, LocalDateTime data) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        return rdao.getIDReserva(email, partida, destino, data);
    }
    @Override
    public Map<Integer, Reserva> getTodasReservasUtilizador(String email) throws SQLException, UtilizadorInexistenteException, VooInexistenteException {
        return rdao.getTodasReservasUtilizador(email);
    }
    @Override
    public void removeReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        rdao.removeReserva(email, partida, destino, dataVoo);
    }

    // Métodos do IVoosDAO
    @Override
    public void saveVoo(Voo v) throws SQLException, VooExisteException {
       vdao.saveVoo(v);
    }
    @Override
    public Voo getVoo(String partida, String destino) throws SQLException, VooInexistenteException {
        return vdao.getVoo(partida, destino);
    }
    @Override
    public Map<String, Voo> getVooPorPartida(String partida) throws SQLException, VooInexistenteException {
        return vdao.getVooPorPartida(partida);
    }
    @Override
    public Map<String, Voo> getVooPorDestino(String destino) throws SQLException, VooInexistenteException {
        return vdao.getVooPorDestino(destino);
    }
    @Override
    public void updateVoo(Voo v) throws VooInexistenteException, SQLException {
        vdao.updateVoo(v);
    }
    @Override
    public void removeVoo(String partida, String destino) throws SQLException, VooInexistenteException {
        vdao.removeVoo(partida, destino);
    }
    @Override
    public List<Voo> getTodosVoos() throws SQLException {
        return vdao.getTodosVoos();
    }
}
