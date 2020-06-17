package AuxClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * Classe auxiliaire permettant de catégoriser les paquets à envoyer et à recevoir
 * afin de séparer les différentes informations des paquets
 *
 */
public class Trame {

	/**
	 * Header du paquet
	 */
	private byte[] header;
	/**
	 * Numéro du paquet du message total à envoyer
	 */
	private byte[] packetNumber;
	/**
	 * Numéro total des paquets envoyés qui consiste ensemble le message
	 */
	private byte[] packetAmount;
	/**
	 * Données du paquet
	 */
	private byte[] data;
	/**
	 * Données du paquet sous forme encryptée afin de vérifier le statut de l'envoi.
	 * Les mêmes données donne le même CRC
	 */
	private byte[] CRC;
	/**
	 * Numéro du paquet du message total à envoyer sous forme d'entier
	 */
	private int packetNumberInt;
	/**
	 * Numéro total des paquets envoyés qui consiste ensemble le message sous forme d'entier
	 */
	private int packetAmountInt;

	/**
	 * Constructeur de trame par défaut sans données
	 */
	public Trame() {
		setHeader("BEGIN".getBytes());
		setPacketNumber(0);
		setPacketAmount(0);
		setData(new byte[187]);
		setCRC(new byte[5]);
		setPacketNumberInt(0);
		setPacketAmountInt(0);
	}

	/**
	 * Constructeur de trame avec données en paramètre
	 * @param data Données à insérer dans la trame
	 */
	public Trame(byte[] data) {
		setHeader(new byte[0]);
		setPacketNumber(new byte[0]);
		setPacketAmount(new byte[0]);
		setData(data);
		setCRC(new byte[0]);
		setPacketNumberInt(0);
		setPacketAmountInt(0);
	}

	/**
	 * Méthode getter des données de la trame
	 * @return data Données de la trame
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Méthode setter des données de la trame
	 * @param data Données à insérer dans la trame
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Méthode getter du CRC
	 * @return CRC Données encryptées
	 */
	public byte[] getCRC() {
		return CRC;
	}

	/**
	 * Méthode setter du CRC
	 * @param cRC Données encryptées à insérer dans la trame
	 */
	public void setCRC(byte[] cRC) {
		CRC = cRC;
	}

	/**
	 * Méthode getter du numéro de paquet
	 * @return Numéro du paquet
	 */
	public byte[] getPacketNumber() {
		return packetNumber;
	}

	/**
	 * Méthode setter du numéro de paquet
	 * @param packetNumber Entier
	 */
	public void setPacketNumber(int packetNumber) {
		this.packetNumber = createByteFromInt(packetNumber);
	}

	/**
	 * Méthode setter du numéro de paquet
	 * @param packetNumber Bytes
	 */
	public void setPacketNumber(byte[] packetNumber) {
		this.packetNumber = packetNumber;
	}

	/**
	 * Méthode getter du nombre total de paquet
	 * @return Nombre de paquet en bytes
	 */
	public byte[] getPacketAmount() {
		return packetAmount;
	}

	/**
	 * Méthode setter du nombre total de paquets
	 * @param packetAmount Nombre de paquets total en entier
	 */
	public void setPacketAmount(int packetAmount) {

		this.packetAmount = createByteFromInt(packetAmount);
	}

	/**
	 * Méthode setter du nombre total de paquets
	 * @param packetAmount Nombre de paquets total en bytes
	 */
	public void setPacketAmount(byte[] packetAmount) {

		this.packetAmount = packetAmount;
	}

	/**
	 * Méthode getter du numéro de paquet en entier
	 * @return Nombre du paquet en entier 
	 */
	public int getPacketNumberInt() {
		return packetNumberInt;
	}

	/**
	 * Méthode setter du numéro de paquet en entier
	 * @param packetNumberInt Numéro du paquet en entier
	 */
	public void setPacketNumberInt(int packetNumberInt) {
		this.packetNumberInt = packetNumberInt;
	}

	/**
	 * Méthode getter du nombre total de paquet en entier
	 * @return Nombre de paquet total en entier
	 */
	public int getPacketAmountInt() {
		return packetAmountInt;
	}

	/**
	 * Méthode setter du nombre de paquet total en entier
	 * @param packetAmountInt Nombre de paquet total en entier
	 */
	public void setPacketAmountInt(int packetAmountInt) {
		this.packetAmountInt = packetAmountInt;
	}

	/**
	 * Méthode permettant de transférer un nombre entier en bytes
	 * @param packets Nombre entier à convertir
	 * @return Nombre converti en bytes
	 */
	public byte[] createByteFromInt(int packets) {
		String packetString = String.valueOf(packets);

		while (packetString.length() < 8) {
			packetString = "0" + packetString;
		}

		return packetString.getBytes();
	}

	/**
	 * Méthode getter de la trame en bytes
	 * @return Trame actuelle en format byte[]
	 */
	public byte[] getTrame() {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(CRC);
			outputStream.write(header);
			outputStream.write(packetNumber);
			outputStream.write(packetAmount);
			outputStream.write(data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	/**
	 * Méthode getter de la trame en bytes sans le CRC
	 * @return Trame actualle en format byte[] sans le CRC
	 */
	public byte[] getTrameTrimmed() {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(header);
			outputStream.write(packetNumber);
			outputStream.write(packetAmount);
			outputStream.write(data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	/**
	 * Méthode getter du header
	 * @return header
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 * Méthode setter du header
	 * @param header
	 */
	public void setHeader(byte[] header) {
		this.header = header;
	}

}
