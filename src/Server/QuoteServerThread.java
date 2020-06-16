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
    Trame trame;
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
                
                trame = liaison.getTrame(new String(packet.getData()).trim());
                if (liaison.validateTrameCRC(trame))
                {
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
                        System.out.println("Manque packet no: " + (trame.getPacketNumberInt() /*- 1 (� enlever ) */));
                        break;
                    default:
                        System.out.println("CASE DEFAULT");
                        break;
                    }
                    receivedPackets.add(trame);
                }
                else 
                {
                	System.out.println("CRC du paquet no: " + new String(trame.getPacketNumber()) + " non valide!!!!");
                	
            		while (AskPaquetToClient(trame.getPacketNumber()) != true)
            		{
            			errorCount++;
            			System.out.println("    Asking for packet no: " + new String(trame.getPacketNumber()) + " (" +  errorCount +"/3)");
                    	if (errorCount >= 3) 
                    	{
                    		System.out.print("TransmissionErrorExcpetion");
                    		errorCount = 0;
                    		return;
                    	}
            			
            		}
                }
                //System.out.println(new String(trame.getTrameTrimmed()));
                //System.out.println(trame.getTrameTrimmed().length);
                
                
                buf = packet.getData();
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
						System.out.println("Manque packet no: " + (trame.getPacketNumberInt() /*- 1 (� enlever ) */));
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