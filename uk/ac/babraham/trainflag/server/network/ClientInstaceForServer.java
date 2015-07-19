package uk.ac.babraham.trainflag.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientInstaceForServer {

	public static boolean [] sendCommand (String [] sections, InetAddress [] addresses) throws IOException {

		boolean [] returnValues = new boolean[addresses.length];

		for (int a=0;a<addresses.length;a++) {

			InetAddress address = addresses[a];
			
			Socket socket = new Socket(address,9926);

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
				returnValues[a] = true;
			}
			else {
				System.err.println("Failed command "+answer);
				returnValues[a] = false;
			}
			

		}
		
		return returnValues;
	}


}
