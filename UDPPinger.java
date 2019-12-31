import java.net.*;

public class UDPPinger {
	
	private final static int PACKET_SIZE = 512;
	
	public static void sendPing(PingMessage ping, DatagramSocket udpSocket) throws Exception {
		
		DatagramSocket ds = udpSocket;
		InetAddress ip = ping.getIP();
		int port = ping.getPort();
		
		byte buff[] = ping.getPayload().getBytes();
		
		DatagramPacket dp = new DatagramPacket(buff, buff.length, ip, port);
		
		ds.send(dp);
	}
	
	public static PingMessage receivePing(DatagramSocket udpSocket) throws SocketTimeoutException, Exception {
		udpSocket.setSoTimeout(1000);				//set timeout
		
		byte[] buff = new byte[PACKET_SIZE];
		
		DatagramPacket inpacket = new DatagramPacket(buff, PACKET_SIZE);
		udpSocket.receive(inpacket);
		
		String payLoad = data(buff).toString();
		
		PingMessage message = new PingMessage(inpacket.getAddress(), inpacket.getPort(), payLoad);
		
		return message;
	}
	
	private static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
	
}