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
	private boolean trameIsGood = false;
	List<Trame> receivedPackets = new ArrayList<>();
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
				byte[] receivedData = new byte[200];
				// receive request
				DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
				socket.receive(packet);

				trame = liaison.getTrame(new String(packet.getData()).trim());

				// Validate Trame's CRC
				trameIsGood = liaison.validateTrame(trame);

				if (trameIsGood) {
					// Look for missing packet
					int checkResponse;
					
					//Create error
					if (trame.getPacketNumberInt() == 6) {
						checkResponse = liaison.checkForSkipedPacket(receivedPackets, trame.getPacketNumberInt() + 1);
					} else {
						checkResponse = liaison.checkForSkipedPacket(receivedPackets, trame.getPacketNumberInt());
					}

					switch (checkResponse) {
					case 0:
						System.out.println("Recu packet no: " + trame.getPacketNumberInt());
						break;
					case 1:
						System.out.println("Manque packet no: " + (trame.getPacketNumberInt() /*- 1 (à enlever ) */));
						break;
					default:
						System.out.println("CASE DEFAULT");
						break;
					}
					receivedPackets.add(trame);

					
					
					
				}

				receivedData = packet.getData();

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