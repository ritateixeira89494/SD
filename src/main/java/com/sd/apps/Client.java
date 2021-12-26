package com.sd.apps;

import java.net.Socket;
import java.util.Scanner;

import com.sd.net.TaggedConnection;
import com.sd.net.TaggedConnection.Frame;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", Integer.parseInt(args[0]));
        TaggedConnection c = new TaggedConnection(socket);

        Scanner scan = new Scanner(System.in);
        boolean on = true;
        while(on) {
            String msg = scan.nextLine();

            Frame f = new Frame(0,msg.getBytes());
            c.send(f);
            if(msg.equals("FYN")) {
                System.out.println(msg + " sent. Closing connection");
                on = false;
            }
        }
        c.close();
        scan.close();
    }
}
