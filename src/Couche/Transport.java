package Couche;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transport {
	private double numberOfPacket;
    private int contentLength;
    byte[] content;
    private List<Trame> packets = new ArrayList<>();
    List<byte[]> byteList = new ArrayList<>();
    private int maxDataLength = 188;
	
	public Transport() 
	{
		
	}
	
	
	public void sendRequest(byte[] text,String ipServer) 
	{
		
		//Send file name to server
		sendFirstRequest(text, ipServer);
		
		
		//SplitPacketArray
        contentLength = text.length;

        if (contentLength > maxDataLength) {
            numberOfPacket = Math.ceil(contentLength / maxDataLength);
            System.out.println(numberOfPacket);
        }
        
        SplitContentIntoArray();
		
        
        
        //Send Packet 1....
        
        
        
	}
	
	public void sendFirstRequest(byte[] text,String ipServer) 
	{
		try {
			
			Trame trame1 = new Trame();
			trame1.setCRC("1000000001".getBytes());
			trame1.setPacketNumber("00000000".getBytes());
			trame1.setPacketAmount("11111111".getBytes());

			DatagramSocket socket;
			socket = new DatagramSocket();
			
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
	
	
	private void SplitContentIntoArray() {
        for (int i = 0; i < contentLength; i += maxDataLength ) {
            byte[] cuttedByte = Arrays.copyOfRange(content , i, i + maxDataLength);
            byteList.add(cuttedByte);
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
