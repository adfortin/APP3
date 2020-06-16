package Couche;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Transport {

	
	public Transport() 
	{
		
	}
	
	
	public void sendRequest(byte[] text,String ipServer) 
	{
		 try {
			Trame trame1 = new Trame();
			trame1.setCRC("1000000001".getBytes());
			trame1.setPacketNumber("00000000".getBytes());
			trame1.setPacketAmount("11111111".getBytes());

			DatagramSocket socket = new DatagramSocket();
			
			byte[] buf = trame1.getTrame();
			InetAddress address = InetAddress.getByName(ipServer);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25001);
			socket.send(packet);
			
			
			// get response
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			
			
			// display response
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("Quote of the Moment: " + received);

			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendFirstRequest() 
	{
		
	}
	
	
	
	
    /*

    // get a datagram socket1
DatagramSocket socket = new DatagramSocket();

    // send request
byte[] buf = new byte[256];
InetAddress address = InetAddress.getByName(args[0]);
DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25001);
socket.send(packet);

    // get response
packet = new DatagramPacket(buf, buf.length);
socket.receive(packet);

// display response
String received = new String(packet.getData(), 0, packet.getLength());
System.out.println("Quote of the Moment: " + received);

socket.close();

*/
}
