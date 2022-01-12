package uni.sd.apps.server;

import uni.sd.ln.server.Iln;
import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssvoos.exceptions.CapacidadeInvalidaException;
import uni.sd.ln.server.ssvoos.exceptions.PartidaDestinoIguaisException;
import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.net.Frame;
import uni.sd.net.TaggedConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Runnable {
    TaggedConnection tc;
    boolean on = true;
    Iln ln;
    String email;

    public Worker(TaggedConnection tc, Iln ln) {
        this.tc = tc;
        this.ln = ln;
    }

    @Override
    public void run() {
        try {
            while(on) {
                Frame f = tc.receive();
                int tipo = f.getTipo();
                switch (tipo) {
                    case 0:
                        autenticar(f.getDados());
                        break;
                    case 1:
                        registar(f.getDados());
                        break;
                }
            }
            tc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autenticar(List<byte[]> dados) throws Exception {
        String email = new String(dados.get(0));
        String password = new String(dados.get(1));

        List<byte[]> respDados = new ArrayList<>();
        try {
            int authority = ln.autenticar(email, password);
            this.email = email;
            respDados.add("OK".getBytes(StandardCharsets.UTF_8));
            respDados.add((authority + "").getBytes(StandardCharsets.UTF_8));
        } catch (CredenciaisErradasException e) {
            respDados.add("CredenciaisErradas".getBytes(StandardCharsets.UTF_8));
        }
        tc.send(0, 0, respDados);
    }

    private void registar(List<byte[]> dados) throws IOException {
        String email = new String(dados.get(0));
        String username = new String(dados.get(1));
        String password = new String(dados.get(2));
        int authority = Integer.parseInt(new String(dados.get(3)));

        List<byte[]> respDados = new ArrayList<>();
        try {
            ln.registar(email, username, password, authority);
            respDados.add("OK".getBytes(StandardCharsets.UTF_8));
        } catch (UtilizadorExisteException e) {
            respDados.add("UtilizadorExiste".getBytes(StandardCharsets.UTF_8));
        } catch (UsernameInvalidoException e) {
            respDados.add("UsernameInvalido".getBytes(StandardCharsets.UTF_8));
        } catch (PasswordInvalidaException e) {
            respDados.add("PasswordInvalido".getBytes(StandardCharsets.UTF_8));
        }
        Frame f = new Frame(0, 1, respDados);
        tc.send(f);
    }

    private void reservarVoo(List<byte[]> dados) throws IOException {
        String partida = new String(dados.get(0));
        String destino = new String(dados.get(1));
        LocalDate data = LocalDate.parse(new String(dados.get(2)), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<byte[]> respDados = new ArrayList<>();
        try {
            int id = ln.reservarVoo(partida, destino, data);
            respDados.add("OK".getBytes(StandardCharsets.UTF_8));
            respDados.add((id + "").getBytes(StandardCharsets.UTF_8));
        } catch (VooInexistenteException e) {
            respDados.add("VooInexistente".getBytes(StandardCharsets.UTF_8));
        }
        tc.send(0, 2, respDados);
    }

    private void cancelarVoo(List<byte[]> dados) {

    }

    private void addInfo(List<byte[]> dados) {
        String partida = new String(dados.get(0));
        String destino = new String(dados.get(1));
        int capacidade = Integer.parseInt(new String(dados.get(2)));

        //List<byte[]>
        try {
            ln.addInfo(partida, destino, capacidade);
        } catch (VooExisteException e) {

        } catch (CapacidadeInvalidaException e) {
            e.printStackTrace();
        } catch (PartidaDestinoIguaisException e) {
            e.printStackTrace();
        }
    }
}
