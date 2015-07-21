package uk.ac.babraham.trainflag.server;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Vector;

public class ClientSet {
	
	/**
	 * This class keeps a record of all of the connected clients
	 */
	
	private Vector<ClientInstance> clients = new Vector<ClientInstance>();
	private Vector<ClientSetListener>listeners = new Vector<ClientSetListener>();
	
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
		Enumeration<ClientSetListener> en = listeners.elements();
		while (en.hasMoreElements()) {
			en.nextElement().clientAdded(client);
		}
	}

	public void removeClient (ClientInstance client) {
		clients.remove(client);
		Enumeration<ClientSetListener> en = listeners.elements();
		while (en.hasMoreElements()) {
			en.nextElement().clientRemoved(client);
		}
	}
	
	public void setClientState (ClientInstance client, int state) {
		client.setState(state);
		Enumeration<ClientSetListener> en = listeners.elements();
		while (en.hasMoreElements()) {
			en.nextElement().clientStateChanged(client, state);
		}
	}
	

	public void setClientName (ClientInstance client, String name) {
		client.setStudentName(name);
		Enumeration<ClientSetListener> en = listeners.elements();
		while (en.hasMoreElements()) {
			en.nextElement().clientNameChanged(client, name);
		}
	}

	
	public void addClientSetListener (ClientSetListener l) {
		if (l != null && ! listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeClientSetListener (ClientSetListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
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
