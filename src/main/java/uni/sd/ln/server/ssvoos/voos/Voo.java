package uni.sd.ln.server.ssvoos.voos;

public class Voo {
    private final String partida;
    private final String destino;
    private int capacidade;
    private int duracao;

    public Voo(String partida, String destino, int capacidade, int duracao) {
        this.partida = partida;
        this.destino = destino;
        this.capacidade = capacidade;
        this.duracao = duracao;
    }

    public Voo(Voo v) {
        this.partida = v.partida;
        this.destino = v.destino;
        this.capacidade = v.capacidade;
        this.duracao = v.duracao;
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

    public int getDuracao() {
        return this.duracao;
    }
}
