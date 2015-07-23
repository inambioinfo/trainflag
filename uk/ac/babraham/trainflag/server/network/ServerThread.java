package uk.ac.babraham.trainflag.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import uk.ac.babraham.trainflag.server.data.ClientInstance;
import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.server.data.TrainFlagDataListener;
import uk.ac.babraham.trainflag.survey.SurveyAnswer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;
import uk.ac.babraham.trainflag.survey.SurveyResponseSet;

public class ServerThread implements Runnable, TrainFlagDataListener {

	private TrainFlagData tfData;
	private ServerSocket m_ServerSocket;
	private SurveyResponseSet surveyResponseSet = null;
	
	public ServerThread (TrainFlagData tfData) throws IOException {
		this.tfData = tfData;
		
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
					if (!tfData.clients().hasClient(ci.address())) {
						tfData.clients().addClient(ci);
					}
										
					out.println("SUCCESS");
				}
								

				else if (commandSections[0].equals("DEREGISTER")) {
					// We need to remove this client from the list of clients

					if (tfData.clients().hasClient(clientSocket.getInetAddress())) {
						tfData.clients().removeClient(tfData.clients().getClient(clientSocket.getInetAddress()));
					}
					
					out.println("SUCCESS");
				}
				
				else if (commandSections[0].equals("ANSWER_SURVEY")) {
					// Add this response to the survey response set
					
					if (surveyResponseSet != null) {				
						if (tfData.clients().hasClient(clientSocket.getInetAddress())) {
							surveyResponseSet.addResponse(tfData.clients().getClient(clientSocket.getInetAddress()), Integer.parseInt(commandSections[1]));
							out.println("SUCCESS");
						}
						else {
							out.println("ERROR_UNKNOWN_CLIENT");
						}
					}
					else {
						out.println("ERROR_NO_ACTIVE_SURVEY");
						
					}
				}

				
				else if (commandSections[0].equals("PING")) {
					// Just show we still care.
					
					out.println("SUCCESS");
				}
				
				else if (commandSections[0].equals("CHANGE_STATE")) {

					// We need to change the state of this client

					if (tfData.clients().hasClient(clientSocket.getInetAddress())) {
						try {
							int newState = Integer.parseInt(commandSections[1]);
							if (! ClientInstance.isValidState(newState)) {
								out.println("INVALID STATE "+newState);
							}
							else {
								tfData.clients().setClientState(tfData.clients().getClient(clientSocket.getInetAddress()),newState);
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

					if (tfData.clients().hasClient(clientSocket.getInetAddress())) {
						tfData.clients().setClientName(tfData.clients().getClient(clientSocket.getInetAddress()),commandSections[1]);
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


	public void clientAdded(ClientInstance client) {}
	public void clientRemoved(ClientInstance client) {}
	public void clientStateChanged(ClientInstance client, int status) {}
	public void clientNameChanged(ClientInstance client, String name) {}
	public void questionAdded(SurveyQuestion question) {}
	public void questionRemoved(SurveyQuestion question) {}
	public void answerAdded(SurveyQuestion question, SurveyAnswer answer) {}
	public void answerRemoved(SurveyQuestion question, SurveyAnswer answer) {}

	public void surveyStarted(SurveyResponseSet responseSet) {
		this.surveyResponseSet = responseSet;
	}

	public void answerReceived(SurveyResponseSet responseSet) {}

	public void surveyEnded(SurveyResponseSet responseSet) {
		surveyResponseSet = null;
	}

}
