package Couche;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Trame {
	
	private byte[] header;
	private byte[] packetNumber;
	private byte[] packetAmount;
	private int packetNumberInt;
	private int packetAmountInt;
	private byte[] data;
	private byte[] CRC;
	
	public Trame()
	{
		setHeader("BEGIN".getBytes());
		setPacketNumber(0);
		setPacketAmount(0);
		setPacketNumberInt(0);
		setPacketAmountInt(0);
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

	public void setPacketNumber(int packetNumber) {
		this.packetNumber = createByteFromInt(packetNumber);
	}
	
	public void setPacketNumber(byte[] packetNumber) {
		this.packetNumber = packetNumber;
	}


	public byte[] getPacketAmount() {
		return packetAmount;
	}

	public void setPacketAmount(int packetAmount) {
	
		this.packetAmount = createByteFromInt(packetAmount);
	}
	
	public void setPacketAmount(byte[] packetAmount) {
		
		this.packetAmount = packetAmount;
	}
	
	public byte[] createByteFromInt(int packets) 
	{
		String packetString = String.valueOf(packets);

		while (packetString.length() < 8) 
		{
			packetString = "0" + packetString;
		}
		
		return packetString.getBytes();
	}
	

	public byte[] getTrame() 
	{

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(CRC);
			outputStream.write(header);
			outputStream.write(packetNumber);
			outputStream.write(packetAmount);
			outputStream.write(data);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}
	
	public byte[] getTrameTrimmed() 
	{

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(header);
			outputStream.write(packetNumber);
			outputStream.write(packetAmount);
			outputStream.write(data);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public int getPacketNumberInt() {
		return packetNumberInt;
	}

	public void setPacketNumberInt(int packetNumberInt) {
		this.packetNumberInt = packetNumberInt;
	}

	public int getPacketAmountInt() {
		return packetAmountInt;
	}

	public void setPacketAmountInt(int packetAmountInt) {
		this.packetAmountInt = packetAmountInt;
	}

}
