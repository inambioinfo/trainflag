package uk.ac.babraham.trainflag.server;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import uk.ac.babraham.trainflag.server.network.ServerThread;
import uk.ac.babraham.trainflag.server.ui.AboutPanel;
import uk.ac.babraham.trainflag.server.ui.ClientSetTableModel;
import uk.ac.babraham.trainflag.server.ui.StatusCellRenderer;
import uk.ac.babraham.trainflag.server.ui.SurveySetTableModel;
import uk.ac.babraham.trainflag.survey.SurveySet;

public class TrainFlagServer extends JFrame {

	public static final String VERSION = "0.1.devel";
	
	private ClientSet clients = new ClientSet();
	private SurveySet surveys = new SurveySet();
	private JTextField courseName;
	
	
	public TrainFlagServer () {
		super("Train Flag Server");
		
		// Create the server thread which is going to do the processing of
		// incoming connections
		new ServerThread(clients);
		
		getContentPane().setLayout(new BorderLayout());
		
		JPanel courseNamePanel = new JPanel();
		courseNamePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		courseNamePanel.setLayout(new BorderLayout());
		
		courseNamePanel.add(new JLabel("Course Name  "),BorderLayout.WEST);
		courseName = new JTextField("My little training course");
		courseNamePanel.add(courseName,BorderLayout.CENTER);
		
		getContentPane().add(courseNamePanel,BorderLayout.NORTH);
		
		JTabbedPane tabPanel = new JTabbedPane();
		
		
		JTable clientTable = new JTable(new ClientSetTableModel(clients));
		clientTable.setFont(new Font("Arial", Font.PLAIN, 20));
		clientTable.setRowHeight(30);
		clientTable.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());
		
		tabPanel.add("Students", new JScrollPane(clientTable));

		
		JTable surveyTable = new JTable(new SurveySetTableModel(surveys));
		surveyTable.setFont(new Font("Arial", Font.PLAIN, 20));
		surveyTable.setRowHeight(30);
		
		tabPanel.add("Surveys", new JScrollPane(surveyTable));

		JPanel aboutContainer = new JPanel();
		aboutContainer.setLayout(new BorderLayout());
		aboutContainer.add(new AboutPanel(),BorderLayout.CENTER);
		tabPanel.add("About", aboutContainer);
		
		getContentPane().add(tabPanel,BorderLayout.CENTER);
		
		setSize(800,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public String name () {
		return courseName.getText();
	}
	
}
