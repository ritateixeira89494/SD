package uni.sd.ln.server.ssvoos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.server.ssvoos.exceptions.CapacidadeInvalidaException;
import uni.sd.ln.server.ssvoos.exceptions.DataInvalidaException;
import uni.sd.ln.server.ssvoos.exceptions.DiaJaAbertoException;
import uni.sd.ln.server.ssvoos.exceptions.DiaJaEncerradoException;
import uni.sd.ln.server.ssvoos.exceptions.PartidaDestinoIguaisException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.server.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.voos.Voo;

public interface ISSVoo {
    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException;
    public void cancelarVoo(int id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
