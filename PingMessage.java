import java.net.*;
import java.util.*;

public class PingMessage {

	private InetAddress addr;
	private int port;
	private String payLoad;
	
	public PingMessage(InetAddress addr, int port, String payLoad) {
		this.addr = addr;
		this.port = port;
		this.payLoad = payLoad;
	}
	
	public InetAddress getIP() {
		return addr;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getPayload() {
		return payLoad;
	}
}
