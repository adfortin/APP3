package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

import Couche.Liaison;
import Couche.Trame;

public class QuoteServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    Liaison liaison = new Liaison();
    List<Trame> receivedPackets = new ArrayList<>();
    Trame receivedtrame;
    Trame responseTrame;
    int errorCount = 0;

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
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                              
                receivedtrame = liaison.getTrame(new String(packet.getData()).trim());
                
                if (liaison.validateTrameCRC(receivedtrame))
                {
                	 // Look for missing packet
                    int checkResponse;

                    //Create error
                    if (receivedtrame.getPacketNumberInt() == 60) {
                        checkResponse = liaison.checkForSkipedPacket(receivedPackets, receivedtrame.getPacketNumberInt() + 1);
                    } else {
                        checkResponse = liaison.checkForSkipedPacket(receivedPackets, receivedtrame.getPacketNumberInt());
                    }

                    switch (checkResponse) {
                    case 0:
                        System.out.println("Recu packet no: " + receivedtrame.getPacketNumberInt());
                        responseTrame = new Trame("0SUCCESS".getBytes());
                        break;
                    case 1:
                        System.out.println("Manque packet no: " + (receivedtrame.getPacketNumberInt() /*- 1 (à enlever ) */));
                        responseTrame = new Trame("1MISSINGPACKET".getBytes());
                        break;
                    default:
                        break;
                    }
                    receivedPackets.add(receivedtrame);
                }
                else 
                {
                	System.out.println("CRC du paquet no: " + new String(receivedtrame.getPacketNumber()) + " non valide!!!!");
                	responseTrame = new Trame("2CRCERROR".getBytes());
                }
                //System.out.println(new String(trame.getTrameTrimmed()));
                //System.out.println(trame.getTrameTrimmed().length);
                buf = responseTrame.getTrame();
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                

            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }
    
    public boolean AskPaquetToClient(byte[] paquetNumber)
    {
        /*try {
        	
            byte[] buf = new byte[200];
            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            
            packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	liaison.AskForPaquet(paquetNumber);*/
    	
    	return false;
    }

}