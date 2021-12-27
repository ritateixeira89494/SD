package uni.sd.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Lock wLock = new ReentrantLock();
    private Lock rLock = new ReentrantLock();

    public static class Frame {
        public int tag;
        public final byte[] data;
        public Frame(int tag,byte[] data) {
            this.tag = tag;
            this.data = data;
        }
    }
    
    public TaggedConnection(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void send(Frame f) throws IOException {
        wLock.lock();
        try {
            dos.writeInt(f.tag);
            dos.writeInt(f.data.length);
            dos.write(f.data);
            dos.flush();
        } finally {
            wLock.unlock();
        }
    }

    public Frame receive() throws IOException {
        rLock.lock();
        try {
            int tag = dis.readInt();
            int length = dis.readInt();
            byte[] data = new byte[length];
            dis.readFully(data);
            return new Frame(tag, data);
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
