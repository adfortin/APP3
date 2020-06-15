package Client;

import Couches.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    public static void main(String[] args) throws IOException {

    	String hostname;
    	Path filePath;
    	
        if (args.length != 2) {
             System.out.println("Usage: java Client <hostname> <filename.txt>");
             return;
        }else {
        	hostname = args[0];
        	filePath = Paths.get(args[1]);
        	Application app = new Application(hostname, filePath);
        
        	app.Run();
        }
    }
}