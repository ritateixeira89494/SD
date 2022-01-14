package uni.sd.data.ssutilizadores;


import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssutilizadores.utilizadores.Administrador;
import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssutilizadores.utilizadores.UtilizadorNormal;

import java.sql.*;


public class UtilizadoresDAO implements IUtilizadoresDAO{
    private final Connection conn;

    public UtilizadoresDAO(Connection conn) throws SQLException {
        this.conn = conn;
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
        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, u.getEmail());
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            throw new UtilizadorExisteException();
        }

        ps = conn.prepareStatement(
                "insert into Utilizador(Email, Nome, Password, Tipo) value (?,?,?,?)"
        );
        ps.setString(1, u.getEmail());
        ps.setString(2, u.getUsername());
        ps.setString(3, u.getPassword());
        ps.setInt(4, u.getAuthority());
        ps.executeUpdate();

        if(u instanceof UtilizadorNormal) {
            saveUtilizadorNormal((UtilizadorNormal) u);
        } else if(u instanceof Administrador) {
            saveAdministrador((Administrador) u);
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

        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
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
        PreparedStatement ps = conn.prepareStatement(
                "select * from Utilizador where Email = ?"
        );
        ps.setString(1, u.getEmail());
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new UtilizadorInexistenteException();
        }

        ps = conn.prepareStatement(
                "update Utilizador set Nome = ?, Password = ? where Email = ?"
        );
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getEmail());
        ps.executeUpdate();
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
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new UtilizadorInexistenteException();
        }
        int tipo = rs.getInt("Tipo");
        int idUtilizador = rs.getInt("idUtilizador");

        ps = conn.prepareStatement(
                "delete from Utilizador where Email = ?"
        );
        ps.setString(1, email);
        ps.executeUpdate();

        switch (tipo) {
            case UtilizadorNormal.AUTHORITY:
                ps = conn.prepareStatement(
                        "delete from UtilizadorNormal where idUtilizadorNormal = ?"
                );
                break;
            case Administrador.AUTHORITY:
                ps = conn.prepareStatement(
                        "delete from Administrador where idAdministrador = ?"
                );
                break;
            default:
                System.out.println("WTF!! How did I get here!?");
                throw new UtilizadorInexistenteException();
        }
        ps.setInt(1, idUtilizador);
        ps.executeUpdate();
    }
}
