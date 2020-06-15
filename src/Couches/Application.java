package Couches;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Application {

	public void readTxt(String filePath) throws Exception {

		File file = new File(filePath);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		while ((st = br.readLine()) != null)
			System.out.println(st);
	}

	public void terminate(Boolean termination) throws Exception {
		if (termination = true) {
			System.exit(0);
		}
	}
}
