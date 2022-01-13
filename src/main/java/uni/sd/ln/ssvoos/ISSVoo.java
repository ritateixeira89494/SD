package uni.sd.ln.ssvoos;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uni.sd.ln.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.ssvoos.exceptions.*;
import uni.sd.ln.ssvoos.voos.Voo;

public interface ISSVoo {
    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    public void cancelarVoo(int idReserva) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public List<Integer> reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException;
    public List<Voo> obterListaVoo() throws SQLException;
    public List<Integer> obterPercursosPossiveis(String partida, String destino);
}
