package uni.sd.apps.server;

import uni.sd.ln.server.Iln;
import uni.sd.ln.server.ssutilizadores.exceptions.*;
import uni.sd.ln.server.ssvoos.exceptions.*;
import uni.sd.ln.server.ssvoos.voos.Voo;
import uni.sd.net.Frame;
import uni.sd.net.TaggedConnection;
import uni.sd.net.TipoMensagem;
import uni.sd.utils.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Runnable {

    private final TaggedConnection tc;
    private final Iln ln;
    private String email;
    private int authority;

    public Worker(TaggedConnection tc, Iln ln) {
        this.tc = tc;
        this.ln = ln;
    }

    @Override
    public void run() {
        try {
            boolean on = true;
            while(on) {
                Frame f = tc.receive();
                String tipo = f.getTipo();
                switch (tipo) {
                    case TipoMensagem.AUTH:
                        autenticar(f.getDados());
                        break;
                    case TipoMensagem.REG:
                        registar(f.getDados());
                        break;
                    case TipoMensagem.RESVOO:
                        reservarVoo(f.getDados());
                        break;
                    case TipoMensagem.CANCELVOO:
                        cancelarVoo(f.getDados());
                        break;
                    case TipoMensagem.ADDINFO:
                        addInfo(f.getDados());
                        break;
                    case TipoMensagem.ENCDIA:
                        encerrarDia(f.getDados());
                        break;
                    case TipoMensagem.ABREDIA:
                        abrirDia(f.getDados());
                        break;
                    case TipoMensagem.RESERVPERCURSO:
                        reservarVooPorPercurso(f.getDados());
                        break;
                    case TipoMensagem.LSVOO:
                        obterListaVoo(f.getDados());
                        break;
                    case TipoMensagem.PERCURSOSPOS:
                        obterPercursosPossiveis(f.getDados());
                        break;
                }
            }
            tc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autenticar(List<String> dados) throws IOException, SQLException {
        String email = dados.get(0);
        String password = dados.get(1);

        String tipoResp;
        List<String> respDados = new ArrayList<>();
        try {
            Pair<String, Integer> info = ln.autenticar(email, password);
            this.email = email;
            this.authority = info.getRight();
            tipoResp = TipoMensagem.OK;
            respDados.add(info.getLeft());
            respDados.add(this.authority + "");
        } catch (CredenciaisErradasException | UtilizadorInexistenteException e) {
            tipoResp = CredenciaisErradasException.Tipo;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }
        tc.send(tipoResp, respDados);
    }

    private void registar(List<String> dados) throws IOException, SQLException {
        String email = (dados.get(0));
        String username = dados.get(1);
        String password = dados.get(2);
        int authority = Integer.parseInt(dados.get(3));

        String tipoDados;
        List<String> respDados = new ArrayList<>();
        try {
            ln.registar(email, username, password, authority);
            tipoDados = TipoMensagem.OK;
        } catch (UtilizadorExisteException e) {
            tipoDados = UtilizadorExisteException.Tipo;
        } catch (UsernameInvalidoException e) {
            tipoDados = UsernameInvalidoException.Tipo;
        } catch (PasswordInvalidaException e) {
            tipoDados = PasswordInvalidaException.Tipo;
        } catch (DiaJaEncerradoException e) {
            tipoDados = DiaJaEncerradoException.Tipo;
        }
        Frame f = new Frame(tipoDados, respDados);
        tc.send(f);
    }

    private void reservarVoo(List<String> dados) throws IOException, SQLException {
        String partida = dados.get(0);
        String destino = dados.get(1);
        LocalDateTime data = LocalDateTime.parse(dados.get(2), DateTimeFormatter.ISO_DATE_TIME);

        String tipoResp;
        List<String> respDados = new ArrayList<>();
        try {
            int id = ln.reservarVoo(email, partida, destino, data);
            tipoResp = TipoMensagem.OK;
            respDados.add(id + "");
        } catch (VooInexistenteException e) {
            tipoResp = VooInexistenteException.Tipo;
        } catch (UtilizadorInexistenteException e) {
            tipoResp = UtilizadorInexistenteException.Tipo;
        } catch (ReservaExisteException e) {
            tipoResp = ReservaExisteException.Tipo;
        } catch (ReservaInexistenteException e) {
            tipoResp = ReservaInexistenteException.Tipo;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        } catch (SemReservaDisponivelException e) {
            tipoResp = SemReservaDisponivelException.Tipo;
        }
        tc.send(tipoResp, respDados);
}

    private void cancelarVoo(List<String> dados) throws IOException, SQLException {
        String tipoResp;

        int ID = Integer.parseInt(dados.get(0));
        try {
            ln.cancelarVoo(ID);
            tipoResp = TipoMensagem.OK;
        } catch (ReservaInexistenteException e) {
            tipoResp = ReservaInexistenteException.Tipo;
        } catch (VooInexistenteException e) {
            tipoResp = VooInexistenteException.Tipo;
        } catch (UtilizadorInexistenteException e) {
            tipoResp = UtilizadorInexistenteException.Tipo;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }
        tc.send(tipoResp, new ArrayList<>());
}

    private void addInfo(List<String> dados) throws IOException, SQLException {
        String partida = dados.get(0);
        String destino = dados.get(1);
        int capacidade = Integer.parseInt(dados.get(2));
        int duracao = Integer.parseInt(dados.get(3));

        String tipoResp;
        List<String> resposta = new ArrayList<>();
        try {
            ln.addInfo(partida, destino, capacidade, duracao);
            tipoResp = TipoMensagem.OK;
        } catch (VooExisteException e) {
            tipoResp = VooExisteException.Tipo;
        } catch (CapacidadeInvalidaException e) {
            tipoResp = CapacidadeInvalidaException.Tipo;
        } catch (PartidaDestinoIguaisException e) {
            tipoResp = PartidaDestinoIguaisException.Tipo;
        } catch (DuracaoInvalidaException e) {
            tipoResp = DuracaoInvalidaException.Tipo;
        }
        tc.send(tipoResp, resposta);
    }

    private void encerrarDia(List<String> dados) throws IOException {
        String tipoResp;
        try {
            ln.encerrarDia();
            tipoResp = TipoMensagem.OK;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }
        tc.send(tipoResp, new ArrayList<>());
    }

    private void abrirDia(List<String> dados) throws IOException {
        String tipoResp;
        try {
            ln.abrirDia();
            tipoResp = TipoMensagem.OK;
        } catch (DiaJaAbertoException e) {
            tipoResp = DiaJaAbertoException.Tipo;
        }
        tc.send(tipoResp, new ArrayList<>());
    }

    private void reservarVooPorPercurso(List<String> dados) throws IOException, SQLException {
        String tipoResp;

        LocalDateTime dataInicio = LocalDateTime.parse(dados.get(0), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime dataFim =  LocalDateTime.parse(dados.get(1), DateTimeFormatter.ISO_DATE_TIME);

        List<String> pontos = new ArrayList<>();
        for(int i = 2; i < dados.size(); i++) {
            pontos.add(dados.get(i));
        }

        try {
            ln.reservarVooPorPercurso(email, pontos, dataInicio, dataFim);
            tipoResp = TipoMensagem.OK;
        } catch (VooInexistenteException e) {
            tipoResp = VooExisteException.Tipo;
        } catch (DataInvalidaException e) {
            tipoResp = DataInvalidaException.Tipo;
        } catch (SemReservaDisponivelException e) {
            tipoResp = SemReservaDisponivelException.Tipo;
        } catch (UtilizadorInexistenteException e) {
            tipoResp = UtilizadorInexistenteException.Tipo;
        } catch (ReservaExisteException e) {
            tipoResp = ReservaExisteException.Tipo;
        } catch (ReservaInexistenteException e) {
            tipoResp = ReservaInexistenteException.Tipo;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }

        tc.send(tipoResp, new ArrayList<>());
}

    private void obterListaVoo(List<String> dados) throws IOException, SQLException {
        List<String> respDados = new ArrayList<>();
        String tipoResp;
        try {
            List<Voo> voos = ln.obterListaVoo();

            for(Voo v: voos) {
                respDados.add(v.getPartida());
                respDados.add(v.getDestino());
                respDados.add(v.getCapacidade() + "");
                respDados.add(v.getDuracao() + "");
            }
            tipoResp = TipoMensagem.OK;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }
    tc.send(tipoResp, respDados);
    }

    private void obterPercursosPossiveis(List<String> dados) throws VooInexistenteException, SQLException, IOException {
        String partida = dados.get(0);
        String destino = dados.get(1);

        String tipoResp;
        List<String> respDados = new ArrayList<>();
        try {
            List<List<String>> caminhos = ln.obterPercursosPossiveis(partida, destino);
            for(List<String> caminho: caminhos) {
                StringBuilder sb = new StringBuilder();
                int i;
                for(i = 0; i < caminho.size() - 1; i++) {
                    sb.append(caminho.get(i));
                    sb.append(";");
                }
                sb.append(caminho.get(i));
                respDados.add(sb.toString());
            }
            tipoResp = TipoMensagem.OK;
        } catch (DiaJaEncerradoException e) {
            tipoResp = DiaJaEncerradoException.Tipo;
        }
        tc.send(tipoResp, respDados);
    }
}
