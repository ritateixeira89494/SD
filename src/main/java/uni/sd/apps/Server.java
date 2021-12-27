package uni.sd.apps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import uni.sd.net.TaggedConnection;
import uni.sd.net.TaggedConnection.Frame;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        ServerSocket ss = new ServerSocket(port);
        try {
            while(true) {
                Socket s = ss.accept();
                TaggedConnection c = new TaggedConnection(s);

                Runnable r = () -> {
                    try {
                        boolean on = true;
                        while(on) {
                            Frame f = c.receive();
                            String msg = new String(f.data);
                            if(msg.equals("FYN")) {
                                System.out.println(msg + " received. Closing connection...");
                                on = false;
                                c.close();
                            } else {
                                System.out.println("Tag: " + f.tag + " Data: " + new String(f.data));
                            }
                        }
                    } catch (Exception e) {}
                };
                Thread t = new Thread(r);
                t.start();
            }
        } finally {
            ss.close();
        }
    }
}
