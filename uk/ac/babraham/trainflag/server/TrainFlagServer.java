package uk.ac.babraham.trainflag.server;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import uk.ac.babraham.trainflag.server.network.BroadcastReceiver;
import uk.ac.babraham.trainflag.server.network.ServerThread;
import uk.ac.babraham.trainflag.server.ui.AboutPanel;
import uk.ac.babraham.trainflag.server.ui.ClientSetPanel.ClientSetPanel;
import uk.ac.babraham.trainflag.server.ui.RoomPanel.RoomPanelContainer;
import uk.ac.babraham.trainflag.server.ui.SurveyPanel.SurveyPanel;
import uk.ac.babraham.trainflag.survey.SurveySet;

public class TrainFlagServer extends JFrame {

	public static final String VERSION = "0.1.devel";

	private ClientSet clients = new ClientSet();
	private SurveySet surveys = new SurveySet();
	private JTextField courseName;


	public TrainFlagServer () {
		super("Train Flag Server");
		setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/trainflag_logo.png")).getImage());


		// Create the server thread which is going to do the processing of
		// incoming connections
		try {
			new ServerThread(clients);
		}
		catch (IOException ioe) {
			// We can't start the network thread so we might as well give up now
			JOptionPane.showMessageDialog(this, "<html>Can't start the network service<br>Is TrainFlag server running already?<br><br>Error:"+ioe.getLocalizedMessage()+"</html>", "Can't start", JOptionPane.ERROR_MESSAGE);
			System.exit(1);

		}

		getContentPane().setLayout(new BorderLayout());

		JPanel courseNamePanel = new JPanel();
		courseNamePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		courseNamePanel.setLayout(new BorderLayout());

		courseNamePanel.add(new JLabel("Course Name  "),BorderLayout.WEST);
		courseName = new JTextField("My first training course");
		courseNamePanel.add(courseName,BorderLayout.CENTER);

		getContentPane().add(courseNamePanel,BorderLayout.NORTH);

		JTabbedPane tabPanel = new JTabbedPane();



		tabPanel.add("Students", new ClientSetPanel(clients));


		tabPanel.add("Surveys", new SurveyPanel(clients,surveys));

		tabPanel.add("Layout",new RoomPanelContainer(clients));
				
		JPanel aboutContainer = new JPanel();
		aboutContainer.setLayout(new BorderLayout());
		aboutContainer.add(new AboutPanel(),BorderLayout.CENTER);
		tabPanel.add("About", aboutContainer);

		getContentPane().add(tabPanel,BorderLayout.CENTER);
	

		// Create the broadcast received which will trigger and advertisement
		// of the service to any clients who ping us
		new BroadcastReceiver(this);


		setSize(800,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);


	}

	public String name () {
		return courseName.getText();
	}

}
