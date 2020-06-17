package Couche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 
 * Couche d'application du serveur et du client
 *
 */
public class Application {
	
	/**
	 * Path du fichier à ouvrir dans l'application
	 */
	private String path;
	/**
	 * Addresse ip du serveur où le fichier doit être envoyé
	 */
	private String ipServer;
	/**
	 * Paramètre que de contrôle d'erreur que l'on veut tester 
	 * 0 - Pas d'erreur,  1 - Erreur de CRC,  2 - Erreur de packet manquant
	 */
	private String wantedError;
	
	/**
	 * Couche de transport
	 */
	Transport transportLayer = new Transport();

	/**
	 * Constructeur d'application permettant de générer le path, l'addresse et pas d'erreur par défaut
	 */
	public Application() {
		this.path = "";
		this.ipServer = "";
		this.wantedError = "0";
	}

	/**
	 * Constructeur d'application
	 * @param path Path du fichier client
	 * @param ipServer Adresse IP à envoyer le fichier
	 * @param wantedError Erreur voulue: 0 - Pas d'erreur,  1 - Erreur de CRC,  2 - Erreur de packet manquant
	 */
	public Application(String path, String ipServer, String wantedError) {
		this.path = path;
		this.ipServer = ipServer;
		this.wantedError = wantedError;
	}

	/**
	 * Fonction qui s'exécute à chaque fois que l'objet est construit
	 */
	public void Run() {
		transportLayer.GetFileName(Paths.get(path));
		transportLayer.sendRequest(GetFileText(), ipServer, wantedError);
	}

	/**
	 * Fonction qui lit le fichier du client et qui transforme le contenu en bytes
	 * @return Byte[] du contenu du fichier lu ou un message d'erreur en bytes
	 */
	private byte[] GetFileText() {
		try {

			return Files.readAllBytes(Paths.get(path));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error while reading file".getBytes();
	}

}
