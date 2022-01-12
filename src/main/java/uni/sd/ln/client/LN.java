package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;
import uni.sd.ln.server.ssutilizadores.exceptions.PasswordInvalidaException;
import uni.sd.ln.server.ssutilizadores.exceptions.UsernameInvalidoException;
import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorExisteException;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.net.Frame;
import uni.sd.net.TaggedConnection;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;

public class LN implements ILN {
    TaggedConnection tc;

    public LN() throws IOException {
        tc = new TaggedConnection(new Socket("localhost", 12345));
    }

    @Override
    public int autenticar(String email, String password) throws CredenciaisErradasException, IOException {
        List<byte[]> dados = new ArrayList<>();
        dados.add(email.getBytes(StandardCharsets.UTF_8));
        dados.add(password.getBytes(StandardCharsets.UTF_8));
        tc.send(0, 0, dados);

        Frame respostaFrame = tc.receive();
        String resposta = new String(respostaFrame.getDados().get(0));
        if(!resposta.equals("OK")) {
            throw new CredenciaisErradasException();
        }
        return Integer.parseInt(new String(respostaFrame.getDados().get(1)));
    }

    @Override
    public void registar(String email, String username, String password, int authority) throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException {
        List<byte[]> dados = new ArrayList<>();
        dados.add(email.getBytes(StandardCharsets.UTF_8));
        dados.add(username.getBytes(StandardCharsets.UTF_8));
        dados.add(password.getBytes(StandardCharsets.UTF_8));
        dados.add((authority + "").getBytes(StandardCharsets.UTF_8));
        tc.send(0, 1, dados);

        Frame respostaFrame = tc.receive();
        String resposta = new String(respostaFrame.getDados().get(0));
        switch(resposta) {
            case "UserExiste":
                throw new UtilizadorExisteException();
            case "UsernameInvalido":
                throw new UsernameInvalidoException();
            case "PasswordInvalida":
                throw new PasswordInvalidaException();
        }
    }

    @Override
    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, IOException {
        List<byte[]> dados = new ArrayList<>();
        dados.add(partida.getBytes(StandardCharsets.UTF_8));
        dados.add(partida.getBytes(StandardCharsets.UTF_8));
        dados.add(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).getBytes(StandardCharsets.UTF_8));

        tc.send(0, 2, dados);
        Frame f = tc.receive();

        String resposta = new String(f.getDados().get(0));
        if(resposta.equals("VooInexistente")) {
            throw new VooInexistenteException();
        }

        return Integer.parseInt(new String(f.getDados().get(1)));
    }

    @Override
    public void cancelarVoo(String id) throws ReservaInexistenteException {

    }

    @Override
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException {
        List<byte[]> dados = new ArrayList<>();
        dados.add(partida.getBytes(StandardCharsets.UTF_8));
        dados.add(destino.getBytes(StandardCharsets.UTF_8));
        dados.add((capacidade + "").getBytes(StandardCharsets.UTF_8));

        tc.send(0, 4, dados);
        Frame f = tc.receive();
        String resposta = new String(f.getDados().get(0));
        switch(resposta) {
            case "VooExiste":
                throw new VooExisteException();
            case "CapacidadeInvalida":
                throw new CapacidadeInvalidaException();
            case "PartidaDestinoIguais":
                throw new PartidaDestinoIguaisException();
        }
    }

    @Override
    public void encerrarDia() throws DiaJaEncerradoException {

    }

    @Override
    public void abrirDia() throws DiaJaAbertoException {

    }

    @Override
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException {

    }

    @Override
    public List<Voo> obterListaVoo() {
        return null;
    }

    @Override
    public List<Voo> obterPercursosPossiveis(String partida, String destino) {
        return null;
    }
}
