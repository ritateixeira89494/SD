package uni.sd.net;

import java.util.List;

public class Frame {
        private final String tipo;
        private final List<String> dados;

        public Frame(String tipo, List<String> dados) {
            this.tipo = tipo;
            this.dados = dados;
    }

    public String getTipo() {
            return this.tipo;
    }

    public List<String> getDados() {
        return this.dados;
    }
}
