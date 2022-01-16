package uni.sd.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection implements AutoCloseable {
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public Connection(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void send(Frame f) throws IOException {
        dos.writeUTF(f.getTipo());
        dos.writeInt(f.getDados().size());
        for(String s: f.getDados()) {
            dos.writeUTF(s);
        }
        dos.flush();
    }

    public void send(String tipo, List<String> dados) throws IOException {
        send(new Frame(tipo, dados));
    }

    public Frame receive() throws IOException {
        String tipo = dis.readUTF();

        int dadosSize = dis.readInt();
        List<String> dados = new ArrayList<>();
        for(int i = 0; i < dadosSize; i++) {
            String s = dis.readUTF();
            dados.add(s);
        }
        return new Frame(tipo, dados);
    }

    @Override
    public void close() throws Exception {
        dis.close();
        dos.close();
    }
}
