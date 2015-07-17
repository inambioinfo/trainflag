package uk.ac.babraham.trainflag.server.ui.roomPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import uk.ac.babraham.trainflag.server.ClientSet;

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
		// TODO Auto-generated method stub
		
	}

}
