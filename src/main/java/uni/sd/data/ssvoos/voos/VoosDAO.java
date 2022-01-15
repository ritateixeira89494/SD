package uni.sd.data.ssvoos.voos;

import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.voos.Voo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class VoosDAO implements IVoosDAO{
    private final Connection conn;
    private final Lock vooLock;

    public VoosDAO(Connection conn, Lock vooLock) {
        this.conn = conn;
        this.vooLock = vooLock;
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
        /*
         * Criamos as statements antes de darmos lock
         * para reduzir o tempo que usamos o lock.
         */
        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Partida = ? and Destino = ?"
        );
        ps.setString(1, v.getPartida());
        ps.setString(2, v.getDestino());

        PreparedStatement savePS = conn.prepareStatement(
                "insert into Voo(Partida, Destino, Capacidade, Ocupacao, Duracao) values (?,?,?,?,?)"
        );
        savePS.setString(1, v.getPartida());
        savePS.setString(2, v.getDestino());
        savePS.setInt(3, v.getCapacidade());
        savePS.setInt(4, v.getOcupacao());
        savePS.setInt(5, v.getDuracao());

        vooLock.lock();
        try {
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                throw new VooExisteException();
            }

            savePS.executeUpdate();
        } finally {
            vooLock.unlock();
        }
    }

    /**
     * Obtem o voo com o ponto de partida e o destino passados como argumento.
     *
     * @param partida Ponto de partida do voo
     * @param destino Destino do Voo
     * @return Voo com o ponto de partida e o destino passados como argumento
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooInexistenteException Caso o voo não exista
     */
    @Override
    public Voo getVoo(String partida, String destino) throws SQLException, VooInexistenteException {
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Partida = ? and Destino = ?"
        );
        ps.setString(1, partida);
        ps.setString(2, destino);

        vooLock.lock();
        try {
            rs = ps.executeQuery();
        } finally {
            vooLock.unlock();
        }
        // Caso rs.next() seja null, o voo não existe
        if(!rs.next()) {
            throw new VooInexistenteException();
        }

        return new Voo(
                rs.getString("Partida"),
                rs.getString("Destino"),
                rs.getInt("Capacidade"),
                rs.getInt("Ocupacao"),
                rs.getInt("Duracao")
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
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Partida = ?"
        );
        ps.setString(1, partida);

        vooLock.lock();
        try {
            rs = ps.executeQuery();
        } finally {
            vooLock.unlock();
        }

        Map<String, Voo> voos = new HashMap<>();
        while(rs.next()) {
            Voo v = new Voo(rs.getString("Partida"),
                    rs.getString("Destino"),
                    rs.getInt("Capacidade"),
                    rs.getInt("Ocupacao"),
                    rs.getInt("Duracao")
            );
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
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Destino = ?"
        );
        ps.setString(1, destino);

        vooLock.lock();
        try {
            rs  = ps.executeQuery();
        } finally {
            vooLock.unlock();
        }

        Map<String, Voo> voos = new HashMap<>();
        while(rs.next()) {
            Voo v = new Voo(
                    rs.getString("Partida"),
                    rs.getString("Destino"),
                    rs.getInt("Capacidade"),
                    rs.getInt("Ocupacao"),
                    rs.getInt("Duracao")
            );
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
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Partida = ? and Destino = ?"
        );
        ps.setString(1, v.getPartida());
        ps.setString(2, v.getDestino());

        PreparedStatement updatePS = conn.prepareStatement(
                "update Voo set Ocupacao = ? where Partida = ? and Destino = ?"
        );
        updatePS.setInt(1, v.getCapacidade());
        updatePS.setString(2, v.getPartida());
        updatePS.setString(3, v.getDestino());

        vooLock.lock();
        try {
            rs = ps.executeQuery();
            if(!rs.next()) {
                throw new VooInexistenteException();
            }
            updatePS.executeUpdate();
        } finally {
            vooLock.unlock();
        }
    }

    /**
     * Remove um voo da base de dados.
     *
     * @param partida Ponto de partida do voo a remover
     * @param destino Destino do voo a remover
     * @throws SQLException Caso haja um problema com a base de dados
     * @throws VooInexistenteException Caso o voo não exista
     */
    @Override
    public void removeVoo(String partida, String destino) throws SQLException, VooInexistenteException {
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Voo where Partida = ? and Destino = ?"
        );
        ps.setString(1, partida);
        ps.setString(2, destino);

        PreparedStatement removePS = conn.prepareStatement(
                "delete from Voo where Partida = ? and Destino = ?"
        );
        removePS.setString(1, partida);
        removePS.setString(2, destino);

        vooLock.lock();
        try {
            rs = ps.executeQuery();
            if(!rs.next()) {
                throw new VooInexistenteException();
            }
            removePS.executeUpdate();
        } finally {
            vooLock.unlock();
        }
    }

    /**
     * Obtém a lista de todos os voos registados na base de dados.
     *
     * @return Lista com todos os voos existentes na base de dados
     * @throws SQLException Caso haja um problema com a base de dados
     */
    @Override
    public List<Voo> getTodosVoos() throws SQLException {
        ResultSet rs;
        PreparedStatement ps = conn.prepareStatement("select * from Voo");

        vooLock.lock();
        try {
            rs = ps.executeQuery();
        } finally {
            vooLock.unlock();
        }
        List<Voo> voos = new ArrayList<>();
        while(rs.next()) {
            Voo v = new Voo(
                    rs.getString("Partida"),
                    rs.getString("Destino"),
                    rs.getInt("Capacidade"),
                    rs.getInt("Ocupacao"),
                    rs.getInt("Duracao")
            );
            voos.add(v);
        }

        return voos;
    }
}
