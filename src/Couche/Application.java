package Couche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {

	private String path;
	private String ipServer;
	private String wantedError;
	Transport transportLayer = new Transport();

	public Application() {
		this.path = "";
		this.ipServer = "";
		this.wantedError = "0";
	}

	public Application(String path, String ipServer, String wantedError) {
		this.path = path;
		this.ipServer = ipServer;
		this.wantedError = wantedError;
	}

	public void Run() {
		transportLayer.GetFileName(Paths.get(path));
		transportLayer.sendRequest(GetFileText(), ipServer, wantedError);
	}

	private byte[] GetFileText() {
		try {

			return Files.readAllBytes(Paths.get(path));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error while reading file".getBytes();
	}

}
