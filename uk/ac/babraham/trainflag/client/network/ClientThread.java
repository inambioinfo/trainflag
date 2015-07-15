package uk.ac.babraham.trainflag.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

import uk.ac.babraham.trainflag.server.ClientInstance;

public class ClientThread implements Runnable {

	private Vector<ClientThreadListener> listeners = new Vector<ClientThreadListener>();
	private ServerSocket m_ServerSocket;
	
	public ClientThread () throws IOException {
		
		m_ServerSocket = new ServerSocket(9926);

		Thread t = new Thread(this);
		t.start();
	}
	
	public void addListener (ClientThreadListener l) {
		if (l != null && ! listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeListener (ClientThreadListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	public void run() {
		try {
			while (true) {
				Socket clientSocket = m_ServerSocket.accept();
				ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
				cliThread.start();
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			//TODO: Add a network error method to the listeners so they can see this
		}

	}


	class ClientServiceThread extends Thread {
		Socket clientSocket;

		ClientServiceThread(Socket s) {
			clientSocket = s;
		}

		public void run() {
			System.out.println("Accepted Client : Address - "+ clientSocket.getInetAddress().getHostAddress());

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

				// Read the first line which should be the command function

				String clientCommand = in.readLine();

				String [] commandSections = clientCommand.split("\t",-1);

				System.err.println("Command is "+clientCommand);

				if (commandSections[0].equals("PING")) {
					// Just show we still care.

					out.println("SUCCESS");
				}

				else if (commandSections[0].equals("SERVER")) {
					Enumeration<ClientThreadListener> en = listeners.elements();
					while (en.hasMoreElements()) {
						en.nextElement().serverAnswered(clientSocket.getInetAddress(), commandSections[1]);
					}
					
					out.println("SUCCESS");
				}

				
				else if (commandSections[0].equals("CHANGE_STATE")) {

					// We need to change our state, unless we're in HELP state
					// already, in which case we'll leave that set.

					try {
						int newState = Integer.parseInt(commandSections[1]);
						if (! ClientInstance.isValidState(newState)) {
							out.println("INVALID STATE "+newState);
						}
						else {
							Enumeration<ClientThreadListener> en = listeners.elements();
							while (en.hasMoreElements()) {
								en.nextElement().changeState(newState);
							}
							out.println("SUCCESS");
						}
					}
					catch (NumberFormatException nfe) {
						out.println("NON-NUMERIC STATE "+commandSections[1]);
					}

				}




				else {
					out.println("UNKNOWN COMMAND '"+commandSections[0]+"'");
					System.err.println("Unknown command '"+commandSections[0]+"'");
				}

				out.flush();
				clientSocket.close();

			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
