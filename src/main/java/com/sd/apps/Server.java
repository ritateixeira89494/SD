package com.sd.apps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        
        ServerSocket ss = new ServerSocket(port);

        while(true) {
            Socket s = ss.accept();
        }
    }
}
