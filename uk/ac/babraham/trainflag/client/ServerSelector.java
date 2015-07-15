package uk.ac.babraham.trainflag.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.trainflag.client.network.ClientThread;
import uk.ac.babraham.trainflag.client.network.ClientThreadListener;
import uk.ac.babraham.trainflag.client.ui.BlockButton;
import uk.ac.babraham.trainflag.resources.ColourScheme;

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
		try {
			clientThread = new ClientThread();
		}
		catch (IOException ioe) {
			// We can't start the network thread so we might as well give up now
			JOptionPane.showMessageDialog(this, "<html>Can't start the network service<br>Is TrainFlag running already?<br><br>Error:"+ioe.getLocalizedMessage()+"</html>", "Can't start", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		clientThread.addListener(this);
		
		serverTable = new JTable(new TrainFlagServerTableModel());
		serverTable.setFont(new Font("Arial", Font.PLAIN, 20));
		serverTable.setRowHeight(30);
		serverTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		serverTable.getColumnModel().getColumn(1).setPreferredWidth(500);
		getContentPane().setLayout(new BorderLayout());
		serverTable.addMouseListener(this);
		
		getContentPane().add(new JScrollPane(serverTable), BorderLayout.CENTER);
		
		JPanel startClientPanel = new JPanel();
		
		startClientPanel.setLayout(new BorderLayout());
		startClientPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		startClientPanel.add(new JLabel("Other server IP  "),BorderLayout.WEST);
		ipField = new JTextField();
		startClientPanel.add(ipField, BorderLayout.CENTER);
		
		BlockButton goButton = new BlockButton("<html><center>Start<br>TrainFlag</center></html>",ColourScheme.FINISHED_COLOUR);
		goButton.setPreferredSize(new Dimension(150, 150));
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectServer();
			}
		});

		getContentPane().add(startClientPanel, BorderLayout.SOUTH);

		getContentPane().add(goButton, BorderLayout.EAST);

		BlockButton refreshButton = new BlockButton("<html><center>Refresh<br>List</center></html>",ColourScheme.WORKING_COLOUR);
		refreshButton.setPreferredSize(new Dimension(150, 150));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendBroadcast();
			}
		});
		
		getContentPane().add(refreshButton, BorderLayout.WEST);
		
		// Now we can broadcast and see if we get anything
		sendBroadcast();

		
		setSize(800,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void sendBroadcast () {

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
	}
	
	private void selectServer () {
		if (ipField.getText().length()==0) return;
		
		InetAddress address;
		try {
			address = InetAddress.getByName(ipField.getText());
		} 
		catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(this, "Address "+ipField.getText()+" didn't look like a real address","Can't register",JOptionPane.WARNING_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "Failed to register with server: "+ioe.getLocalizedMessage(),"Can't register",JOptionPane.WARNING_MESSAGE);
			ioe.printStackTrace();
		}				

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
		// See if this server matches any of the ones we know about already
		for (int s=0;s<servers.size();s++) {
			if(servers.elementAt(s).address.equals(address)) {
				// We know about this already
				if (!servers.elementAt(s).courseName.equals(courseName)) {
					servers.elementAt(s).courseName = courseName;
					((TrainFlagServerTableModel)(serverTable.getModel())).fireTableDataChanged();					
				}
				return;
			}
		}
		
		// If we get here then it's a new course we've not seen before.
		ServerInstance si = new ServerInstance(address, courseName);
		servers.add(si);
		((TrainFlagServerTableModel)(serverTable.getModel())).fireTableDataChanged();
	}

	public void changeState(int state) {}

	public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) selectServer();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		ipField.setText(servers.elementAt(serverTable.getSelectedRow()).address.getHostAddress());
	}

	public void mouseReleased(MouseEvent e) {}
	
	
}
