package uni.sd.apps.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import uni.sd.ln.server.Iln;
import uni.sd.ln.server.LN;
import uni.sd.net.TaggedConnection;

public class Server {
    Iln ln;
    public static void main(String[] args) throws IOException, SQLException {
        Iln ln = new LN();
        int port = Integer.parseInt(args[0]);
        ServerSocket ss = new ServerSocket(port);
        try {
            while(true) {
                Socket s = ss.accept();
                TaggedConnection c = new TaggedConnection(s);

                Thread t = new Thread(new Worker(c, ln));
                t.start();
            }
        } finally {
            ss.close();
        }
    }
}
