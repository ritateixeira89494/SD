package uni.sd.ln.ssutilizadores.utilizadores;

public class UtilizadorNormal extends Utilizador {
    /**
     * Autoridade do utilizador. Isto indica que tarefas pode executar no programa.
     * Esta classe tem apenas as permissões mais basicas.
     */
    public static final int AUTHORITY = 0;

    public UtilizadorNormal(String email, String username, String password) {
        super(email, username, password);
    }

    @Override
    public int getAuthority() {
        return AUTHORITY;
    }
}
