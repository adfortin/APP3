package Couches;

public class Trame {

	private byte[] packetNumber = new byte[4];
	private byte[] packetAmount = new byte[4];
	private byte[] data = new byte[187];
	private byte[] CRC = new byte[5];
	
	public Trame() {
		
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

}
