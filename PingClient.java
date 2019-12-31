import java.net.*;
import java.util.*;

public class PingClient extends UDPPinger implements Runnable{

	public static void main(String[] args) {
		PingClient client = new PingClient();
		
		client.run();

	}

	@Override
	public void run(){
		try {
			running();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void running()  {
		
		//ask User for server ip/port and client port
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Input Server IP: ");
		String IP = scan.nextLine();
		
		System.out.println("Input Server Port: ");
		int serverPort = scan.nextInt();
		
		System.out.println("Input Client Port: ");
		int portnum = scan.nextInt();
		
		
		//create Datagram socket to receive
		DatagramSocket udpSocket = null;
		try {
			udpSocket = new DatagramSocket(portnum);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Contacting host: " + IP + " at port " + serverPort);
			
		
		boolean received[] = new boolean[10];
		long RTT[] = new long[10];
	
		//send and receive 2 messages
		for(int i = 0; i < 10; i++) {
			
			Date time = new Date();
			
			//create payload
			String payLoad = "PING " + i + " " + time.getTime();
			
			
			//create the PingMessage
			PingMessage message = null;
			try {
				message = new PingMessage(InetAddress.getByName(IP), serverPort, payLoad);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//create the PingMessage
			long startTime = System.nanoTime();				//use to calculate RTT
			try {
				UDPPinger.sendPing(message, udpSocket);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long endTime = 0;								//use to calculate RTT
			
			//receive message
			try {
				PingMessage receiveMessage = UDPPinger.receivePing(udpSocket);
				received[i] = true;
				System.out.println("Received packet from " + receiveMessage.getIP() + " " + receiveMessage.getPort() + " " + time.toString());
			} catch (SocketTimeoutException e) {
//				e.printStackTrace();
				System.out.println(e.toString());
				received[i] = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			endTime = System.nanoTime();					//use to calculate RTT
			
			//calculate RTT
			long totalTime;
			if(received[i]) {
				totalTime = endTime - startTime;
			}
			else {
				totalTime = 1000000000;				//10^9 nano sec = 1000 ms
			}

			RTT[i] = totalTime/1000000;
			
		}//end of for loop

		//calculate max, min, and avg time in milliseconds
		udpSocket.close();
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		long avg = 0;
		for(int i = 0; i < 10; i++) {
			min = Long.min(min, RTT[i]);
			max = Long.max(max, RTT[i]);
			avg += RTT[i];
//			System.out.println(RTT[i]);
			System.out.println("PING " + i + ": " + received[i] + " RTT: " + RTT[i]);
		}
		
		avg /= 10;
		System.out.println("Minimum: " + min + "ms, Maximum: " + max + "ms, Average: " + avg + "ms");
	}

}
