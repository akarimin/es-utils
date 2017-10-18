package ir.hafiz.esutils.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by akarimin on 10/18/17.
 */
public class TelnetClient {

    public static void telnet(String host, int port) throws IOException {
        Socket pingSocket;
        PrintWriter out;
        BufferedReader in;

        try {
            pingSocket = new Socket(host, port);
            out = new PrintWriter(pingSocket.getOutputStream(), false);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
        } catch (IOException e) {
            return;
        }

//        System.out.println("########################################################################################\n" +
//                "pinging .... \n");
        out.println("ping");
        System.out.println(in.readLine());
        out.close();
        in.close();
        pingSocket.close();
    }
}
