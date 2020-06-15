package Couches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {

	Transport transport = new Transport();
	
	private String hostname;
	private Path filePath;
	private byte[] content = new byte[0];
	
	public Application(String hostname, Path filePath) {
		this.hostname  = hostname;
		this.filePath = filePath;
	}
	
	public void Run() {
		getFileData();
		transport.sendRequest(content, hostname);
		
	}
	
	private void getFileData() {
 	   try {
           content = Files.readAllBytes(filePath);
           System.out.println(new String(content,"UTF-8"));
       } catch (IOException e) {
           e.printStackTrace();
       }
	}
	
}
