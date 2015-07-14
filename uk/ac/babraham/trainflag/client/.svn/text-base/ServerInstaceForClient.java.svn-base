package uk.ac.babraham.trainflag.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerInstaceForClient {

	private InetAddress address;
	
	public ServerInstaceForClient (InetAddress address) {
		this.address = address;
	}
	
	public boolean sendCommand (String [] sections) throws IOException {
		Socket socket = new Socket(address,9925);
		
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		StringBuffer command = new StringBuffer();
		command.append(sections[0]);
		for (int i=1;i<sections.length;i++) {
			command.append("\t");
			command.append(sections[i]);
		}
		
		out.println(command.toString());
		out.flush();
		
		String answer = in.readLine();
		
		if (answer.equals("SUCCESS")) {
			return true;
		}
		
		System.err.println("Failed command "+answer);
		
		return false;
	}
	
	
}
