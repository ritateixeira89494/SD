package uni.sd.ln.ssutilizadores.utilizadores;

import uni.sd.data.ssutilizadores.AdministradorDAO;

public class Administrador extends Utilizador {
    /**
     * Autoridade do utilizador. Isto indica que tarefas pode executar no programa.
     * Esta classe tem permissões de um nivel mais elevado que o UtilizadorNormal,
     * que neste caso, é o de editar o sistema todo...
     * O nosso programa não é muito complexo ok!?
     */
    public static final int AUTHORITY = 1;

    public Administrador(String username, String password) {
        super(username, password);
    }

    @Override
    public int getAuthority() {
        return AUTHORITY;
    }

    @Override
    public Class<?> getDAO() {
        return AdministradorDAO.class;
    }
}