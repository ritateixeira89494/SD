package uni.sd.ln.server.ssvoos.voos;

public class Voo {
    private final String partida;
    private final String destino;
    private int capacidade;

    public Voo(String partida, String destino, int capacidade) {
        this.partida = partida;
        this.destino = destino;
        this.capacidade = capacidade;
    }

    public Voo(Voo v) {
        this.partida = v.partida;
        this.destino = v.destino;
        this.capacidade = v.capacidade;
    }

    public String getPartida() {
        return this.partida;
    }

    public String getDestino() {
        return this.destino;
    }

    public int getCapacidade() {
        return this.capacidade;
    }
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

}
