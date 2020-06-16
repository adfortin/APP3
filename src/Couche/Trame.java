package Couche;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Trame {
	
	private byte[] packetNumber;
	private byte[] packetAmount;
	private byte[] data;
	private byte[] CRC;
	
	public Trame()
	{
		setPacketNumber(new byte[4]);
		setPacketAmount(new byte[4]);
		setData(new byte[187]);
		setCRC(new byte[5]);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getCRC() {
		return CRC;
	}

	public void setCRC(byte[] cRC) {
		CRC = cRC;
	}

	public byte[] getPacketNumber() {
		return packetNumber;
	}

	public void setPacketNumber(byte[] packetNumber) {
		this.packetNumber = packetNumber;
	}

	public byte[] getPacketAmount() {
		return packetAmount;
	}

	public void setPacketAmount(byte[] packetAmount) {
		this.packetAmount = packetAmount;
	}
	
	public byte[] getTrame() 
	{

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(CRC);
			outputStream.write(packetNumber);
			outputStream.write(packetAmount);
			outputStream.write(data);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray( );
	}
}
