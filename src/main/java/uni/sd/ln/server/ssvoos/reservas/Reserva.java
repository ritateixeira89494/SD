package uni.sd.ln.server.ssvoos.reservas;

import java.time.LocalDate;

public class Reserva {
    private final String emailUtilizador;
    private final String partida;
    private final String destino;
    private final LocalDate dataVoo;
    private final LocalDate dataReserva;

    public Reserva(String emailUtilizador, String partida, String destino, LocalDate dataVoo, LocalDate dataReserva) {
        this.emailUtilizador = emailUtilizador;
        this.partida = partida;
        this.destino = destino;
        this.dataVoo = dataVoo;
        this.dataReserva = dataReserva;
    }

    public String getEmailUtilizador() {
        return this.emailUtilizador;
    }

    public String getPartida() {
        return this.partida;
    }

    public String getDestino() {
        return this.destino;
    }

    public LocalDate getDataVoo() {
        return this.dataVoo;
    }

    public LocalDate getDataReserva() {
        return this.dataReserva;
    }
}
