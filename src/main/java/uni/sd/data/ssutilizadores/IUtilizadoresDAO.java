package uni.sd.data.ssutilizadores;

import uni.sd.ln.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;

import java.sql.SQLException;

public interface IUtilizadoresDAO {
    void saveUtilizador(Utilizador u) throws SQLException, UtilizadorExisteException;
    Utilizador getUtilizador(String email) throws UtilizadorInexistenteException, SQLException;
    void updateUtilizador(Utilizador u) throws SQLException, UtilizadorInexistenteException;
    void removeUtilizador(String email) throws SQLException, UtilizadorInexistenteException;
}
