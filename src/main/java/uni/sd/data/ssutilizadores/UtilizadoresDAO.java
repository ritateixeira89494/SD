package uni.sd.data.ssutilizadores;


import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssutilizadores.utilizadores.Administrador;
import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssutilizadores.utilizadores.UtilizadorNormal;

import java.sql.*;
import java.util.concurrent.locks.Lock;


public class UtilizadoresDAO implements IUtilizadoresDAO{
    private final Connection conn;
    private final Lock userLock;

    public UtilizadoresDAO(Connection conn, Lock userLock) {
        this.conn = conn;
        this.userLock = userLock;
    }

    /**
     * Guarda um novo utilizador na base de dados.
     * Caso já exista um utilizador com o mesmo email, é atirada uma exception.
     *
     * @param u Utilizador a inserir na base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorExisteException Caso um utilizador com o mesmo email já esteja registado
     *                                   na base de dados
     */
    @Override
    public void saveUtilizador(Utilizador u) throws SQLException, UtilizadorExisteException {
        /* Começamos por criar ambas as statements.
         * Nós criamos a statement de inserção antes de fazermos
         * qualquer query para que usemos o lock
         * o mínimo de tempo possível.
         */
        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, u.getEmail());

        PreparedStatement savePS = conn.prepareStatement(
                "insert into Utilizador(Email, Nome, Password, Tipo) value (?,?,?,?)"
        );
        savePS.setString(1, u.getEmail());
        savePS.setString(2, u.getUsername());
        savePS.setString(3, u.getPassword());
        savePS.setInt(4, u.getAuthority());

        // Aqui damos Lock para fazermos a query e se for o caso
        // o insert na base de dados.
        try{
            userLock.lock();
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                throw new UtilizadorExisteException();
            }
            savePS.executeUpdate();

            // Aqui inserimos o utilizador na respetiva tabela
            // dependendo da sua classe
            if(u instanceof UtilizadorNormal) {
                saveUtilizadorNormal((UtilizadorNormal) u);
            } else if(u instanceof Administrador) {
                saveAdministrador((Administrador) u);
            }
        } finally {
            userLock.unlock();
        }

    }

    /**
     * Função auxiliar que adiciona o utilizador adicionado na função saveUtilizador
     * à tabela UtilizadorNormal.
     *
     * @param u Utilizador a adicionar à base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    private void saveUtilizadorNormal(UtilizadorNormal u) throws SQLException {
        conn.createStatement().executeUpdate(
                "insert into UtilizadorNormal(idUtilizadorNormal) value (LAST_INSERT_ID())"
        );
    }

    /**
     * Função auxiliar que adiciona u utilizador adicionado na função saveUtilizador
     * à tabela Administrador
     * @param admin Administrador a adicionar à base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    private void saveAdministrador(Administrador admin) throws SQLException {
        conn.createStatement().executeUpdate(
                "insert into Administrador(idAdministrador) value (LAST_INSERT_ID())"
        );
    }

    /**
     * Obtém um utilizador da base de dados através do seu email.
     *
     * @param email Email do utilizador a obter
     * @return Utilizador com o email passado como argumento
     * @throws UtilizadorInexistenteException Caso um utilizador com o email dado não exista na base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    @Override
    public Utilizador getUtilizador(String email) throws UtilizadorInexistenteException, SQLException {
        Utilizador u;
        ResultSet rs;

        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, email);

        // Damos lock para fazermos a query à base de dados
        // e imediatamente a seguir podemos liberta-lo.
        try {
            userLock.lock();
            rs = ps.executeQuery();
        } finally {
            userLock.unlock();
        }

        if(!rs.next()) {
            throw new UtilizadorInexistenteException();
        }
        String nome = rs.getString("Nome");
        String password = rs.getString("Password");
        switch(rs.getInt("Tipo")) {
            case UtilizadorNormal.AUTHORITY:
                u = new UtilizadorNormal(email, nome, password);
                break;
            case Administrador.AUTHORITY:
                u = new Administrador(email, nome, password);
                break;
            default:
                throw new UtilizadorInexistenteException();
        }

        return u;
    }

    /**
     * Atualiza o username e a password de um utilizador com as do Utilizador passado
     * como parâmetro.
     *
     * @param u Utilizador com a informação atualizada
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso um utilizador com o email do utilizador
     *                                      passado como argumento não esteja registado na base de dados
     */
    @Override
    public void updateUtilizador(Utilizador u) throws SQLException, UtilizadorInexistenteException {
        /*
         * Tal como no método saveUtilizador,
         * nós criamos ambas as statements antes
         * de qualquer query/update à base de dados
         * como forma de reduzir o tempo que cada thread
         * usa o lock.
         */
        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, u.getEmail());

        PreparedStatement updatePS = conn.prepareStatement(
                "update Utilizador set Nome = ?, Password = ? where Email = ?"
        );
        updatePS.setString(1, u.getUsername());
        updatePS.setString(2, u.getPassword());
        updatePS.setString(3, u.getEmail());

        // Lock para executar as queries
        try {
            userLock.lock();
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                throw new UtilizadorInexistenteException();
            }
            updatePS.executeUpdate();
        } finally {
            userLock.unlock();
        }
    }

    /**
     * Remove um utilizador da base de dados.
     *
     * @param email Email associado ao utilizador
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorInexistenteException Caso nenhum utilizador esteja registado com
     *                                        esse email
     */
    @Override
    public void removeUtilizador(String email) throws SQLException, UtilizadorInexistenteException {
        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, email);

        PreparedStatement removePS = conn.prepareStatement(
                "delete from Utilizador where Email = ?"
        );
        removePS.setString(1, email);

        /*
         * Infelizmente quando removemos um utilizador temos
         * que manter o lock para o removermos também da tabela
         * da sua classe.
         */
        try {
            userLock.lock();
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                throw new UtilizadorInexistenteException();
            }
            int tipo = rs.getInt("Tipo");
            int idUtilizador = rs.getInt("idUtilizador");

            removePS.executeUpdate();

            switch (tipo) {
                case UtilizadorNormal.AUTHORITY:
                    removePS = conn.prepareStatement(
                            "delete from UtilizadorNormal where idUtilizadorNormal = ?"
                    );
                    break;
                case Administrador.AUTHORITY:
                    removePS = conn.prepareStatement(
                            "delete from Administrador where idAdministrador = ?"
                    );
                    break;
                default:
                    System.out.println("WTF!! How did I get here!?");
                    throw new UtilizadorInexistenteException();
            }
            removePS.setInt(1, idUtilizador);
            removePS.executeUpdate();
        } finally {
            userLock.unlock();
        }
    }
}
