import java.net.*;
import java.util.*;
import java.io.*;

public class PingServer {

	private final int PACKET_SIZE = 512;
	private final int DOUBLE = 2;
	private final int AVERAGE_DELAY = 100;
	private final double LOSS_RATE = 0.3;
	
	public static void main(String[]args) {
		PingServer server = new PingServer();
		try {
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void run() throws Exception {
		
		Random random = new Random(new Date().getTime());
		
		int portnum = 5520;
		
		DatagramSocket udpSocket = new DatagramSocket(portnum);
		
		byte[] buff = new byte[PACKET_SIZE];
		
		boolean running = true;
		while(running) {			
			System.out.println("Waiting for UDP packet....");
			
			//used to simulate packet loss
			Random lossRandom = new Random();
			double rng = lossRandom.nextInt(10) + 1;
			rng /= 10;			

			//receive the datagram
			DatagramPacket inpacket = new DatagramPacket(buff, PACKET_SIZE);
			
			udpSocket.receive(inpacket);
			
			System.out.println("Received from: " + inpacket.getAddress() + " " + inpacket.getPort() + " " + data(buff).toString());
			
			//sleep to simulate transmission delay
			Thread.sleep((long)(random.nextDouble() * DOUBLE * AVERAGE_DELAY));
			
//			System.out.println("rng: " + rng);
			if(rng <= LOSS_RATE) {				//packet lost
				System.out.println("Packet loss...., reply not sent.");
				continue;
			}
			
			//send out datagram
			DatagramPacket outpacket = new DatagramPacket(buff, PACKET_SIZE, inpacket.getAddress(), inpacket.getPort());
			
			udpSocket.send(outpacket);
			
			//clear buff
			buff = new byte[PACKET_SIZE];
		}
		
	}
	
	private StringBuilder data(byte[] a) 
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

