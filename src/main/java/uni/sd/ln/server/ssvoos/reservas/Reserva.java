package uni.sd.ln.server.ssvoos.reservas;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private final String emailUtilizador;
    private final String partida;
    private final String destino;
    private final LocalDateTime dataVoo;
    private final LocalDateTime dataReserva;

    public Reserva(String emailUtilizador, String partida, String destino, LocalDateTime dataVoo, LocalDateTime dataReserva) {
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

    public LocalDateTime getDataVoo() {
        return this.dataVoo;
    }

    public LocalDateTime getDataReserva() {
        return this.dataReserva;
    }
}
