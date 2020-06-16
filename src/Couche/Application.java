package Couche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {

		private String path;
		private String ipServer;
		Transport transportLayer = new Transport();
			 
		public Application() 
		{
			path = "";
			ipServer = "";
		}
		
		public Application(String path,String ipServer,String ErrorCode) 
		{
			this.path = path;
			this.ipServer = ipServer;
		}
		
		public void Run() 
		{
			transportLayer.GetFileName(Paths.get(path));
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
