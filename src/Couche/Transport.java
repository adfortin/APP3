package Couche;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import AuxClass.Trame;

/**
 * 
 * Couche transport permettant de transporter les données du client au serveur
 *
 */
public class Transport {
	/**
	 * Erreur voulue afin de tester les cas non-fonctionnels
	 * 0 - Pas d'erreur,  1 - Erreur de CRC,  2 - Erreur de paquet manquant
	 */
	private int wantedError;
	/**
	 * Variable qui compte le nombre d'erreur de transmission au serveur
	 */
	private int errorCpt;
	/**
	 * Nombre entier aléatoire afin de tester un paquet manquant
	 */
	private int randonIndex;
	/**
	 * Nombre de packet total dans le paquet
	 */
	private int numberOfPacket;
	/**
	 * Longeur du texte entré en bytes
	 */
	private int contentLength;
	/**
	 * Longuer totale des données afin de ne pas dépasser 200 bytes par paquet
	 */
	private int maxDataLength = 169;
	/**
	 * Booléen permettant de savoir si une erreur a déjà été créé pour un paquet manquant
	 */
	private boolean alreadyCreated;
	/**
	 * Contenu du paquet à gérer+
	 */
	private byte[] content;
	/**
	 * Nom du fichier dont le client envoie
	 */
	private byte[] fileName;
	/**
	 * Liste des paquets à envoyer au total
	 */
	private List<Trame> packets = new ArrayList<>();
	/**
	 * Données de chaque paquet séparées en liste 
	 */
	private List<byte[]> byteList = new ArrayList<>();
	/**
	 * Liaison entre le serveur et le client
	 */
	private Liaison liaison;

	/**
	 * Constructeur de la couche Transport
	 */
	public Transport() {
		liaison = new Liaison();
	}

	/**
	 * Méthode permettant d'envoyer le fichier au serveur à l'addresse ipServer
	 * @param text Contenu du fichier à envoyer
	 * @param ipServer Adresse IP du serveur où l'on veut envoyer
	 * @param wantedErr Variable décrivant l'erreur que l'on veut tester où 
	 * 0 - Pas d'erreur,  1 - Erreur de CRC,  2 - Erreur de paquet manquant
	 */
	public void sendRequest(byte[] text, String ipServer, String wantedErr) {
		contentLength = text.length;
		content = text;
		wantedError = Integer.parseInt(wantedErr);

		if (contentLength > maxDataLength) {
			numberOfPacket = (int) Math.ceil(contentLength / maxDataLength);
		}
		SplitContentIntoArray();

		randonIndex = getRandomIndex();

		sendFirstRequest(ipServer);
		sendRemainingPackets(ipServer);
	}

	/**
	 * Méthode permettant d'envoyer le premier paquet avec le nom du fichier
	 * @param ipServer Adresse IP où l'on veut envoyer le paquet
	 */
	public void sendFirstRequest(String ipServer) {
		try {
			Trame trame1 = new Trame();

			trame1.setData(fileName);
			trame1.setPacketNumber(1);
			trame1.setPacketAmount(numberOfPacket + 2);
			trame1.setCRC(liaison.calculCRC(trame1.getTrameTrimmed()));

			DatagramSocket socket = new DatagramSocket();

			int codeError = 5;
			byte[] buf = trame1.getTrame();
			byte[] bufResponse = new Trame(new byte[20]).getTrame();
			InetAddress address = InetAddress.getByName(ipServer);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25002);

			while (codeError != 0) {
				socket.send(packet);

				// get response
				DatagramPacket packet1 = new DatagramPacket(bufResponse, bufResponse.length);
				socket.receive(packet1);

				// display response
				String received = new String(packet1.getData(), 0, packet1.getLength());
				codeError = Integer.parseInt(received.substring(0, 1));
			}
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant d'obtenir le nom du fichier à envoyer
	 * @param path Path où le fichier est localisé dans le client
	 */
	public void GetFileName(Path path) {
		fileName = path.getFileName().toString().getBytes();
	}

	/**
	 * Méthode qui envoie les paquets restants avec les données du fichier à envoyer au serveur
	 * @param ipServer Adresse IP où l'on veut envoyer les données
	 */
	private void sendRemainingPackets(String ipServer) {
		int packetNumber = 2;
		Trame trame;
		int i = 0;
		for (i = 0; i < byteList.size(); i++) {
			try {
				// Missing packet error creator.
				if (wantedError == 2 && packetNumber == randonIndex && !alreadyCreated) {
					trame = new Trame();
					trame.setPacketNumber(packetNumber + 1);
					trame.setPacketAmount(numberOfPacket + 2);
					trame.setData(byteList.get(i + 1));
					alreadyCreated = true;
				} else {
					trame = new Trame();
					trame.setPacketNumber(packetNumber);
					trame.setPacketAmount(numberOfPacket + 2);
					trame.setData(byteList.get(i));
				}

				// CRC error creator.
				if (wantedError == 1 && packetNumber == randonIndex && errorCpt == 0) {
					trame.setCRC("0000000000".getBytes());
				} else {
					trame.setCRC(liaison.calculCRC(trame.getTrameTrimmed()));
				}

				packets.add(trame);

				// Send packet
				DatagramSocket socket;
				socket = new DatagramSocket();

				byte[] buf = trame.getTrame();
				byte[] bufResponse = new Trame(new byte[20]).getTrame();

				InetAddress address = InetAddress.getByName(ipServer);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25002);
				socket.send(packet);

				// get response
				packet = new DatagramPacket(bufResponse, bufResponse.length);
				socket.receive(packet);

				// display response
				String received = new String(packet.getData());
				int codeError = Integer.parseInt(received.substring(0, 1));

				// Loop killer
				if (codeError != 0 && errorCpt == 0) {
					errorCpt++;
					i--;
					packetNumber--;
				}

				packetNumber++;
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Méthode permettant de séparer le contenu à envoyer en plusieurs bytes qui ne dépasse pas la limite
	 * déterminée par la variable maxDataLength
	 */
	private void SplitContentIntoArray() {
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

	/**
	 * Méthode permettant d'obtenir un index aléatoire des paquets envoyés pour des raisons de test
	 * @return Nombre entier entre 0 et le nombre total de paquets + 1
	 */
	private int getRandomIndex() {
		return ThreadLocalRandom.current().nextInt(1, numberOfPacket + 1);
	}

}
