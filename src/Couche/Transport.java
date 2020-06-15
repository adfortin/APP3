package Couche;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Transport {

	
	public Transport() 
	{
		
	}
	
	
	public void sendRequest(byte[] text,String ipServer) 
	{
		 try {
			DatagramSocket socket = new DatagramSocket();
			

			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
