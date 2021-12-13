package com.sd.ssvoos.voos;

public class Voo {
    /**
     * ID único do voo.
     * Este valor é construído da seguinte forma: id = partida + ":" + destino
     */
    private String id;
    
    private String partida;
    private String destino;
    private int capacidade;

    public Voo(String partida, String destino, int capacidade) {
        id = partida + ":" + destino;
        this.partida = partida;
        this.destino = destino;
        this.capacidade = capacidade;
    }

    public Voo(Voo v) {
        this.id = v.id;
        this.partida = v.partida;
        this.destino = v.destino;
        this.capacidade = v.capacidade;
    }

    public String getId() {
        return this.id;
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
