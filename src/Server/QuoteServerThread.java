package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import AuxClass.Trame;

import Couche.Liaison;

/**
 * 
 * Classe permettant de commencer l'application serveur
 *
 */
public class QuoteServerThread extends Thread {

	/**
	 * Socket serveur
	 */
	private DatagramSocket socket = null;
	/**
	 * Initialisation de la couche de liaison entre le serveur et le client
	 */
	private Liaison liaison = new Liaison();
	/**
	 * Liste des paquets reçus
	 */
	private List<Trame> receivedPackets = new ArrayList<>();
	/**
	 * Trame reçue par le serveur
	 */
	private Trame receivedtrame;
	/**
	 * Trame à envoyer par le serveur
	 */
	private Trame responseTrame;
	/**
	 * Variable permettant d'arrêter le transfert de paquets lorsque l'envoi est terminé
	 */
	private boolean moreQuotes = true;

	/**
	 * Constructeur du serveur avec un nom par défaut
	 * @throws IOException Erreur de construction
	 */
	public QuoteServerThread() throws IOException {
		this("QuoteServerThread");
	}

	/**
	 * Constructeur du serveur par défaut. Associe un port 25002 au socket
	 * @param name Nom du serveur
	 * @throws IOException Erreur de construction
	 */
	public QuoteServerThread(String name) throws IOException {
		super(name);
		socket = new DatagramSocket(25002);

	}

	/**
	 * Méthode permettant d'exécuter le serveur
	 */
	public void run() {

		while (moreQuotes) {
			try {
				byte[] buf = new byte[200];
				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				receivedtrame = liaison.getTrame(new String(packet.getData()));
				liaison.ecrireLog(receivedtrame, 0);

				if (liaison.validateTrameCRC(receivedtrame)) {
					// Look for missing packet
					int checkResponse = liaison.checkForSkipedPacket(receivedPackets,receivedtrame.getPacketNumberInt());

					switch (checkResponse) {
					case 0:
						System.out.println("Recu packet no: " + receivedtrame.getPacketNumberInt());
						receivedPackets.add(receivedtrame);
						responseTrame = new Trame("0SUCCESS".getBytes());
						break;
					case 1:
						System.out.println("Manque packet no: " + (receivedtrame.getPacketNumberInt() - 1));
						System.out.println("Retransmission du paquet manquant en cours...");
						responseTrame = new Trame("1MISSINGPACKET".getBytes());
						break;
					}
				} else {
					System.out.println("CRC du paquet no: " + new String(receivedtrame.getPacketNumber()) + " non valide!");
					System.out.println("Retransmission du paquet non valide en cours...");
					responseTrame = new Trame("2CRCERROR".getBytes());
				}

				buf = responseTrame.getTrame();
				// System.out.println(new String(buf));
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				socket.send(packet);
				liaison.ecrireLog(responseTrame, 1);
				
				if (receivedtrame.getPacketNumberInt() == receivedtrame.getPacketAmountInt()) {
					moreQuotes = false;
				}

			} catch (IOException e) {
				e.printStackTrace();
				moreQuotes = false;
			}
		}
		socket.close();
		SaveFile();
	}

	/**
	 * Méthode permettant de sauvegarder le fichier envoyé par le client
	 */
	private void SaveFile() {
		int numberOfPackets = receivedPackets.get(0).getPacketAmountInt();
		String nameOfFile = getFileName();
		// approximate size of file to create: get size of first real packet * total number
		int sizeInBytes = receivedPackets.get(1).getTrame().length * numberOfPackets;

		System.out.println("Creation du fichier... Nombre de paquets: " + numberOfPackets + "  Grosseur Total en byte: " + sizeInBytes);
		System.out.println(nameOfFile);

		File file = new File(nameOfFile + "Received.txt"); // Create a temporary file to reserve memory.
		long bytes = sizeInBytes; // number of bytes reserved.
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(file, "rw"); // rw stands for open in read/write mode.
			rf.setLength(bytes); // This will cause java to "reserve" memory for your application by inflating/truncating the file to the specific size.

			for (int i = 1; i < numberOfPackets; i++) {
				rf.writeBytes(new String(receivedPackets.get(i).getData()));
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ioex) {
					System.out.println(ioex.toString());
				}
			}
		}
	}

	/**
	 * Méthode getter du nom du fichier reçu
	 * @return FileName
	 */
	private String getFileName() {
		// get file name except for the extension
		String CompleteFileName = new String(receivedPackets.get(0).getData());
		String[] parts = CompleteFileName.split(".");
		String FileName = "";
		for (int i = 0; i < parts.length; i++) {
			FileName += parts[i];
		}
		return FileName;
	}
}