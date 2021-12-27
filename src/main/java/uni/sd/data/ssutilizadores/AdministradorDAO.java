package uni.sd.data.ssutilizadores;

import java.io.FileWriter;
import java.io.IOException;

import uni.sd.ln.ssutilizadores.utilizadores.Administrador;

public class AdministradorDAO {
    public void saveUtilizador(Administrador u, FileWriter fw) throws IOException {
        String line = Administrador.AUTHORITY + ";" + u.getUsername() + ";" + u.getPassword() + "\n";
        fw.write(line);
    }
}
