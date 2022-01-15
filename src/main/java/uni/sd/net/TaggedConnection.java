package uni.sd.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final Lock wLock = new ReentrantLock();
    private final Lock rLock = new ReentrantLock();


    public TaggedConnection(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void send(Frame f) throws IOException {
        wLock.lock();
        try {
            dos.writeUTF(f.getTipo());
            dos.writeInt(f.getDados().size());
            for(String s: f.getDados()) {
                dos.writeUTF(s);
            }
            dos.flush();
        } finally {
            wLock.unlock();
        }
    }

    public void send(String tipo, List<String> dados) throws IOException {
        send(new Frame(tipo, dados));
    }

    public Frame receive() throws IOException {
        rLock.lock();
        try {
            String tipo = dis.readUTF();

            int dadosSize = dis.readInt();
            List<String> dados = new ArrayList<>();
            for(int i = 0; i < dadosSize; i++) {
                String s = dis.readUTF();
                dados.add(s);
            }
            return new Frame(tipo, dados);
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public void close() throws Exception {
        dis.close();
        dos.close();
    }
}
