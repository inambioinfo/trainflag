package uk.ac.babraham.trainflag.server.network;

import java.io.IOException;
import java.net.InetAddress;

import uk.ac.babraham.trainflag.server.data.ClientInstance;
import uk.ac.babraham.trainflag.server.data.ClientSet;

public class ClientHeartbeatCheck implements Runnable {

	private ClientSet clients;

	public ClientHeartbeatCheck (ClientSet clients) {
		this.clients = clients;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {

		while (true) {

			ClientInstance [] clients = this.clients.clients();

			for (int c=0;c<clients.length;c++) {

				try {
					if (! ClientInstaceForServer.sendCommand(new String [] {"PING"}, new InetAddress [] {clients[c].address()})[0]) {
						this.clients.removeClient(clients[c]);
					}
				}
				catch (IOException ioe) {
					this.clients.removeClient(clients[c]);
					}
			}


			try {
				Thread.sleep(10000);
			} 
			catch (InterruptedException e) {}

		}

	}





}
