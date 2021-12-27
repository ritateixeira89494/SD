package uni.sd.ln.ssutilizadores;

import java.util.HashMap;
import java.util.Map;

import uni.sd.ln.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;
import uni.sd.ln.ssutilizadores.utilizadores.UtilizadorNormal;

public class SSUtilizadorFacade implements ISSUtilizador {
    /**
     * Lista de utilizadores.
     * TODO: Adicionar persistencia dos dados possivelmente com uma base de dados.
     */
    Map<String, Utilizador> users;
    
    public SSUtilizadorFacade() {
        users = new HashMap<>();
        /**
         *  Adicionei isto para testes. Este comentário serve
         *  para o caso me ter esquecido de remover isto antes de
         *  dar commit. 
         */
        users.put("lol",new UtilizadorNormal("lol", "12345"));
        users.put("abc",new UtilizadorNormal("abc", "69"));
        users.put("teste",new UtilizadorNormal("teste", "420"));
    }

    /**
     * Verifica o username e password inseridos e autentica, ou não, o utilizador.
     * 
     * @param username Username inserido pelo utilizador
     * @param password Password inserida pelo utilizador
     * @return Retorna true caso a autenticação seja bem sucedida. Caso contrário,
     * atira a CredenciaisErradasException.
     */
    @Override
    public boolean autenticar(String username, String password) throws CredenciaisErradasException {
        Utilizador user = users.get(username);
        if(user == null) {
            throw new CredenciaisErradasException("Utilizador não existe!");
        }
        if(!user.getPassword().equals(password)) {
            throw new CredenciaisErradasException("Password Errada!");
        }

        return true;
    }

    /**  
     * Regista um novo utilizador no sistema.
     * Este método verifica o limita o comprimento de ambos username e password
     * para evitar que alguém insira um username/password com 5GiB de tamanho, ou
     * um username/password demasiado curto.
     * 
     * @param username Username único do novo utilizador
     * @param password Password do utilizador
     * @param authority Autoridade do utilizador. Isto define a classe a ser criada
     *                  (UtilizadorNormal/Administrador), porém gostaria de mudar isto no futuro.
     * 
     * TODO: Substituir authority por uma implementação melhor 
     */
    @Override
    public void registar(String username, String password, int authority)
            throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException {
        if(
            username.length() < Utilizador.USERNAME_LENGTH_MINIMO ||
            username.length() > Utilizador.USERNAME_LENGTH_MAXIMO
        ) { throw new UsernameInvalidoException("Username inválido!"); }
        if(
            password.length() < Utilizador.PASSWORD_LENGTH_MINIMO ||
            password.length() > Utilizador.PASSWORD_LENGTH_MAXIMO
        ) { throw new PasswordInvalidaException("Password inválida!"); }
        
        if(users.get(username) != null) {
            throw new UtilizadorExisteException("Utilizador já existe!");
        }

        Utilizador novoUser;
        switch(authority) {
            case 0 : 
                novoUser = new UtilizadorNormal(username, password);
                break;
            case 1 :
                novoUser = new UtilizadorNormal(username, password);
                break;
            default :
                System.err.println("WTF!! How did i get here lol");
                throw new UsernameInvalidoException("Exception temporária! Nunca deveria ter chegado aqui, mas este é o mundo em que vivemos");
        }
        users.put(username, novoUser);
    }
    
}
