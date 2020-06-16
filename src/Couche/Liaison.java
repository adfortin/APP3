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
		
		return String.valueOf(value).getBytes();
	}
	
	
	public Trame validateTrame(String data) {
        Trame trame = new Trame();
        String pattern = "([0-9].*?)(BEGIN)(\\d{8})(\\d{8})(.*?$)"; 
        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);

        Matcher m = r.matcher(data);


          if (m.find( )) {
             System.out.println("CRC: " + m.group(1) );
             System.out.println("Header: " + m.group(2) );
             System.out.println("Number: " + m.group(3) );
             System.out.println("Amount: " + m.group(4) );
             System.out.println("Data: " + m.group(5) );
             System.out.println("");
          } else {
             System.out.println("NO MATCH");
          }
		
		
		trame.setHeader("BEGIN".getBytes());
		trame.setCRC(m.group(1).getBytes());
		trame.setPacketNumber(Integer.parseInt(m.group(3)));
		trame.setPacketAmount(Integer.parseInt(m.group(4)));
		
		return trame;
	}
	
	
	
	
	
	
	
	
	
}
