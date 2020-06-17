package Client;

import java.io.IOException;

import Couche.Application;

public class Client {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Argument missing! Try: <path> <hostname> <wanted error: 0-no error,  1-CRC error,  2-missing packet error>");
			return;
		}

		Application app = new Application(args[0], args[1], args[2]);
		app.Run();

	}
}
