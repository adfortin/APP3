package Couche;

import java.util.zip.CRC32;

public class Liaison {

	public byte[] calculCRC(byte[] message) 
	{
		CRC32 crc = new CRC32();
		crc.update(message);
		
		return Long.toString(crc.getValue()).getBytes();
	}
}
