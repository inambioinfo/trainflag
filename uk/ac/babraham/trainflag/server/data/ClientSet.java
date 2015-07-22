package uk.ac.babraham.trainflag.server.data;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Vector;

public class ClientSet {
	
	/**
	 * This class keeps a record of all of the connected clients
	 */
	
	private TrainFlagData tfData;
	
	private Vector<ClientInstance> clients = new Vector<ClientInstance>();
	
	public ClientSet(TrainFlagData tfData) {
		this.tfData = tfData;
	}
	
	public boolean hasClient(InetAddress address) {
		Enumeration<ClientInstance> en = clients.elements();
		while (en.hasMoreElements()) {
			if (en.nextElement().address().equals(address)) {
				return true;
			}
		}
		return false;
	}
	
	public ClientInstance getClient (InetAddress address) {
		Enumeration<ClientInstance> en = clients.elements();
		while (en.hasMoreElements()) {
			ClientInstance ci = en.nextElement();
			if (ci.address().equals(address)) {
				return ci;
			}
		}
		return null;
		
	}
	
	public void addClient (ClientInstance client) {
		clients.add(client);
		TrainFlagDataListener [] ls = tfData.listeners();
		for (int l=0;l<ls.length;l++) {
			ls[l].clientAdded(client);
		}
	}

	public void removeClient (ClientInstance client) {
		clients.remove(client);
		TrainFlagDataListener [] ls = tfData.listeners();
		for (int l=0;l<ls.length;l++) {
			ls[l].clientRemoved(client);
		}	
	}
	
	public void setClientState (ClientInstance client, int state) {
		client.setState(state);
		TrainFlagDataListener [] ls = tfData.listeners();
		for (int l=0;l<ls.length;l++) {
			ls[l].clientStateChanged(client, state);
		}	
	}
	

	public void setClientName (ClientInstance client, String name) {
		client.setStudentName(name);
		TrainFlagDataListener [] ls = tfData.listeners();
		for (int l=0;l<ls.length;l++) {
			ls[l].clientNameChanged(client, name);
		}		
	}

	
	public void addTrainFlagDataListener (TrainFlagDataListener l) {
		tfData.addTrainFlagDataListener(l);
	}
	
	public void removeTrainFlagDataListener (TrainFlagDataListener l) {
		tfData.removeTrainFlagDataListener(l);
	}
	
	public ClientInstance [] clients () {
		return (clients.toArray(new ClientInstance[0]));
	}
	
	public InetAddress [] addresses () {
		ClientInstance [] clients = clients();
		InetAddress [] addresses = new InetAddress[clients.length];
		for (int c=0;c<clients.length;c++) {
			addresses[c] = clients[c].address();
		}
		return addresses;
	}

	public int clientCount () {
		return clients.size();
	}
	
}
