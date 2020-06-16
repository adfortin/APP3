package Couche;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
<<<<<<< HEAD
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
=======
>>>>>>> SendManyPackets
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transport {
	private int numberOfPacket;
	private int contentLength;
	byte[] content;
	private List<Trame> packets = new ArrayList<>();
	List<byte[]> byteList = new ArrayList<>();
	private int maxDataLength = 169;
	byte[] fileName;
	Liaison liaison;

	public Transport() {
		liaison = new Liaison();
	}

	public void sendRequest(byte[] text, String ipServer) {
		contentLength = text.length;
		content = text;

		if (contentLength > maxDataLength) {
			numberOfPacket = (int) Math.ceil(contentLength / maxDataLength);
		}

		SplitContentIntoArray();

		sendFirstRequest(ipServer);
		sendRemainingPackets(ipServer);

	}

	public void sendFirstRequest(String ipServer) {
		try {

			Trame trame1 = new Trame();

			trame1.setData(fileName);
			trame1.setPacketNumber(1);
			trame1.setPacketAmount(numberOfPacket + 2);
			trame1.setCRC(liaison.calculCRC(trame1.getTrameTrimmed()));

			trame1.setCRC("1000000001".getBytes());
			trame1.setPacketNumber(0);
			trame1.setPacketAmount(0);
				
			DatagramSocket socket;
			socket = new DatagramSocket();
			
			int codeError = 5;
			String messageError = "";
			byte[] buf = trame1.getTrame();
			InetAddress address = InetAddress.getByName(ipServer);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25001);
			
			while (codeError!= 0) 
			{
				socket.send(packet);

				// get response
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				
				// display response
				String received = new String(packet.getData(), 0, packet.getLength());
				codeError = Integer.parseInt(received.substring(0, 1));
				//System.out.println("Quote of the Moment: " + codeError);
				messageError = received.substring(1);
			}
			

			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void SplitContentIntoArray() {
		
		 /*for (int i = 0; i < contentLength; i += maxDataLength ) { 
			 byte[] cuttedByte = Arrays.copyOfRange(content , i, i + maxDataLength); 
			 byteList.add(cuttedByte);
		 }*/
		 

		byte[] cuttedByte = new byte[maxDataLength];
		for (int i = 0; i < contentLength; i += maxDataLength) {
			if (i + maxDataLength < contentLength) {
				cuttedByte = Arrays.copyOfRange(content, i, i + maxDataLength);
				byteList.add(cuttedByte);
			} else {
				cuttedByte = Arrays.copyOfRange(content, i, contentLength);
				byteList.add(cuttedByte);
			}
		}
	}

	public void GetFileName(Path path) {
		fileName = path.getFileName().toString().getBytes();
	}

	private void sendRemainingPackets(String ipServer) {
		int packetNumber = 2;
		Trame trame;
		int i = 0;
		for (i = 0; i < byteList.size(); i++) {
			try {
				trame = new Trame();
				trame.setPacketNumber(packetNumber);
				trame.setPacketAmount(numberOfPacket + 2);
				trame.setData(byteList.get(i));
				trame.setCRC(liaison.calculCRC(trame.getTrameTrimmed()));
				packets.add(trame);

				DatagramSocket socket;
				socket = new DatagramSocket();

				byte[] buf = trame.getTrame();
				InetAddress address = InetAddress.getByName(ipServer);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25001);
				socket.send(packet);

				// get response
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				// display response
				String received = new String(packet.getData(), 0, packet.getLength());

				int codeError = Integer.parseInt(received.substring(0, 1));
				if (codeError != 0)
				{
					i--;
					packetNumber--;
				}

				String messageError = received.substring(1);


				socket.close();

				packetNumber++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
