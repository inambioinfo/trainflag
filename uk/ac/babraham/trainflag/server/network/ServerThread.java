package uk.ac.babraham.trainflag.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import uk.ac.babraham.trainflag.server.ClientInstance;
import uk.ac.babraham.trainflag.server.ClientSet;

public class ServerThread implements Runnable {

	private ClientSet clients;
	private ServerSocket m_ServerSocket;
	
	public ServerThread (ClientSet clients) throws IOException {
		this.clients = clients;
		
		m_ServerSocket = new ServerSocket(9925);
		
		Thread t = new Thread(this);
		t.start();
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
				
				if (commandSections[0].equals("REGISTER")) {
					// We just need to create a new client instance for this IP
					
					ClientInstance ci = new ClientInstance(clientSocket.getInetAddress());
					
					// Check to see if this client has already registered and add them if not
					// If they are already registered then assume they just died on the client
					// end and restarted.
					if (!clients.hasClient(ci.address())) {
						clients.addClient(ci);
					}
					
					out.println("SUCCESS");
				}
								

				else if (commandSections[0].equals("DEREGISTER")) {
					// We need to remove this client from the list of clients

					if (clients.hasClient(clientSocket.getInetAddress())) {
						clients.removeClient(clients.getClient(clientSocket.getInetAddress()));
					}
					
					out.println("SUCCESS");
				}

				
				else if (commandSections[0].equals("PING")) {
					// Just show we still care.
					
					out.println("SUCCESS");
				}
				
				else if (commandSections[0].equals("CHANGE_STATE")) {

					// We need to change the state of this client

					if (clients.hasClient(clientSocket.getInetAddress())) {
						try {
							int newState = Integer.parseInt(commandSections[1]);
							if (! ClientInstance.isValidState(newState)) {
								out.println("INVALID STATE "+newState);
							}
							else {
								clients.setClientState(clients.getClient(clientSocket.getInetAddress()),newState);
								out.println("SUCCESS");
								
							}
						}
						catch (NumberFormatException nfe) {
							out.println("NON-NUMERIC STATE "+commandSections[1]);
						}
					}
					else {
						out.println("UNREGISTERED CLIENT");
					}
					
				}

				
				else if (commandSections[0].equals("CHANGE_NAME")) {

					// We need to change the name of this client

					if (clients.hasClient(clientSocket.getInetAddress())) {
						clients.setClientName(clients.getClient(clientSocket.getInetAddress()),commandSections[1]);
						out.println("SUCCESS");
					}
					else {
						out.println("UNREGISTERED CLIENT");
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
