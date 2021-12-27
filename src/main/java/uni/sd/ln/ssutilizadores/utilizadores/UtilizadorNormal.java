package uni.sd.ln.ssutilizadores.utilizadores;

import uni.sd.data.ssutilizadores.UtilizadorNormalDAO;

public class UtilizadorNormal extends Utilizador {
    /**
     * Autoridade do utilizador. Isto indica que tarefas pode executar no programa.
     * Esta classe tem apenas as permissões mais basicas.
     */
    public static final int AUTHORITY = 0;

    public UtilizadorNormal(String username, String password) {
        super(username, password);
    }

    @Override
    public int getAuthority() {
        return AUTHORITY;
    }

    @Override
    public Class<?> getDAO() {
        return UtilizadorNormalDAO.class;
    }
}
