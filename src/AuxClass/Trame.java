package AuxClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * Classe auxiliaire permettant de cat�goriser les paquets � envoyer et � recevoir
 * afin de s�parer les diff�rentes informations des paquets
 *
 */
public class Trame {

	/**
	 * Header du paquet
	 */
	private byte[] header;
	/**
	 * Num�ro du paquet du message total � envoyer
	 */
	private byte[] packetNumber;
	/**
	 * Num�ro total des paquets envoy�s qui consiste ensemble le message
	 */
	private byte[] packetAmount;
	/**
	 * Donn�es du paquet
	 */
	private byte[] data;
	/**
	 * Donn�es du paquet sous forme encrypt�e afin de v�rifier le statut de l'envoi.
	 * Les m�mes donn�es donne le m�me CRC
	 */
	private byte[] CRC;
	/**
	 * Num�ro du paquet du message total � envoyer sous forme d'entier
	 */
	private int packetNumberInt;
	/**
	 * Num�ro total des paquets envoy�s qui consiste ensemble le message sous forme d'entier
	 */
	private int packetAmountInt;

	/**
	 * Constructeur de trame par d�faut sans donn�es
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
	 * Constructeur de trame avec donn�es en param�tre
	 * @param data Donn�es � ins�rer dans la trame
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
	 * M�thode getter des donn�es de la trame
	 * @return data Donn�es de la trame
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * M�thode setter des donn�es de la trame
	 * @param data Donn�es � ins�rer dans la trame
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * M�thode getter du CRC
	 * @return CRC Donn�es encrypt�es
	 */
	public byte[] getCRC() {
		return CRC;
	}

	/**
	 * M�thode setter du CRC
	 * @param cRC Donn�es encrypt�es � ins�rer dans la trame
	 */
	public void setCRC(byte[] cRC) {
		CRC = cRC;
	}

	/**
	 * M�thode getter du num�ro de paquet
	 * @return Num�ro du paquet
	 */
	public byte[] getPacketNumber() {
		return packetNumber;
	}

	/**
	 * M�thode setter du num�ro de paquet
	 * @param packetNumber Entier
	 */
	public void setPacketNumber(int packetNumber) {
		this.packetNumber = createByteFromInt(packetNumber);
	}

	/**
	 * M�thode setter du num�ro de paquet
	 * @param packetNumber Bytes
	 */
	public void setPacketNumber(byte[] packetNumber) {
		this.packetNumber = packetNumber;
	}

	/**
	 * M�thode getter du nombre total de paquet
	 * @return Nombre de paquet en bytes
	 */
	public byte[] getPacketAmount() {
		return packetAmount;
	}

	/**
	 * M�thode setter du nombre total de paquets
	 * @param packetAmount Nombre de paquets total en entier
	 */
	public void setPacketAmount(int packetAmount) {

		this.packetAmount = createByteFromInt(packetAmount);
	}

	/**
	 * M�thode setter du nombre total de paquets
	 * @param packetAmount Nombre de paquets total en bytes
	 */
	public void setPacketAmount(byte[] packetAmount) {

		this.packetAmount = packetAmount;
	}

	/**
	 * M�thode getter du num�ro de paquet en entier
	 * @return Nombre du paquet en entier 
	 */
	public int getPacketNumberInt() {
		return packetNumberInt;
	}

	/**
	 * M�thode setter du num�ro de paquet en entier
	 * @param packetNumberInt Num�ro du paquet en entier
	 */
	public void setPacketNumberInt(int packetNumberInt) {
		this.packetNumberInt = packetNumberInt;
	}

	/**
	 * M�thode getter du nombre total de paquet en entier
	 * @return Nombre de paquet total en entier
	 */
	public int getPacketAmountInt() {
		return packetAmountInt;
	}

	/**
	 * M�thode setter du nombre de paquet total en entier
	 * @param packetAmountInt Nombre de paquet total en entier
	 */
	public void setPacketAmountInt(int packetAmountInt) {
		this.packetAmountInt = packetAmountInt;
	}

	/**
	 * M�thode permettant de transf�rer un nombre entier en bytes
	 * @param packets Nombre entier � convertir
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
	 * M�thode getter de la trame en bytes
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
	 * M�thode getter de la trame en bytes sans le CRC
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
	 * M�thode getter du header
	 * @return header
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 * M�thode setter du header
	 * @param header
	 */
	public void setHeader(byte[] header) {
		this.header = header;
	}

}
