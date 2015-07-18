package uk.ac.babraham.trainflag.server.ui.roomPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.trainflag.server.ClientInstance;
import uk.ac.babraham.trainflag.server.ClientSet;
import uk.ac.babraham.trainflag.server.network.IPtoByteConverter;

public class RoomPanelContainer extends JPanel implements ActionListener {

	private RoomPanel roomPanel;
	private ClientSet clients;

	public RoomPanelContainer (ClientSet clients) {
		this.clients = clients;
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
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("load")) {
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

		else if (ae.getActionCommand().equals("save")){
			// Write the current positions of the clients out to a file
			while (true) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileFilter(new FileFilter() {

					public String getDescription() {
						return "TrainFlag Room Layout Files";
					}

					public boolean accept(File f) {
						if (f.isDirectory() || f.getName().toLowerCase().endsWith(".tfr")) {
							return true;
						}
						else {
							return false;
						}
					}

				});

				int result = chooser.showSaveDialog(this);
				if (result == JFileChooser.CANCEL_OPTION) return;

				File file = chooser.getSelectedFile();
				if (! file.getPath().toLowerCase().endsWith(".tfr")) {
					file = new File(file.getPath()+".tfr");
				}

				// Check if we're stepping on anyone's toes...
				if (file.exists()) {
					int answer = JOptionPane.showOptionDialog(this,file.getName()+" exists.  Do you want to overwrite the existing file?","Overwrite file?",0,JOptionPane.QUESTION_MESSAGE,null,new String [] {"Overwrite and Save","Cancel"},"Overwrite and Save");

					if (answer > 0) {
						continue;
					}
				}

				try {
				PrintWriter pr = new PrintWriter(new FileWriter(file));
				
				ClientInstance [] clients = this.clients.clients();
				
				for (int c=0;c<clients.length;c++) {
					pr.println(
							clients[c].address().getHostAddress()+
							"\t" +
							clients[c].address().getHostName()+
							"\t" +
							clients[c].x()+
							"\t" +
							clients[c].y()
							);
				}
				
				pr.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error saving file: "+ioe.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
				}
				
				break;
			}
		}

	}

}
