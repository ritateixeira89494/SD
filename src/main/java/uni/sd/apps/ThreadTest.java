package uni.sd.apps;

import uni.sd.ln.client.ILN;
import uni.sd.ln.client.LN;
import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;

import java.io.IOException;
import java.time.LocalDateTime;

public class ThreadTest {
    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            int j = i;
            Thread t = new Thread(() -> {
                try {
                    ILN ln = new LN();
                    String email = j + "@email.com";
                    String username = j + "Thread";
                    String password = j + "Password";
                    //ln.registar(email, username, password, 0);
                    ln.autenticar(email, password);
                    ln.reservarVoo("Porto", "Paris", LocalDateTime.of(2022, 3, 12, 14, 30).plusDays(j));
                } catch (VooInexistenteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UtilizadorInexistenteException e) {
                    e.printStackTrace();
                } catch (ReservaExisteException e) {
                    e.printStackTrace();
                } catch (ReservaInexistenteException e) {
                    e.printStackTrace();
                } catch (CredenciaisErradasException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
}
