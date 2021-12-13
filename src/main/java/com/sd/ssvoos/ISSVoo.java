package com.sd.ssvoos;

import java.time.LocalDateTime;
import java.util.List;

import com.sd.ssvoos.exceptions.CapacidadeInvalidaException;
import com.sd.ssvoos.exceptions.DataInvalidaException;
import com.sd.ssvoos.exceptions.DiaJaAbertoException;
import com.sd.ssvoos.exceptions.DiaJaEncerradoException;
import com.sd.ssvoos.exceptions.PartidaDestinoIguaisException;
import com.sd.ssvoos.exceptions.ReservaInexistenteException;
import com.sd.ssvoos.exceptions.SemReservaDisponivelException;
import com.sd.ssvoos.exceptions.VooExisteException;
import com.sd.ssvoos.exceptions.VooInexistenteException;
import com.sd.ssvoos.voos.Voo;

public interface ISSVoo {
    public void reservarVoo(String id, LocalDateTime data) throws VooInexistenteException;
    public void cancelarVoo(String id) throws ReservaInexistenteException;
    public void addInfo(String partida, String destino, int capacidade) throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException;
    public void encerrarDia() throws DiaJaEncerradoException;
    public void abrirDia() throws DiaJaAbertoException;
    public void reservarVooPorPercurso(List<String> voos, LocalDateTime dataInicio, LocalDateTime dataFim) throws VooInexistenteException, DataInvalidaException, SemReservaDisponivelException;
    public List<Voo> obterListaVoo();
    public List<Voo> obterPercursosPossiveis(String partida, String destino);
}
