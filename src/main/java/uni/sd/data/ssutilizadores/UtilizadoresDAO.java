package uni.sd.data.ssutilizadores;


import uni.sd.data.ssutilizadores.exceptions.UtilizadorNaoExisteException;
import uni.sd.data.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.ssutilizadores.utilizadores.Administrador;
import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.ssutilizadores.utilizadores.UtilizadorNormal;

import java.sql.*;


public class UtilizadoresDAO implements IUtilizadoresDAO{
    private Connection conn;

    public UtilizadoresDAO() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd_db", "root", "rootPass12345");
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
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from Utilizador where Email = '" + u.getEmail() + "'");
        if(rs.next()) {
            System.out.println("Valor já existe!!");
            throw new UtilizadorExisteException();
        }

        stmt.executeUpdate(
                "insert into Utilizador(Email, Nome, Password, Tipo) value ("
                        + "'" + u.getEmail() + "', "
                        + "'" + u.getUsername() + "', "
                        + "'" + u.getPassword() + "', "
                        + "'" + u.getAuthority() + "')"
        );

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
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("insert into UtilizadorNormal(idUtilizadorNormal) value (LAST_INSERT_ID())");
    }

    /**
     * Função auxiliar que adiciona u utilizador adicionado na função saveUtilizador
     * à tabela Administrador
     * @param admin Administrador a adicionar à base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    private void saveAdministrador(Administrador admin) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("insert into Administrador(idAdministrador) value (LAST_INSERT_ID())");
    }

    /**
     * Obtém um utilizador da base de dados através do seu email.
     *
     * @param email Email do utilizador a obter
     * @return Utilizador com o email passado como argumento
     * @throws UtilizadorNaoExisteException Caso um utilizador com o email dado não exista na base de dados
     * @throws SQLException Caso haja algum problema com a base de dados
     */
    @Override
    public Utilizador getUtilizador(String email) throws UtilizadorNaoExisteException, SQLException {
        Statement stmt = conn.createStatement();
        Utilizador u;

        ResultSet rs = stmt.executeQuery("select * from Utilizador where Email = '" + email + "'");
        if(!rs.next()) {
            throw new UtilizadorNaoExisteException();
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
                System.out.println("WTF?! Este tipo não existe!");
                throw new UtilizadorNaoExisteException();
        }

        return u;
    }

    /**
     * Atualiza o username e a password de um utilizador com as do Utilizador passado
     * como parâmetro.
     *
     * @param u Utilizador com a informação atualizada
     * @throws SQLException Caso haja algum problema com a base de dados
     * @throws UtilizadorNaoExisteException Caso um utilizador com o email do utilizador
     *                                      passado como argumento não esteja registado na base de dados
     */
    @Override
    public void updateUtilizador(Utilizador u) throws SQLException, UtilizadorNaoExisteException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select * from Utilizador where Email = '" + u.getEmail() + "'");
        if(!rs.next()) {
            throw new UtilizadorNaoExisteException();
        }

        stmt.executeUpdate(
                "update Utilizador set "
                        + "Nome = '" + u.getUsername() + "', "
                        + "Password = '" + u.getPassword() + "' "
                        + "where Email = '" + u.getEmail() + "'"
                );
    }

}
