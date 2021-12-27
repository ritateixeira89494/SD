package uni.sd.data.ssutilizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uni.sd.ln.ssutilizadores.utilizadores.Utilizador;

import org.reflections.Reflections;

public class UtilizadorDAO {
    /** 
     *  Ficheiro onde os utilizadores são guardados.
     *  Porquê CSV? Porque gosto de poder abrir a base de dados e alterar os valores num
     *  editor de texto sem ter que abrir o programa.
     *  Até guardava em base de dados mas não sei percebo nada disso e, por isso, não me vou
     *  pôr já a inventar.
     * 
     *  TODO: Mudar isto para uma base de dados. Se tiver tempo, claro.
     */
    public static final String FILEPATH = "utilizadores.csv";

    /**
     *  Guarda o Map com todos os utilizadores para o ficheiro de utilizadores.
     * 
     * @param us Map de utilizadores
     * @throws IOException Caso haja algum problema a abrir o filewriter
     */
    public static void saveUtilizadores(Map<String,Utilizador> us) throws IOException {
        File f = new File(FILEPATH);
        FileWriter fw = new FileWriter(f);
        try {
            f.createNewFile();

            Reflections r = new Reflections("uni.sd");
            Set<Class<? extends Utilizador>> subClasses = r.getSubTypesOf(Utilizador.class);

            for(Utilizador u: us.values()) {
                for(Class<? extends Utilizador> classe: subClasses) {
                    if(u.getClass() == classe) {
                        
                    }
                }
            }
        } finally {
            fw.close();            
        }
    }

    public static Map<String,Utilizador> getUtilizadores() throws IOException {
        Map<String,Utilizador> users = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(FILEPATH)));

            String line;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split(";");

                Utilizador u; 
            }
        } catch (FileNotFoundException e) {}

        return users;
    }
}
