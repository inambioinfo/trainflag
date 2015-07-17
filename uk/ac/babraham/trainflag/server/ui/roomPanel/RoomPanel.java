package uk.ac.babraham.trainflag.server.ui.roomPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import uk.ac.babraham.trainflag.resources.ColourScheme;
import uk.ac.babraham.trainflag.server.ClientInstance;
import uk.ac.babraham.trainflag.server.ClientSet;
import uk.ac.babraham.trainflag.server.ClientSetListener;;

public class RoomPanel extends JPanel implements ClientSetListener, MouseListener, MouseMotionListener {

	private ClientSet clients;
	private TrainerInstance trainer;
	
	private ClientInstanceSpace [] spaces = new ClientInstanceSpace [0];
		
	// This is going to be the size of the icons we're using.  It will also be
	// used to set the border around the room.  Later on we may set this dynamically
	// based on the number of clients we have.
	private int iconSize = 30;
	
	private ClientInstance selectedClient = null;
	
	public RoomPanel (ClientSet clients) {
		this.clients = clients;
		clients.addClientSetListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		try {
			trainer = new TrainerInstance();
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void setClientInstanceSpaces (ClientInstanceSpace [] spaces) {
		this.spaces = spaces;
		
		// Go through the existing clients, moving them to the spaces if there
		// are any
		ClientInstance [] clients = this.clients.clients();
		for (int c=0;c<clients.length;c++) {
			for (int i=0;i<spaces.length;i++) {
				if (spaces[i].address().equals(clients[c].address())) {
					clients[c].setPosition(spaces[i].x(), spaces[i].y());
				}
			}
		}
		
		repaint();
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
		
		// Draw the spaces, unless the corresponding client is already there
		for (int c=0;c<spaces.length;c++) {
			for (int i=0;i<allClients.length;i++) {
				if (allClients[i].address().equals(spaces[c].address())) {
					continue;
				}
			}
			drawClient(g, spaces[c]);
		}
		
		drawClient(g, trainer);
		
	}
	
	private void drawClient (Graphics g, ClientInstance client) {
		int x = getX(client.x());
		int y = getY(client.y());
		
		if (client == trainer) {
			g.setColor(Color.GRAY);
		}
		else if (client instanceof ClientInstanceSpace) {
			g.setColor(Color.WHITE);
		}
		else {
			g.setColor(ColourScheme.getColourForState(client.state()));
		}
		
		g.fillRect(x-(iconSize/2), y-(iconSize/2), iconSize, iconSize);

		g.setColor(Color.BLACK);

		g.drawRect(x-(iconSize/2), y-(iconSize/2), iconSize, iconSize);
		
		String name = client.studentName();
		g.drawString(name, x-(g.getFontMetrics().stringWidth(name)/2), y+iconSize);
		
		name = client.hostname();
		g.drawString(name, x-(g.getFontMetrics().stringWidth(name)/2), y+iconSize+g.getFontMetrics().getAscent());
	}
	
	private ClientInstance findClientAtPosition (int x, int y) {
		
		ClientInstance [] actualClients = clients.clients();
		
		ClientInstance [] allClients = new ClientInstance [actualClients.length+1];
		for (int i=0;i<actualClients.length;i++) {
			allClients[i] = actualClients[i];
		}
		allClients[allClients.length-1] = trainer;
		
		for (int c=0;c<allClients.length;c++) {
			int cx = getX(allClients[c].x());
			int cy = getY(allClients[c].y());
			
			if (Math.sqrt(Math.pow(cx-x, 2)+Math.pow(cy-y,2))< iconSize) {
				return allClients[c];
			}
		}
		return null;
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
	
	private float getXProportion (int x) {
		float usableWidth = getWidth()-(iconSize*2);

		x -= iconSize;
		
		float proportion = x/usableWidth;

		if (proportion<0) proportion=0;
		if (proportion>100) proportion=100;
		
		return proportion;
	}

	private float getYProportion (int y) {
		float usableHeight = getHeight()-(iconSize*2);

		y -= iconSize;
		
		float proportion = y/usableHeight;
		if (proportion<0) proportion=0;
		if (proportion>100) proportion=100;
		
		return proportion;
	}

	public void clientAdded(ClientInstance client) {
		// See if this client matches any of the room spaces, and move it
		// if it does
		for (int i=0;i<spaces.length;i++) {
			if (spaces[i].address().equals(client.address())) {
				client.setPosition(spaces[i].x(), spaces[i].y());
				break;
			}
		}
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

	public void mouseDragged(MouseEvent me) {

		if (selectedClient != null) {
			selectedClient.setPosition(getXProportion(me.getX()), getYProportion(me.getY()));
			repaint();
		}
		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent me) {
		// See if we're within a drawn client to select it
		selectedClient = findClientAtPosition(me.getX(), me.getY());
		repaint();
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class TrainerInstance extends ClientInstance {
		
		public TrainerInstance () throws UnknownHostException  {
			super(InetAddress.getLocalHost());
			setPosition(0.5f, 0);
		}
		
		public String studentName () {
			return "Trainer";
		}
		
		public String hostname () {
			return "";
		}
	}
	
	

}
