package uk.ac.babraham.trainflag.server.ui.ClientSetPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import uk.ac.babraham.trainflag.server.ClientInstance;
import uk.ac.babraham.trainflag.server.ClientSet;
import uk.ac.babraham.trainflag.server.network.ClientInstaceForServer;

public class ClientSetPanel extends JPanel implements ActionListener {

	private ClientSet clients;

	public ClientSetPanel (ClientSet clients) {
		this.clients = clients;

		setLayout(new BorderLayout());

		JTable clientTable = new JTable(new ClientSetTableModel(clients));
		clientTable.setFont(new Font("Arial", Font.PLAIN, 20));
		clientTable.setRowHeight(30);
		clientTable.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());

		add(new JScrollPane(clientTable),BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton startExerciseButton = new JButton("Start Exercise");
		startExerciseButton.setActionCommand("start_exercise");
		startExerciseButton.addActionListener(this);
		buttonPanel.add(startExerciseButton);

		JButton endExerciseButton = new JButton("End Exercise");
		endExerciseButton.setActionCommand("end_exercise");
		endExerciseButton.addActionListener(this);
		buttonPanel.add(endExerciseButton);

		add(buttonPanel,BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		ClientInstance [] clients = this.clients.clients();
		InetAddress [] addresses = new InetAddress[clients.length];
		for (int c=0;c<clients.length;c++) {
			addresses[c] = clients[c].address();
		}
		try {
			if (ae.getActionCommand().equals("start_exercise")) {
				ClientInstaceForServer.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_WORKING}, addresses);
			}
			else if (ae.getActionCommand().equals("end_exercise")) {
				ClientInstaceForServer.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_READY}, addresses);
			}
			else {
				throw new IllegalArgumentException("Unknown command "+ae.getActionCommand());
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


}
