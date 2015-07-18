package uk.ac.babraham.trainflag.server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import uk.ac.babraham.trainflag.server.network.BroadcastReceiver;
import uk.ac.babraham.trainflag.server.network.ServerThread;
import uk.ac.babraham.trainflag.server.ui.AboutPanel;
import uk.ac.babraham.trainflag.server.ui.RoomPanel.RoomPanelContainer;
import uk.ac.babraham.trainflag.server.ui.StatusPanel.ClientSetTableModel;
import uk.ac.babraham.trainflag.server.ui.StatusPanel.StatusCellRenderer;
import uk.ac.babraham.trainflag.server.ui.SurveyPanel.SurveyQuestionEditor;
import uk.ac.babraham.trainflag.server.ui.SurveyPanel.SurveySetTableModel;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;
import uk.ac.babraham.trainflag.survey.SurveySet;

public class TrainFlagServer extends JFrame implements MouseListener {

	public static final String VERSION = "0.1.devel";

	private ClientSet clients = new ClientSet();
	private SurveySet surveys = new SurveySet();
	private JTable surveyTable;
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


		JTable clientTable = new JTable(new ClientSetTableModel(clients));
		clientTable.setFont(new Font("Arial", Font.PLAIN, 20));
		clientTable.setRowHeight(30);
		clientTable.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());

		tabPanel.add("Students", new JScrollPane(clientTable));

		JPanel surveysPanel = new JPanel();
		surveysPanel.setLayout(new BorderLayout());

		surveyTable = new JTable(new SurveySetTableModel(surveys));
		surveyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		surveyTable.addMouseListener(this);
		surveyTable.setFont(new Font("Arial", Font.PLAIN, 20));
		surveyTable.setRowHeight(30);
		surveysPanel.add(new JScrollPane(surveyTable),BorderLayout.CENTER);

		JPanel surveyButtonPanel = new JPanel();
		JButton newSurveyButton = new JButton("Add Survey");
		newSurveyButton.setActionCommand("new_survey");
		newSurveyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				SurveyQuestion newQuestion = new SurveyQuestion("Why is a raven like a writing desk?");
				new SurveyQuestionEditor(newQuestion);
				surveys.addQuestion(newQuestion);
			}
		});

		surveyButtonPanel.add(newSurveyButton);

		surveysPanel.add(surveyButtonPanel,BorderLayout.SOUTH);

		tabPanel.add("Surveys", surveysPanel);

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

	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == surveyTable) {
			// This comes from the survey table
			if (me.getClickCount() == 2) {
				new SurveyQuestionEditor(surveys.questions()[surveyTable.getSelectedRow()]);
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent arg0) {}

}
