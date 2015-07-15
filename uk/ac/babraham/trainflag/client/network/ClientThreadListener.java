package uk.ac.babraham.trainflag.client.network;

import java.net.InetAddress;

public interface ClientThreadListener {

	public void serverAnswered (InetAddress address, String courseName);
	public void changeState (int state);
	
}
