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
	 * Méthode servant a trouver le CRC a partir de données en bytes
	 */
	public byte[] calculCRC(byte[] message) {
		String mTrimmed = new String(message).trim();
		CRC32 crc = new CRC32();
		crc.update(mTrimmed.getBytes());
		long value = crc.getValue();

		return String.valueOf(value).getBytes();
	}

	/**
	 * Méthode qui retourne une trame avec les données insérées par argument en string
	 * @param String données que l'on veut convertir en trame
	 * @return Trame avec les données insérées dans le paramètre
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
	 * Méthode qui reçoit une trame et qui compare le CRC de la trame avec le CRC calculé du reste de la trame
	 * afin de vérifier que les données sont bien exactes
	 * @param trameRecu Trame qui contient le CRC et les données a recalculer
	 * @return True si le CRC de la trame reçue et le CRC calculé sont équivalents
	 */
	public boolean validateTrameCRC(Trame trameRecu) {
		byte[] crc = trameRecu.getCRC();
		byte[] trameWithoutCRC = trameRecu.getTrameTrimmed();
		byte[] newCRC = calculCRC(trameWithoutCRC);

		return Arrays.equals(crc, newCRC);
	}

	/**
	 * Méthode qui vérifie si un packet est manquant
	 * @param packets Liste des packets qui sont envoyés
	 * @param currentPacketId Numéro du packet a vérifier
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
	 * Méthode qui permet d'écrire un log avec les informations de la trame reçue en paramètre
	 * le paramètre operation sert a savoir si la trame est reçue du client ou du serveur
	 * @param trameAEcrire La trame a écrire dans le fichier log
	 * @param operation Quel élément a envoyé la trame
	 */
    public void ecrireLog(Trame trameAEcrire,int operation, int  packetSuccessful, int packetLoss, int packetError) {
        File log = new File(".\\liaisonDeDonnees.log");

        try {
            if (log.createNewFile()) {
                System.out.println("Fichier log créé: " + log.getName());
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
                logWriter.write("Paquet reçu du client: ");
            }
            else 
            {
                logWriter.write("Paquet envoyé au client: ");
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
            System.out.println("Erreur au niveau de l'écriture du log");
            e.printStackTrace();
        }
    }
}
