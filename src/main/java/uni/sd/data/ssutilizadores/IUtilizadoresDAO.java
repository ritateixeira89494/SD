package uni.sd.data.ssutilizadores;

import uni.sd.data.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.data.ssutilizadores.exceptions.UtilizadorNaoExisteException;
import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;

import java.sql.SQLException;

public interface IUtilizadoresDAO {
    void saveUtilizador(Utilizador u) throws SQLException, UtilizadorExisteException;
    Utilizador getUtilizador(String email) throws UtilizadorNaoExisteException, SQLException;
    void updateUtilizador(Utilizador u) throws SQLException, UtilizadorNaoExisteException;
}
