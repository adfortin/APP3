package Server;
import java.io.*;
import java.net.*;
import java.util.*;

import Couche.Liaison;
import Couche.Trame;

public class QuoteServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    Liaison liaison = new Liaison();
    Trame trame;

    public QuoteServerThread() throws IOException {
    this("QuoteServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(25001);

    }

    public void run() {

        while (moreQuotes) {
            try {
                byte[] buf = new byte[200];
                // receive request
                DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
                socket.receive(packet);
                
                trame = liaison.getTrame(new String(packet.getData()).trim());
                
                buf = packet.getData();

		// send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(receivedData, receivedData.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }

}