package Couche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {

		private String path;
		private String ipServer;
			 
		public Application() 
		{
			path = "";
			ipServer = "";
		}
		
		public Application(String path,String ipServer) 
		{
			this.path = path;
			this.ipServer = ipServer;
		}
		
		public void Run() 
		{
			Transport transportLayer = new Transport();
			transportLayer.sendRequest(GetFileText(), ipServer);
		}

		        
		private byte[] GetFileText()
		{
			try {
				
				return Files.readAllBytes(Paths.get(path));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Error while reading file".getBytes();
		}
		

}
