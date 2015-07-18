package uk.ac.babraham.trainflag.server.ui.roomPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPanel;

import uk.ac.babraham.trainflag.server.ClientSet;
import uk.ac.babraham.trainflag.server.network.IPtoByteConverter;

public class RoomPanelContainer extends JPanel implements ActionListener {
	
	private RoomPanel roomPanel;
	
	public RoomPanelContainer (ClientSet clients) {
		setLayout(new BorderLayout());
		roomPanel = new RoomPanel(clients);
		add(roomPanel,BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton loadButton = new JButton("Load Layout");
		loadButton.setActionCommand("load");
		loadButton.addActionListener(this);
		buttonPanel.add(loadButton);
		
		JButton saveButton = new JButton("Save Layout");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		
		buttonPanel.add(saveButton);
		
		add(buttonPanel,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("load")) {
			// TODO: Actually load something
			
			try {
			ClientInstanceSpace [] spaces = new ClientInstanceSpace [] {
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.6")),0,0.25f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.7")),0.25f,0.25f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.8")),0.5f,0.25f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.9")),0.75f,0.25f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.10")),1,0.25f),
					new ClientInstanceSpace(InetAddress.getByAddress("bi1274m",IPtoByteConverter.asBytes("149.155.148.11")),0,0.5f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.12")),0.25f,0.5f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.13")),0.75f,0.5f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.14")),1,0.5f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.15")),0,0.75f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.16")),0.25f,0.75f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.17")),0.5f,0.75f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.18")),0.75f,0.75f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.19")),1,0.75f),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.1")),0,1),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.2")),0.25f,1),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.3")),0.5f,1),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.4")),0.75f,1),
					new ClientInstanceSpace(InetAddress.getByAddress("Space",IPtoByteConverter.asBytes("149.155.148.5")),1,1)
			};
			
			roomPanel.setClientInstanceSpaces(spaces);
			
			}
			catch (UnknownHostException he) {
				he.printStackTrace();
			}
			
		}
		
	}

}
