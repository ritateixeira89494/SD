package uni.sd.ln.server.ssutilizadores;

import java.sql.SQLException;

import uni.sd.data.IDados;
import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssutilizadores.utilizadores.Administrador;
import uni.sd.ln.server.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.server.ssutilizadores.utilizadores.UtilizadorNormal;
import uni.sd.utils.Pair;

public class SSUtilizadorFacade implements ISSUtilizador {
    IDados daos;

    public SSUtilizadorFacade(IDados daos) {
        this.daos = daos;
    }

    /**
     * Verifica o username e password inseridos e autentica, ou não, o utilizador.
     * 
     * @param email Email inserido pelo utilizador
     * @param password Password inserida pelo utilizador
     * @return Retorna true caso a autenticação seja bem sucedida. Caso contrário,
     * atira a CredenciaisErradasException.
     */
    @Override
    public Pair<String, Integer> autenticar(String email, String password) throws CredenciaisErradasException, SQLException {
        try {
            Utilizador user = daos.getUtilizador(email);
            if(!user.getPassword().equals(password)) {
                throw new CredenciaisErradasException("Password Errada!");
            }
            return new Pair<>(user.getUsername(), user.getAuthority());
        } catch (UtilizadorInexistenteException e) {
            throw new CredenciaisErradasException();
        }
    }

    /**  
     * Regista um novo utilizador no sistema.
     * Este método verifica o limita o comprimento de ambos username e password
     * para evitar que alguém insira um username/password com 5GiB de tamanho, ou
     * um username/password demasiado curto.
     *
     * @param email Email único do novo utilizador
     * @param username Username do novo utilizador
     * @param password Password do utilizador
     * @param authority Autoridade do utilizador. Isto define a classe a ser criada
     *                  (UtilizadorNormal/Administrador), porém gostaria de mudar isto no futuro.
     * 
     */
    @Override
    public void registar(String email, String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, SQLException {
        if(
            username.length() < Utilizador.USERNAME_LENGTH_MINIMO ||
            username.length() > Utilizador.USERNAME_LENGTH_MAXIMO
        ) { throw new UsernameInvalidoException("Username inválido!"); }
        if(
            password.length() < Utilizador.PASSWORD_LENGTH_MINIMO ||
            password.length() > Utilizador.PASSWORD_LENGTH_MAXIMO
        ) { throw new PasswordInvalidaException("Password inválida!"); }

        Utilizador novoUser;
        switch(authority) {
            case 0 : 
                novoUser = new UtilizadorNormal(email, username, password);
                break;
            case 1 :
                novoUser = new Administrador(email, username, password);
                break;
            default :
                System.err.println("WTF!! How did i get here lol");
                throw new UsernameInvalidoException("Exception temporária! Nunca deveria ter chegado aqui, mas este é o mundo em que vivemos");
        }
        daos.saveUtilizador(novoUser);
    }
    
}
