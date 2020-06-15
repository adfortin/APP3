package Client;

import java.io.IOException;

import Couche.Application;

public class Client {
	public static void main(String[] args) throws IOException 
	{
		if (args.length != 2) {
			System.out.println("Hostname or Filepath missing");
			return;
		}
      
		Application app = new Application(args[0],args[1]);
		app.Run();
		
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
}
