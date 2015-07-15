package uk.ac.babraham.trainflag.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.trainflag.client.network.ClientThread;
import uk.ac.babraham.trainflag.client.network.ClientThreadListener;

public class ServerSelector extends JFrame implements MouseListener, ClientThreadListener {

	/**
	 * This class creates a UI which pings the local network segment
	 * and then listens for responses from any severs which are already
	 * running.
	 */
	
	private Vector<ServerInstance> servers = new Vector<ServerSelector.ServerInstance>();
	private JTable serverTable;
	private JTextField ipField;
	
	private ClientThread clientThread;
	
	
	public ServerSelector () {
		super("Select your course");
		
		// Start a server to listen for responses
		clientThread = new ClientThread();
		clientThread.addListener(this);
		
		serverTable = new JTable(new TrainFlagServerTableModel());
		getContentPane().setLayout(new BorderLayout());
		serverTable.addMouseListener(this);
		
		getContentPane().add(new JScrollPane(serverTable), BorderLayout.CENTER);
		
		JPanel startClientPanel = new JPanel();
		
		startClientPanel.setLayout(new BorderLayout());
		
		startClientPanel.add(new JLabel("Use server IP"),BorderLayout.WEST);
		ipField = new JTextField();
		startClientPanel.add(ipField, BorderLayout.CENTER);
		
		JButton goButton = new JButton("Start client");
		goButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				InetAddress address;
				try {
					address = InetAddress.getByName(ipField.getText());
				} 
				catch (UnknownHostException e1) {
					return;
				}
				
				ServerInstaceForClient server = new ServerInstaceForClient(address);
				
				//Register ourself
				try {
					if (server.sendCommand(new String [] {"REGISTER"})) {
						// We successfully registered, so we're done here
						TrainFlagClient client = new TrainFlagClient(server);
						clientThread.addListener(client);
						clientThread.removeListener(ServerSelector.this);
						setVisible(false);
						dispose();
					}
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}				
			}
		});
		
		startClientPanel.add(goButton, BorderLayout.EAST);
		getContentPane().add(startClientPanel, BorderLayout.SOUTH);
		
		
		// Now we can broadcast and see if we get anything

		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);

			byte [] sendData = "PING".getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"),9927);
			socket.send(sendPacket);
			
			socket.close();

		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		
		setSize(700,300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	

	private class TrainFlagServerTableModel extends AbstractTableModel {

		public int getColumnCount() {
			return 2;
		}

		public String getColumnName (int col) {
			if (col==0) return "IP Address";
			if (col==1) return "Course Name";
			return null;
		}
		
		public int getRowCount() {
			return servers.size();
		}

		public Object getValueAt(int row, int col) {
			if (col==0) return servers.elementAt(row).address.getHostAddress();
			if (col==1) return servers.elementAt(row).courseName;
			return null;
			
		}
		
	}
	
	private class ServerInstance {
		public InetAddress address;
		private String courseName;
		
		public ServerInstance (InetAddress address, String courseName) {
			this.address = address;
			this.courseName = courseName;
		}
	}

	@Override
	public void serverAnswered(InetAddress address, String courseName) {
		ServerInstance si = new ServerInstance(address, courseName);
		servers.add(si);
		((TrainFlagServerTableModel)(serverTable.getModel())).fireTableDataChanged();
	}

	public void changeState(int state) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		ipField.setText(servers.elementAt(serverTable.getSelectedRow()).address.getHostAddress());
	}

	public void mouseReleased(MouseEvent e) {}
	
	
}
