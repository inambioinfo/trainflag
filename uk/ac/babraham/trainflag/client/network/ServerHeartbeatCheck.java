package uk.ac.babraham.trainflag.client.network;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ServerHeartbeatCheck implements Runnable {

	private ServerInstaceForClient server;

	public ServerHeartbeatCheck (ServerInstaceForClient server) {
		this.server = server;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {

		while (true) {

			try {
				if (! server.sendCommand(new String [] {"PING"})) {
					JOptionPane.showMessageDialog(null, "Server seems to have gone away - exiting - sorry.","TrainFlag Server died",JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
			catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "Server seems to have gone away - exiting - sorry.","TrainFlag Server died",JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);				
			}


			try {
				Thread.sleep(10000);
			} 
			catch (InterruptedException e) {}
			
		}

	}





}
