package uk.ac.babraham.trainflag.server.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import uk.ac.babraham.trainflag.resources.ColourScheme;
import uk.ac.babraham.trainflag.server.ClientInstance;
import uk.ac.babraham.trainflag.server.ClientSet;
import uk.ac.babraham.trainflag.server.ClientSetListener;;

public class RoomPanel extends JPanel implements ClientSetListener {

	private ClientSet clients;
	
	// This is going to be the size of the icons we're using.  It will also be
	// used to set the border around the room.  Later on we may set this dynamically
	// based on the number of clients we have.
	private int iconSize = 30;
	
	public RoomPanel (ClientSet clients) {
		this.clients = clients;
		clients.addClientSetListener(this);
	}
	
	public void paint (Graphics g) {
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Draw the presenter at the front (middle top)
		
		// Draw the clients
		ClientInstance [] allClients = clients.clients();
		
		for (int c=0;c<allClients.length;c++) {
			drawClient(g,allClients[c]);
		}
		
	}
	
	private void drawClient (Graphics g, ClientInstance client) {
		int x = getX(client.x());
		int y = getY(client.y());
		
		g.setColor(ColourScheme.getColourForState(client.state()));
		
		g.fillRect(x-(iconSize/2), y-(iconSize/2), iconSize, iconSize);

		g.setColor(Color.BLACK);

		g.drawRect(x-(iconSize/2), y-(iconSize/2), iconSize, iconSize);

		
		
	}
	
	private int getX (float proportion) {
		int usableWidth = getWidth()-(iconSize*2);
		int x = (int)(usableWidth * proportion);
		x += iconSize;
		return x;
	}
	
	private int getY (float proportion) {
		int usableHeight = getHeight()-(iconSize*2);
		int y = (int)(usableHeight * proportion);
		y += iconSize;
		return y;
	}

	
	
	public void clientAdded(ClientInstance client) {
		repaint();
	}

	public void clientRemoved(ClientInstance client) {
		repaint();
	}

	public void clientStateChanged(ClientInstance client, int status) {
		repaint();
	}

	public void clientNameChanged(ClientInstance client, String name) {
		repaint();
	}
	
	
	

}
