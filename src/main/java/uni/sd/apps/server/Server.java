package uni.sd.apps.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import uni.sd.data.DadosDAO;
import uni.sd.ln.server.Iln;
import uni.sd.ln.server.LN;
import uni.sd.net.Connection;

public class Server {
    public static void main(String[] args) throws IOException, SQLException {
        Iln ln = new LN(new DadosDAO());
        int port = 12345;
        ServerSocket ss = new ServerSocket(port);
        try {
            while(true) {
                Socket s = ss.accept();
                Connection c = new Connection(s);

                Thread t = new Thread(new Worker(c, ln));
                t.start();
            }
        } finally {
            ss.close();
        }
    }
}
