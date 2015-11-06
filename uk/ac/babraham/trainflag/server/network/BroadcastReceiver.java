package uk.ac.babraham.trainflag.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import uk.ac.babraham.trainflag.server.TrainFlagServer;

public class BroadcastReceiver implements Runnable {

	private TrainFlagServer tfServer;
	
	public BroadcastReceiver (TrainFlagServer tfServer) {
		this.tfServer = tfServer;
		
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		
		try {
			DatagramSocket socket = new DatagramSocket(9927,InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			
			while (true) {
				byte [] receiveBuffer = new byte[15000];
				DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				socket.receive(packet);
				
				System.err.println("Saw broadcast from "+packet.getAddress().getHostAddress());
				
				// We'll try writing back to the client who sent us that message
				//TODO: Add something to make the name unique (time server was started for example)
				ClientInstaceForServer.sendCommand(new String [] {"SERVER", tfServer.name()}, new InetAddress[] {packet.getAddress()});
			}
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
	}
	
	
	
	
}
