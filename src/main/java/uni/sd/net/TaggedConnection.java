package uni.sd.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
            dos.writeInt(f.getTag());
            dos.writeInt(f.getTipo());
            dos.writeInt(f.getDados().size());
            for(byte[] array: f.getDados()) {
                dos.writeInt(array.length);
                dos.write(array);
            }
            dos.flush();
        } finally {
            wLock.unlock();
        }
    }

    public void send(int tag, int tipo, List<byte[]> dados) throws IOException {
        send(new Frame(tag, tipo, dados));
    }

    public Frame receive() throws IOException {
        rLock.lock();
        try {
            int tag = dis.readInt();
            int tipo = dis.readInt();
            int dadosSize = dis.readInt();

            List<byte[]> dados = new ArrayList<>();
            for(int i = 0; i < dadosSize; i++) {
                int arraySize = dis.readInt();
                byte[] array = new byte[arraySize];
                dis.readFully(array);
                dados.add(array);
            }
            return new Frame(tag, tipo, dados);
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
