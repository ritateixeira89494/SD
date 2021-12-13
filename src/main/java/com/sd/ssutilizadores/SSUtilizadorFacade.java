package com.sd.ssutilizadores;

import java.util.HashMap;
import java.util.Map;

import com.sd.ssutilizadores.exceptions.CredenciaisErradasException;
import com.sd.ssutilizadores.exceptions.PasswordInvalidaException;
import com.sd.ssutilizadores.exceptions.UsernameInvalidoException;
import com.sd.ssutilizadores.exceptions.UtilizadorExisteException;
import com.sd.ssutilizadores.utilizadores.Utilizador;
import com.sd.ssutilizadores.utilizadores.UtilizadorNormal;

public class SSUtilizadorFacade implements ISSUtilizador {
    /**
     * Lista de utilizadores.
     * TODO: Adicionar persistencia dos dados possivelmente com uma base de dados.
     */
    Map<String, Utilizador> users;
    
    public SSUtilizadorFacade() {
        users = new HashMap<>();
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
