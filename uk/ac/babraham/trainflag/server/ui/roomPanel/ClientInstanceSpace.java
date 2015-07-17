package uk.ac.babraham.trainflag.server.ui.roomPanel;

import java.net.InetAddress;

import uk.ac.babraham.trainflag.server.ClientInstance;

public class ClientInstanceSpace extends ClientInstance {

	public ClientInstanceSpace(InetAddress ipAddress, float x, float y) {
		super(ipAddress);
		setPosition(x, y);
	}

}
