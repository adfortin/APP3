package Couche;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import AuxClass.Trame;
/**
 *
 * Couche de liaison entre le serveur et le client
 *
 */
public class Liaison {

	/**
	 * M�thode servant � trouver le CRC � partir de donn�es en bytes
	 */
	public byte[] calculCRC(byte[] message) {
		String mTrimmed = new String(message).trim();
		CRC32 crc = new CRC32();
		crc.update(mTrimmed.getBytes());
		long value = crc.getValue();

		return String.valueOf(value).getBytes();
	}

	/**
	 * M�thode qui retourne une trame avec les donn�es ins�r�es par argument en string
	 * @param String donn�es que l'on veut convertir en trame
	 * @return Trame avec les donn�es ins�r�es dans le param�tre
	 */
	public Trame getTrame(String data) {
		Trame trame = new Trame();
		String pattern = "([0-9].*?)(BEGIN)(\\d{8})(\\d{8})(.*?$)";
		Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
		Matcher m = r.matcher(data);

		if (m.find()) {
			trame.setCRC(m.group(1).getBytes());
			trame.setHeader(m.group(2).getBytes());
			trame.setPacketNumber(m.group(3).getBytes());
			trame.setPacketAmount(m.group(4).getBytes());
			trame.setData(m.group(5).getBytes());
			trame.setPacketNumberInt(Integer.parseInt(m.group(3)));
			trame.setPacketAmountInt(Integer.parseInt(m.group(4)));
		} else {
			System.out.println("NO MATCH");
		}
		return trame;
	}

	/**
	 * M�thode qui re�oit une trame et qui compare le CRC de la trame avec le CRC calcul� du reste de la trame
	 * afin de v�rifier que les donn�es sont bien exactes
	 * @param trameRecu Trame qui contient le CRC et les donn�es � recalculer
	 * @return True si le CRC de la trame re�ue et le CRC calcul� sont �quivalent
	 */
	public boolean validateTrameCRC(Trame trameRecu) {
		byte[] crc = trameRecu.getCRC();
		byte[] trameWithoutCRC = trameRecu.getTrameTrimmed();
		byte[] newCRC = calculCRC(trameWithoutCRC);

		return Arrays.equals(crc, newCRC);
	}

	/**
	 * M�thode qui v�rifie si un packet est manquant
	 * @param packets Liste des packets qui sont envoy�s
	 * @param currentPacketId Num�ro du packet � v�rifier
	 * @return True si le packet est manquant
	 */
	public int checkForSkipedPacket(List<Trame> packets, int currentPacketId) {
		if (packets.size() == 0 && currentPacketId == 1) {
			return 0;
		} else {
			if (currentPacketId - 1 != packets.get(packets.size() - 1).getPacketNumberInt()) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * M�thode qui permet d'�crire un log avec les informations de la trame re�ue en param�tre
	 * le param�tre operation sert � savoir si la trame est re�ue du client ou du serveur
	 * @param trameAEcrire La trame � �crire dans le fichier log
	 * @param operation Quel �l�ment � envoy� la trame
	 */
    public void ecrireLog(Trame trameAEcrire,int operation, int  packetSuccessful, int packetLoss, int packetError) {
        File log = new File(".\\liaisonDeDonnees.log");

        try {
            if (log.createNewFile()) {
                System.out.println("Fichier log cr��: " + log.getName());
            }
        } catch (IOException e) {
            System.out.println("Erreur de creation de fichier log");
            e.printStackTrace();
        }

        try {
            FileWriter logWriter = new FileWriter(log.getAbsolutePath(), true);

            logWriter.write(java.time.LocalDateTime.now() + " ");

            if (operation == 0)
            {
                logWriter.write("Paquet re�u du client: ");
            }
            else 
            {
                logWriter.write("Paquet envoy� au client: ");
            }


            logWriter.write(new String(trameAEcrire.getCRC()).trim() + " ");
            logWriter.write(new String(trameAEcrire.getHeader()).trim() + " ");
            logWriter.write(new String(trameAEcrire.getPacketNumber()).trim() + " ");
            logWriter.write(new String(trameAEcrire.getPacketAmount()).trim() + " ");
            logWriter.write(new String(trameAEcrire.getData()).trim() + "\n");
            
            
            if (operation == 2) {
            	 logWriter.write("TRANSMISSION STATS:   SuccessfulPackets: " + String.valueOf(packetSuccessful) + "   PacketLoss: " + String.valueOf(packetLoss)+ "   PacketError: " + String.valueOf(packetError));
            }
            

            logWriter.close();
        } catch (IOException e) {
            System.out.println("Erreur au niveau de l'ecriture du log");
            e.printStackTrace();
        }
    }
}
