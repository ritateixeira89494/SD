package uni.sd.net;

import java.util.List;

public class Frame {
        private final int tag;
        private final int tipo;
        private final List<byte[]> dados;

        public Frame(int tag, int tipo, List<byte[]> dados) {
            this.tag = tag;
            this.tipo = tipo;
            this.dados = dados;
    }

    public int getTag() {
        return this.tag;
    }

    public int getTipo() {
            return this.tipo;
    }

    public List<byte[]> getDados() {
        return this.dados;
    }
}
