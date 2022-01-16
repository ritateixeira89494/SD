package uni.sd.data.ssvoos.reservas;

import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.reservas.Reserva;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class ReservasDAO implements IReservasDAO {
    private final Connection conn;
    private final Lock reservaLock;
    private final Lock userLock;
    private final Lock vooLock;

    public ReservasDAO(Connection conn, Lock reservaLock, Lock userLock, Lock vooLock) {
        this.conn = conn;
        this.reservaLock = reservaLock;
        this.userLock = userLock;
        this.vooLock = vooLock;
    }

    /**
     * Guarda um novo registo de reserva na base de dados.
     *
     * @param r Reserva a ser guardada.
     * @return ID da reserva guardada
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador associado à reserva não exista
     * @throws VooInexistenteException Caso o voo reservado não exista
     * @throws ReservaExisteException Caso já tenha sido feita uma reserva do mesmo voo, pelo mesmo utilizador
     *                                e para o mesmo dia.
     */
    @Override
    public int saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException, SemReservaDisponivelException {
        Timestamp dataVoo = Timestamp.valueOf(r.getDataVoo());
        Timestamp dataReserva = Timestamp.valueOf(r.getDataReserva());

        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setTimestamp(3, dataVoo);

        PreparedStatement savePS = conn.prepareStatement(
                "insert into Reserva(idUtilizador, idVoo, Data_Reserva, Data_Voo) value (?,?,?,?)"
        );
        savePS.setTimestamp(3, dataReserva);
        savePS.setTimestamp(4, dataVoo);

        PreparedStatement vooPS = conn.prepareStatement(
                "select idVoo, Capacidade from Voo where Partida = ? and Destino = ?"
        );
        vooPS.setString(1, r.getPartida());
        vooPS.setString(2, r.getDestino());

        PreparedStatement reservasPS = conn.prepareStatement(
                "select count(*) from Reserva where idVoo = ? and Data_Voo = ?"
        );
        reservasPS.setTimestamp(2, dataVoo);
        // Obtemos todos os locks
        userLock.lock();
        vooLock.lock();
        reservaLock.lock();
        // Obtemos o id do utilizador e libertamos
        // o lock de utilizadores
        int idUtilizador = getUtilizadorID(r.getEmailUtilizador());
        userLock.unlock();
        // Obtemos o id e capacidades do voo e libertamos
        // o lock de voos
        ResultSet vooRS = vooPS.executeQuery();
        if(!vooRS.next()) {
            vooLock.unlock();
            reservaLock.unlock();
            throw new VooInexistenteException();
        }
        int idVoo = vooRS.getInt("idVoo");
        int capacidade = vooRS.getInt("Capacidade");
        vooLock.unlock();
        // Adicionamos os à query
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        // Executamos a query e verificamos
        // Se a reserva já existe
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            // Antes de atirarmos a exception, libertamos o lock
            reservaLock.unlock();
            throw new ReservaExisteException();
        }

        reservasPS.setInt(1, idVoo);
        ResultSet reservas = reservasPS.executeQuery();
        reservas.next();
        int ocupacao = reservas.getInt(1);
        if(ocupacao == capacidade) {
            reservaLock.unlock();
            throw new SemReservaDisponivelException();
        }

        // Adicionamos os ids ao insert
        savePS.setInt(1, idUtilizador);
        savePS.setInt(2, idVoo);
        // Excecutamos o insert e, de seguida, libertamos o lock
        savePS.executeUpdate();
        reservaLock.unlock();

        try {
            return getIDReserva(r.getEmailUtilizador(), r.getPartida(), r.getDestino(), r.getDataVoo());
        } catch (ReservaInexistenteException e) {
            System.out.println("WTF!!! Como!!!");
            return -1;
        }
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
    public Reserva getReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        Timestamp dataVooDate = Timestamp.valueOf(dataVoo);
        // Obtemos todos os locks
        userLock.lock();
        vooLock.lock();
        reservaLock.lock();
        // Obtemos os ids e libertamos os locks
        int idUtilizador = getUtilizadorID(email);
        userLock.unlock();
        int idVoo = getVooID(partida, destino);
        vooLock.unlock();
        // Obtemos a data de reserva e damos unlock
        Timestamp dataReserva = getDataReserva(idUtilizador, idVoo, dataVooDate);
        reservaLock.unlock();

        // Se chegamos aqui, então a partida, o destino, a dataVoo e a reserva são válidos,
        // visto que todos os métodos acima atiram as respetivas exceções caso algo
        // esteja mal. Por causa disso, não precisamos de os ir buscar a base de dados e podemos utilizar os
        // parâmetros passados.
        return new Reserva(email, partida, destino, dataVoo, dataReserva.toLocalDateTime());
    }

    /**
     * Obtem uma reserva através do seu ID
     *
     * @param id ID da encomenda a procurar
     * @return Reserva com o ID passado como argumento
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws ReservaInexistenteException Caso uma reserva com aquele ID não exista
     * @throws UtilizadorInexistenteException Caso o Utilizador da reserva não exista.
     * @throws VooInexistenteException Caso o Voo da reserva não exista.
     */
    @Override
    public Reserva getReservaPorID(int id) throws SQLException, ReservaInexistenteException, UtilizadorInexistenteException, VooInexistenteException {
        // Criamos todas as statements e preenchemos os
        // campos que já podem ser preenchidos
        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idReserva = ?"
        );
        ps.setInt(1, id);

        PreparedStatement userPS = conn.prepareStatement(
                "select Email from Utilizador where idUtilizador = ?"
        );

        PreparedStatement vooPS = conn.prepareStatement(
                "select Partida, Destino from Voo where idVoo = ?"
        );

        // Obtemos todos os locks
        userLock.lock();
        vooLock.lock();
        reservaLock.lock();

        // Executamos a query à tabela de Reserva e libertamos o lock
        ResultSet rs = ps.executeQuery();
        reservaLock.unlock();
        if(!rs.next()) {
            userLock.unlock();
            vooLock.unlock();
            throw new ReservaInexistenteException();
        }
        int idUtilizador = rs.getInt("idUtilizador");
        int idVoo = rs.getInt("idVoo");
        LocalDateTime dataVoo = rs.getTimestamp("Data_Voo").toLocalDateTime();
        LocalDateTime dataReserva = rs.getTimestamp("Data_Reserva").toLocalDateTime();

        // Obtemos o email do utilizador e libertamos o lock
        userPS.setInt(1, idUtilizador);
        rs = userPS.executeQuery();
        userLock.unlock();
        if(!rs.next()) {
            vooLock.unlock();
            throw new UtilizadorInexistenteException();
        }
        String email = rs.getString("Email");

        // Obtemos a partida e o destino do voo e libertamos o lock
        vooPS.setInt(1, idVoo);
        rs = vooPS.executeQuery();
        vooLock.unlock();
        if(!rs.next()) {
            throw new VooInexistenteException();
        }
        String partida = rs.getString("Partida");
        String destino = rs.getString("Destino");

        return new Reserva(email, partida, destino, dataVoo, dataReserva);
    }

    /**
     * Obtem todas as reservas feitas por um utilizador.
     *
     * @param email Email do utilizador a procurar
     * @return Map com todas as reservas desse utilizador.
     *         Nota: A chave do Map é o id da reserva e o valor a própria reserva
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador não exista
     * @throws VooInexistenteException Caso o voo de alguma reserva não exista
     */
    @Override
    public Map<Integer, Reserva> getTodasReservasUtilizador(String email) throws SQLException, UtilizadorInexistenteException, VooInexistenteException {
        Map<Integer, Reserva> reservas = new HashMap<>();

        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idUtilizador = ?"
        );
        PreparedStatement vooPS = conn.prepareStatement(
                "select Partida, Destino from Voo where idVoo = ?"
        );

        userLock.lock();
        vooLock.lock();
        reservaLock.lock();

        int idUtilizador = getUtilizadorID(email);
        userLock.unlock();
        ps.setInt(1, idUtilizador);
        ResultSet rs = ps.executeQuery();
        reservaLock.unlock();

        while(rs.next()) {
            // Obtemos a data do voo e da reserva
            LocalDateTime dataVoo = rs.getTimestamp("Data_Voo").toLocalDateTime();
            LocalDateTime dataReserva = rs.getTimestamp("Data_Reserva").toLocalDateTime();
            int idVoo = rs.getInt("idVoo");
            // Colocamos o ID do voo na statement e fazemos a query
            vooPS.setInt(1, idVoo);
            ResultSet rs2 = vooPS.executeQuery();
            if(!rs2.next()) {
                vooLock.unlock();
                throw new VooInexistenteException();
            }
            String partida = rs2.getString("Partida");
            String destino = rs2.getString("Destino");

            reservas.put(rs.getInt("idReserva"), new Reserva(email, partida, destino, dataVoo, dataReserva));
        }
        // Infelizmente só podemos libertar este lock depois
        // do ciclo while
        vooLock.unlock();

        return reservas;
    }

    /**
     * Remove uma reserva da base de dados, caso esta exista, claro.
     * @param email Email associado à reserva
     * @param partida Partida do voo reservado
     * @param destino Destino do voo reservado
     * @param dataVoo Data do voo reservado
     *
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador da reserva não exista
     * @throws VooInexistenteException Caso o voo reservado não exista
     * @throws ReservaInexistenteException Caso a reserva não exista
     */
    @Override
    public void removeReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        Timestamp dataVooTS = Timestamp.valueOf(dataVoo);

        PreparedStatement ps = conn.prepareStatement(
                "delete from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setTimestamp(3, dataVooTS);

        // Obter os locks
        userLock.lock();
        vooLock.lock();
        reservaLock.lock();

        // Obter os ids e libertar os locks respetivos
        int idUtilizador = getUtilizadorID(email);
        userLock.lock();
        int idVoo = getVooID(partida, destino);
        vooLock.lock();

        // Aqui usamos a função getDataReserva só para 
        // verificar se a reserva existe. Nós não precisamos
        // do return para nada neste caso.
        getDataReserva(idUtilizador, idVoo, dataVooTS);

        // Preencher os campos que faltam e executar o update
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.executeUpdate();

        reservaLock.unlock();
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
    private Timestamp getDataReserva(int idUtilizador, int idVoo, Timestamp dataVoo) throws ReservaInexistenteException, SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "select * from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ps.setTimestamp(3, dataVoo);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new ReservaInexistenteException();
        }

        return rs.getTimestamp("Data_Reserva");
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
            userLock.unlock();
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
            vooLock.unlock();
            throw new VooInexistenteException();
        }

        return rs.getInt("idVoo");
    }


    /**
     * Obtém o ID de uma reserva.
     *
     * @param email Email do utilizador que fez a reserva
     * @param partida Partida do voo
     * @param destino Destino do voo
     * @param data Data do voo
     * @return ID da reserva
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso o utilizador não exista
     * @throws VooInexistenteException Caso o voo não exista
     * @throws ReservaInexistenteException Caso a reserva não exista
     */
    @Override
    public int getIDReserva(String email, String partida, String destino, LocalDateTime data) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException {
        PreparedStatement ps = conn.prepareStatement(
                "select idReserva from Reserva where idUtilizador = ? and idVoo = ? and Data_Voo = ?"
        );
        ps.setTimestamp(3, Timestamp.valueOf(data));

        userLock.lock();
        vooLock.lock();
        reservaLock.lock();

        int idUtilizador = getUtilizadorID(email);
        userLock.unlock();
        int idVoo = getVooID(partida, destino);
        vooLock.unlock();

        ps.setInt(1, idUtilizador);
        ps.setInt(2, idVoo);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            reservaLock.unlock();
            throw new ReservaInexistenteException();
        }
        reservaLock.unlock();

        return rs.getInt("idReserva");
    }
}
