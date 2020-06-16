package Couches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.zip.CRC32;

public class Transport {

	private double numberOfPacket;
	private int contentLength;
	byte[] content;
	private List<Trame> packets = new ArrayList<>();
	List<byte[]> byteList = new ArrayList<>();
	private int maxDataLength = 188;
	
	public Transport() {
		
	}
	
	
	public void sendRequest(byte[] textdata, String hostname) {
		
		content = textdata;
		contentLength = content.length;
		
		if (contentLength > maxDataLength) {
			numberOfPacket = Math.ceil(contentLength / maxDataLength);
			System.out.println(numberOfPacket);
		}
		
		for (int i = 0; i < contentLength; i++) {
			 
		}
		
		SplitContentIntoArray();
		
		
		for (byte[] bytes : byteList) {
			System.out.println(new String(bytes));
			System.out.println(" ");
		}
		
		/*CRC32 fileCRC32 = new CRC32();
        fileCRC32.update(content);
        System.out.println(String.format(Locale.US,"%08X", fileCRC32.getValue()));*/
		
        /*
            // get a datagram socket1
        DatagramSocket socket = new DatagramSocket();

            // send request
         
        
        byte[] buf = new byte[200];
        InetAddress address = InetAddress.getByName(args[0]);
        
        
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

	    // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
    
        socket.close(); */
	}
	
	private void SplitContentIntoArray() {
		for (int i = 0; i < contentLength; i += maxDataLength ) {
			byte[] cuttedByte = Arrays.copyOfRange(content , i, i + maxDataLength);
			byteList.add(cuttedByte);
		}
	}
}








