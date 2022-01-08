package uni.sd.data.ssvoos.reservas;

import uni.sd.ln.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.reservas.Reserva;

import java.sql.*;
import java.time.LocalDate;

public class ReservasDAO implements IReservasDAO {
    Connection conn;

    public ReservasDAO() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd_db", "root", "rootPass12345");
    }

    /**
     * Guarda um novo registo de reserva na base de dados.
     * 
     * @param r Reserva a ser guardada.
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador associado à reserva não exista
     * @throws VooInexistenteException Caso o voo reservado não exista
     * @throws ReservaExisteException Caso já tenha sido feita uma reserva do mesmo voo, pelo mesmo utilizador
     *                                e para o mesmo dia.
     */
    @Override
    public void saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException {
        int idUtilizador = getUtilizadorID(r.getEmailUtilizador());
        int idVoo = getVooID(r.getPartida(), r.getDestino());

        java.sql.Date dataVoo = Date.valueOf(r.getDataVoo());
        java.sql.Date dataReserva = Date.valueOf(r.getDataReserva());

        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.setDate(3, dataVoo);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            throw new ReservaExisteException();
        }

        ps = conn.prepareStatement(
                "insert into Reserva(idUtilizador, idVoo, Data_Reserva, Data_Voo) value (?,?,?,?)"
        );

        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.setDate(3, dataReserva);
        ps.setDate(4, dataVoo);
        ps.executeUpdate();
    }

    /**
     * Obtem a reserva com a informação passada como parâmetro.
     *
     * @param email Email do utilizador associado à reserva
     * @param partida Ponto de partida do voo reservado
     * @param destino Ponto de destino do voo reservado
     * @param dataVoo Data do voo
     * @return Reserva feita
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador não exista
     * @throws VooInexistenteException Caso o voo não exista
     * @throws ReservaInexistenteException Caso a reserva não exista
     */
    @Override
    public Reserva getReserva(String email, String partida, String destino, LocalDate dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        int idUtilizador = getUtilizadorID(email);
        int idVoo = getVooID(partida, destino);
        java.sql.Date dataVooDate = java.sql.Date.valueOf(dataVoo);
        java.sql.Date dataReserva = getDataReserva(idUtilizador, idVoo, dataVooDate);
        
        // Se chegamos aqui, então a partida, o destino, a dataVoo e a reserva são válidos,
        // visto que todos os métodos acima atiram as respetivas exceções caso algo
        // esteja mal. Por causa disso, não precisamos de os ir buscar a base de dados e podemos utilizar os
        // parâmetros passados.
        return new Reserva(email, partida, destino, dataVoo, dataReserva.toLocalDate());
    }

    /**
     * Remove uma reserva da base de dados, caso esta exista, claro.
     * @param r Reserva a remover
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador da reserva não exista
     * @throws VooInexistenteException Caso o voo reservado não exista
     * @throws ReservaInexistenteException Caso a reserva não exista
     */
    @Override
    public void removeReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        int idUtilizador = getUtilizadorID(r.getEmailUtilizador());
        int idVoo = getVooID(r.getPartida(), r.getDestino());
        java.sql.Date dataVoo = Date.valueOf(r.getDataVoo());

        // Aqui usamos a função getDataReserva só para 
        // verificar se a reserva existe. Nós não precisamos
        // do return para nada neste caso.
        getDataReserva(idUtilizador, idVoo, dataVoo);

        PreparedStatement ps = conn.prepareStatement(
                "delete from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.setDate(3, dataVoo);
        ps.executeUpdate();
    }

    /**
     * Função auxiliar que verifica se uma reserva existe
     * e retorna a data de reserva.
     * Inicialmente era suposto só verificar se a reserva existe.
     * Porém é mais útil que retorne a data de reserva, visto ser
     * necessária para a função getReserva.
     *
     * @param idUtilizador ID do utilizador associado à reserva
     * @param idVoo ID do voo associado à reserva
     * @param dataVoo Data do voo reservado
     * @return Data de reserva da reserva
     * @throws ReservaInexistenteException Caso a reserva não exista
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    private java.sql.Date getDataReserva(int idUtilizador, int idVoo, java.sql.Date dataVoo) throws ReservaInexistenteException, SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.setDate(3, dataVoo);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new ReservaInexistenteException();
        }

        return rs.getDate("Data_Reserva");
    }

    /**
     * Obtém o ID do utilizador associado ao email passado como parâmetro.
     * Esta função, inicialmente, estava na classe UtilizadoresDAO, porém
     * necessitaria de estar a criar uma instância da classe só para poder
     * chamar este método. Por isso, decidi que era melhor ficar aqui.
     *
     * @param email Email do utilizador
     * @return ID do utilizador associado ao email
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso nenhum utilizador esteja registado
     *                                        com este email
     */
    private int getUtilizadorID(String email) throws SQLException, UtilizadorInexistenteException {
        PreparedStatement ps = conn.prepareStatement("select idUtilizador from Utilizador where Email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new UtilizadorInexistenteException();
        }

        return rs.getInt("idUtilizador");
    }

    /**
     * Obtém o id do voo com a partida e o destino passados como argumento.
     * Do mesmo modo que a função getUtilizadorID, esta função era suposto
     * estar na classe VoosDAO. No entanto, como necessitária de criar uma instância
     * desta só para chamar um método que não é utilizador por mais classe nenhuma,
     * decidi que era melhor coloca-la aqui.
     *
     * @param partida Ponto de partida do voo
     * @param destino Ponto de chegada do voo
     * @return ID do voo associado a este percurso
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws VooInexistenteException Caso não exista nenhum voo com este percurso
     */
    private int getVooID(String partida, String destino) throws SQLException, VooInexistenteException {
        PreparedStatement ps = conn.prepareStatement("select idVoo from Voo where Partida = ? and Destino = ?");
        ps.setString(1, partida);
        ps.setString(2, destino);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new VooInexistenteException();
        }

        return rs.getInt("idVoo");
    }
}
