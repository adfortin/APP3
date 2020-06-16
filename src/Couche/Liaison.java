package Couche;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class Liaison {

	public byte[] calculCRC(byte[] message) 
	{
		CRC32 crc = new CRC32();
		crc.update(message);
		
		long value = crc.getValue();
		
		System.out.println(String.valueOf(value));
		return String.valueOf(value).getBytes();
	}
	
	
	public Trame getTrame(String data)
	{
		Trame trame = new Trame();
		String pattern = "([0-9].*?)(BEGIN)(\\d{8})(\\d{8})(.*?$)"; 
		Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);
		
		Matcher m = r.matcher(data);
		
	      
	      if (m.find( )) {
	    	  trame.setCRC(m.group(1).getBytes());
	    	  trame.setHeader(m.group(2).getBytes());
	    	  trame.setPacketNumber(m.group(3).getBytes());
	    	  trame.setPacketAmount(m.group(4).getBytes());
	    	  trame.setData(m.group(5).getBytes());
	      } else {
	         System.out.println("NO MATCH");
	      }
	      
	      return trame;
	}
	
}
