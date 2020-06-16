package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuoteServerThread extends Thread {

	protected DatagramSocket socket = null;
	protected BufferedReader in = null;
	protected boolean moreQuotes = true;

	public QuoteServerThread() throws IOException {
		this("QuoteServerThread");
	}

	public QuoteServerThread(String name) throws IOException {
		super(name);
		socket = new DatagramSocket(25001);
	}

	public void run() {
		dayTime();
	}
	
	public void dayTime() {

		byte[] buf = new byte[256];

		try {
		
		// receive request
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		// figure out response
		String dString = null;
		dString = new Date().toString();

		buf = dString.getBytes();

		// send the response to the client at "address" and "port"
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
	
	public void sendFileName(String fileName) {
		
		byte[] buf = new byte[256];
		try {
			
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

			socket.receive(packet);
			
			buf = fileName.getBytes();
			
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
}