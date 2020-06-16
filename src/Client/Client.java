package Client;

import java.io.IOException;

import Couche.Application;

public class Client {
	public static void main(String[] args) throws IOException 
	{
		if (args.length != 2) {
			System.out.println("Hostname or Filepath missing");
			return;
		}
      
		Application app = new Application(args[0],args[1],args[2]);
		app.Run();

	}
}
