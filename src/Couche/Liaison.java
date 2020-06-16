package Couche;

import java.util.zip.CRC32;

public class Liaison {

	private byte[] calculCRC(byte[] message) 
	{
		CRC32 crc = new CRC32();
		crc.update(message);
		
		long value = crc.getValue();
		
		
		
		return message;
		
		
		
	}
	
}
