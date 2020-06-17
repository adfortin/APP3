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

public class Transport {
	private int wantedError;
	private int errorCpt;
	private int randonIndex;
	private int numberOfPacket;
	private int contentLength;
	private int maxDataLength = 169;
	private boolean alreadyCreated;
	private byte[] content;
	private byte[] fileName;
	private List<Trame> packets = new ArrayList<>();
	private List<byte[]> byteList = new ArrayList<>();
	private Liaison liaison;

	public Transport() {
		liaison = new Liaison();
	}

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

	public void GetFileName(Path path) {
		fileName = path.getFileName().toString().getBytes();
	}

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

	private int getRandomIndex() {
		return ThreadLocalRandom.current().nextInt(1, numberOfPacket + 1);
	}

}
