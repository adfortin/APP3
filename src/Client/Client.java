package Client;

import java.io.IOException;

import Couche.Application;

/**
 * 
 * Classe permettant de commencer l'application client
 *
 */
public class Client {
	/**
	 * Méthode permettant de commencer l'application
	 * @param args Arguments à insérer dans la console, notamment le path, l'adresse IP et le code d'erreur
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Argument missing! Try: <path> <hostname> <wanted error: 0-no error,  1-CRC error,  2-missing packet error>");
			return;
		}

		Application app = new Application(args[0], args[1], args[2]);
		app.Run();

	}
}
