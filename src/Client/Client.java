package Client;

import java.io.*;
import Couches.Application;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
             System.out.println("Usage: java QuoteClient <hostname>");
             return;
        }

            // get a datagram socket1
        DatagramSocket socket = new DatagramSocket();

            // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

	    // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
        testTexte(args);
        
        socket.close();
    }
    
    public static void testTexte(String[] args) throws Exception{
    	Application readtext = new Application();
    	readtext.readTxt(args[1]);
    }
    
    public static void terminate(String[] args) throws Exception {
    	Application terminate = new Application();
    	terminate.terminate(Boolean.parseBoolean(args[2]));
    }
}