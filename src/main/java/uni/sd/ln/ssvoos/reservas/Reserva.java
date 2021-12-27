package uni.sd.ln.ssvoos.reservas;

import java.time.LocalDateTime;

public class Reserva {
    private String id;
    private LocalDateTime data;

    public Reserva(int id, LocalDateTime data) {
        this.id = id + "";
        this.data = data;
    }

    public String getId() {
        return this.id;
    }

    public LocalDateTime getData() {
        return this.data;
    }
}
