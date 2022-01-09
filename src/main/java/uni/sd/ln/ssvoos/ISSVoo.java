package uni.sd.ln.ssvoos;

import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssvoos.exceptions.CapacidadeInvalidaException;
import uni.sd.ln.ssvoos.exceptions.DataInvalidaException;
import uni.sd.ln.ssvoos.exceptions.DiaJaAbertoException;
import uni.sd.ln.ssvoos.exceptions.DiaJaEncerradoException;
import uni.sd.ln.ssvoos.exceptions.PartidaDestinoIguaisException;
import uni.sd.ln.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.ssvoos.exceptions.VooExisteException;
import uni.sd.ln.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.ssvoos.voos.Voo;

public interface ISSVoo {
    public void reservarVoo(Voo idVoo, LocalDateTime data) throws VooInexistenteException;
    public void cancelarVoo(String idReserva) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
