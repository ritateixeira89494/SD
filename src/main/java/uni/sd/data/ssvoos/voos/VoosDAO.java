package uni.sd.data.ssvoos.voos;

import uni.sd.ln.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.voos.Voo;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class VoosDAO implements IVoosDAO{
    final Connection conn;

    public VoosDAO() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd_db", "root", "rootPass12345");
    }

    /**
     * Guarda um novo voo na base de dados.
     * Este voo deve ser único. Caso já exista um voo
     * com a mesma partida e destino atira uma exceção.
     *
     * @param v Voo a ser guardado
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooExisteException Caso já exista um voo com a mesma partida
     *                            e destino na base de dados.
     */
    @Override
    public void saveVoo(Voo v) throws SQLException, VooExisteException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(
                "select * from Voo where "
                        + "Partida = '" + v.getPartida() + "' and "
                        + "Destino = '" + v.getDestino() + "'"
        );
        if(rs.next()) {
            throw new VooExisteException();
        }

        stmt.executeUpdate(
                "insert into Voo(Partida, Destino, Capacidade) values ("
                        + "'" + v.getPartida() + "', "
                        + "'" + v.getDestino() + "', "
                        + "'" + v.getCapacidade() + "')"
        );
    }

    /**
     * Obtem a lista de todos os voos que tenham "partida"
     * como bem... partida.
     * Os voos são guardados num Map em que a chave é o destino
     * do voo e o objeto é o próprio voo. Isto facilita a procura de
     * um voo específico.
     *
     * @param partida Cidade de partida do voo
     * @return Map com todos os voos com "partida" como ponto de partida.
     *         Nota: A chave do Map é o destino do voo.
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooInexistenteException Caso não exista nenhum voo com "partida" como partida
     */
    @Override
    public Map<String, Voo> getVooPorPartida(String partida) throws SQLException, VooInexistenteException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(
                "select * from Voo where "
                        + "Partida = '" + partida + "'"
        );
        if(!rs.next()) {
            throw new VooInexistenteException();
        }
        
        Map<String, Voo> voos = new HashMap<>();

        Voo v = new Voo(rs.getString("Partida"), rs.getString("Destino"), rs.getInt("Capacidade"));
        voos.put(v.getDestino(), v);

        while(rs.next()) {
            v = new Voo(rs.getString("Partida"), rs.getString("Destino"), rs.getInt("Capacidade"));
            voos.put(v.getDestino(), v);
        }
        
        return voos;
    }

    /**
     * Obtem a lista de todos os voos que tenham "destino"
     * como seu destino.
     * Os voos são guardados num Map em que a chave é a partida
     * do voo e o objeto é o próprio voo. Isto facilita a procura de
     * um voo específico.
     *
     * @param destino Cidade de destino do voo
     * @return Map com todos os voos com "destino" como cidade de destino.
     *         Nota: A chave do Map é a partida do voo.
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooInexistenteException Caso não exista nenhum voo com "destino" como destino
     */
    @Override
    public Map<String, Voo> getVooPorDestino(String destino) throws SQLException, VooInexistenteException {
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery(
                "select * from Voo where "
                        + "Destino = '" + destino + "'"
        );
        if(!rs.next()) {
            throw new VooInexistenteException();
        }
        
        Map<String, Voo> voos = new HashMap<>();

        Voo v = new Voo(rs.getString("Partida"), rs.getString("Destino"), rs.getInt("Capacidade"));
        voos.put(v.getPartida(), v);


        while(rs.next()) {
            v = new Voo(rs.getString("Partida"), rs.getString("Destino"), rs.getInt("Capacidade"));
            voos.put(v.getPartida(), v);
        }
        
        return voos;
    }

    /**
     * Atualiza a capacidade de um voo.
     * Nota: Isto não atualiza a partida nem o destino.
     *
     * @param v Voo com a informação atualizada
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooInexistenteException Caso não exista nenhum voo com a partida e destino do voo dado na base de dados
     */
    @Override
    public void updateVoo(Voo v) throws SQLException, VooInexistenteException  {
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery(
                "select * from Voo where "
                        + "Partida = '" + v.getPartida() + "' and "
                        + "Destino = '" + v.getDestino() + "'"
        );
        if(!rs.next()) {
            throw new VooInexistenteException();
        }

        stmt.executeUpdate(
                "update Voo set "
                        + "Capacidade = '" + v.getCapacidade() + "'"
                        + "where "
                            + "Partida = '" + v.getPartida() + "' and "
                            + "Destino = '" + v.getDestino() + "'"
        );
    }
}
