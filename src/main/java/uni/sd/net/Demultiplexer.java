package uni.sd.net;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demultiplexer implements AutoCloseable {
    TaggedConnection conn;
    Thread t;
    Map<Integer,Entry> frames = new HashMap<>();
    Lock wLock = new ReentrantLock();
    Condition c = wLock.newCondition();
    boolean stop = false; 

    private class Entry {
        final Lock l = new ReentrantLock();
        final Condition cond = l.newCondition() ;
        final ArrayDeque<byte[]> queue = new ArrayDeque<>();
    }

    public Demultiplexer(TaggedConnection conn) {
        this.conn = conn;
        t = new Thread(() -> {
                try  {
                    while (!stop) {
                        Frame f = conn.receive();
                        wLock.lock();
                        Entry e = get(f.getTag());
                        e.l.lock();
                        wLock.unlock();
                        //e.queue.add(f.getDados());
                        frames.put(f.getTag(), e);
                        e.cond.signalAll();
                        e.l.unlock();
                    }
                } catch (Exception ignored) {ignored.printStackTrace();}
            });
    }

    private Entry get(int tag) {
        Entry e = frames.get(tag);

        if(e == null) {
            e = new Entry();
        }
        return e;
    }

    public void start() {
        t.start();
    }

    public void send(Frame f) throws IOException {
        wLock.lock();
        try {
            conn.send(f);
        } finally {
            wLock.unlock();
        }
    }

    public void send(int tag, int tipo, List<byte[]> dados) throws IOException {
        send(new Frame(tag, tipo, dados));
    }

    public byte[] receive(int tag) throws InterruptedException {
        Entry e = frames.get(tag);
        try{
            e.l.lock();
            while(e.queue.isEmpty()) {
                frames.get(tag).cond.await();
            }

            return frames.get(tag).queue.pop();
        } finally {
            e.l.unlock();
        }
    }

    public void close() throws IOException, InterruptedException {
        stop = true;
    }
}
