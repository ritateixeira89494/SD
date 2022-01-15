package uni.sd.ln.client;

import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.net.Frame;
import uni.sd.net.TaggedConnection;
import uni.sd.net.TipoMensagem;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LN implements ILN {
    TaggedConnection tc;

    public LN() throws IOException {
        tc = new TaggedConnection(new Socket("localhost", 12345));
    }

    @Override
    public int autenticar(String email, String password) throws CredenciaisErradasException, IOException {
        List<String> dados = new ArrayList<>();
        dados.add(email);
        dados.add(password);
        tc.send("AUTH", dados);

        Frame respostaFrame = tc.receive();
        if(respostaFrame.getTipo().equals(CredenciaisErradasException.Tipo)) {
            throw new CredenciaisErradasException();
        }
        return Integer.parseInt(respostaFrame.getDados().get(0));
    }

    @Override
    public void registar(String email, String username, String password, int authority) throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException, IOException {
        List<String> dados = new ArrayList<>();
        dados.add(email);
        dados.add(username);
        dados.add(password);
        dados.add(authority + "");
        tc.send(TipoMensagem.REG, dados);

        Frame respostaFrame = tc.receive();
        switch(respostaFrame.getTipo()) {
            case UtilizadorExisteException.Tipo:
                throw new UtilizadorExisteException();
            case UsernameInvalidoException.Tipo:
                throw new UsernameInvalidoException();
            case PasswordInvalidaException.Tipo:
                throw new PasswordInvalidaException();
        }
    }

    @Override
    public int reservarVoo(String partida, String destino, LocalDateTime data) throws VooInexistenteException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        List<String> dados = new ArrayList<>();
        dados.add(partida);
        dados.add(destino);
        dados.add(data.format(DateTimeFormatter.ISO_DATE_TIME));

        tc.send(TipoMensagem.RESVOO, dados);
        Frame f = tc.receive();

        switch(f.getTipo()) {
            case VooInexistenteException.Tipo:
                throw new VooInexistenteException();
            case UtilizadorInexistenteException.Tipo:
                throw new UtilizadorInexistenteException();
            case ReservaExisteException.Tipo:
                throw new ReservaExisteException();
            case ReservaInexistenteException.Tipo:
                throw new ReservaInexistenteException();
        }

        return Integer.parseInt(f.getDados().get(0));
    }

    @Override
    public void cancelarVoo(int ID) throws ReservaInexistenteException, IOException, VooInexistenteException, UtilizadorInexistenteException {
        List<String> dados = new ArrayList<>();
        dados.add(ID + "");
        tc.send(TipoMensagem.CANCELVOO, dados);

        Frame f = tc.receive();
        switch(f.getTipo()) {
            case ReservaInexistenteException.Tipo:
                throw new ReservaInexistenteException();
            case VooInexistenteException.Tipo:
                throw new VooInexistenteException();
            case UtilizadorInexistenteException.Tipo:
                throw new UtilizadorInexistenteException();
        }
    }

    @Override
    public void addInfo(String partida, String destino, int capacidade, int duracao) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, IOException, DuracaoInvalidaException {
        List<String> dados = new ArrayList<>();
        dados.add(partida);
        dados.add(destino);
        dados.add(capacidade + "");
        dados.add(duracao + "");

        tc.send(TipoMensagem.ADDINFO, dados);
        Frame f = tc.receive();
        switch(f.getTipo()) {
            case VooExisteException.Tipo:
                throw new VooExisteException();
            case CapacidadeInvalidaException.Tipo:
                throw new CapacidadeInvalidaException();
            case PartidaDestinoIguaisException.Tipo:
                throw new PartidaDestinoIguaisException();
            case DuracaoInvalidaException.Tipo:
                throw new DuracaoInvalidaException();
        }
    }

    @Override
    public void encerrarDia() throws DiaJaEncerradoException, IOException {
        tc.send(TipoMensagem.ENCDIA, new ArrayList<>());
        Frame f = tc.receive();

        if(f.getTipo().equals(DiaJaEncerradoException.Tipo)) {
            throw new DiaJaEncerradoException();
        }
    }

    @Override
    public void abrirDia() throws DiaJaAbertoException, IOException {
        tc.send(TipoMensagem.ABREDIA, new ArrayList<>());
        Frame f = tc.receive();

        if(f.getTipo().equals(DiaJaAbertoException.Tipo)) {
            throw new DiaJaAbertoException();
        }
    }

    @Override
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, IOException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        List<String> dados = new ArrayList<>();
        dados.add(dataInicio.format(DateTimeFormatter.ISO_DATE_TIME));
        dados.add(dataFim.format(DateTimeFormatter.ISO_DATE_TIME));
        dados.addAll(voos);

        tc.send(TipoMensagem.RESERVPERCURSO, dados);
        Frame f = tc.receive();
        switch(f.getTipo()) {
            case VooInexistenteException.Tipo:
                throw new VooInexistenteException();
            case DataInvalidaException.Tipo:
                throw new DataInvalidaException();
            case SemReservaDisponivelException.Tipo:
                throw new SemReservaDisponivelException();
            case UtilizadorInexistenteException.Tipo:
                throw new UtilizadorInexistenteException();
            case ReservaExisteException.Tipo:
                throw new ReservaExisteException();
            case ReservaInexistenteException.Tipo:
                throw new ReservaInexistenteException();
        }
    }

    @Override
    public List<Voo> obterListaVoo() throws IOException {
        tc.send(TipoMensagem.LSVOO, new ArrayList<>());
        Frame f = tc.receive();

        List<String> dados = f.getDados();
        List<Voo> voos = new ArrayList<>();
        for(int i = 0; i < dados.size(); i+=5) {
            voos.add(new Voo(dados.get(i), dados.get(i+1), Integer.parseInt(dados.get(i+2)), Integer.parseInt(dados.get(i+3)), Integer.parseInt(dados.get(i+4))));
        }
        return voos;
    }

    @Override
    public List<Voo> obterPercursosPossiveis(String partida, String destino) throws IOException {
        List<String> dados = new ArrayList<>();
        dados.add(partida);
        dados.add(destino);

        tc.send(TipoMensagem.PERCURSOSPOS, dados);
        Frame f = tc.receive();
        List<String> respDados = f.getDados();

        List<Voo> voos = new ArrayList<>();
        for(int i = 0; i < respDados.size(); i+=5) {
            voos.add(new Voo(respDados.get(i), respDados.get(i+1), Integer.parseInt(respDados.get(i+2)), Integer.parseInt(dados.get(i+3)), Integer.parseInt(dados.get(i+4))));
        }

        return voos;
    }
}
