package uk.ac.babraham.trainflag.server.ui.SurveyPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.server.network.ClientInstaceForServer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;

public class SurveyPanel extends JSplitPane implements MouseListener, ActionListener {

	private JTable surveyTable;
	private SurveyQuestionPanel questionPanel;
	private TrainFlagData tfData;
	
	public SurveyPanel (TrainFlagData tfData) {
		super(JSplitPane.VERTICAL_SPLIT);
		this.tfData = tfData;

		surveyTable = new JTable(new SurveySetTableModel(tfData.surveys()));
		surveyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		surveyTable.addMouseListener(this);
		surveyTable.setFont(new Font("Arial", Font.PLAIN, 20));
		surveyTable.setRowHeight(30);
		setTopComponent(new JScrollPane(surveyTable));

		JPanel questionAndButtonPanel = new JPanel();
		questionAndButtonPanel.setLayout(new BorderLayout());
		
		questionPanel = new SurveyQuestionPanel();
		questionAndButtonPanel.add(questionPanel,BorderLayout.CENTER);
		
		JPanel surveyButtonPanel = new JPanel();
		
		JButton loadSurveysButton = new JButton("Load Surveys");
		loadSurveysButton.setActionCommand("load_surveys");
		loadSurveysButton.addActionListener(this);
		surveyButtonPanel.add(loadSurveysButton);
		
		JButton saveSurveysButton = new JButton("Save Surveys");
		saveSurveysButton.setActionCommand("save_surveys");
		saveSurveysButton.addActionListener(this);
		surveyButtonPanel.add(saveSurveysButton);
		
		JButton newSurveyButton = new JButton("Add Survey");
		newSurveyButton.setActionCommand("new_survey");
		newSurveyButton.addActionListener(this);
		surveyButtonPanel.add(newSurveyButton);
		
		JButton askSurveyButton = new JButton("Ask Survey");
		askSurveyButton.setActionCommand("ask_survey");
		askSurveyButton.addActionListener(this);
		surveyButtonPanel.add(askSurveyButton);	
		
		questionAndButtonPanel.add(surveyButtonPanel,BorderLayout.SOUTH);

		setBottomComponent(questionAndButtonPanel);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == surveyTable) {
			// This comes from the survey table
			questionPanel.setQuestion(tfData.surveys().questions()[surveyTable.getSelectedRow()]);
			if (me.getClickCount() == 2) {
				new SurveyQuestionEditor(tfData.surveys().questions()[surveyTable.getSelectedRow()]);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("new_survey")) {
			SurveyQuestion newQuestion = new SurveyQuestion("Why is a raven like a writing desk?");
			new SurveyQuestionEditor(newQuestion);
			SurveyPanel.this.tfData.surveys().addQuestion(newQuestion);
		}
		else if (ae.getActionCommand().equals("load_surveys")) {
			// TODO: Load surveys
		}
		else if (ae.getActionCommand().equals("save_surveys")) {
			// TODO: Load surveys
		}
		else if (ae.getActionCommand().equals("ask_survey")) {
			// Check if we have a survey selected
			if (surveyTable.getSelectedRow() != -1) {
				try {
					ClientInstaceForServer.sendSurveyQuestion(tfData.surveys().questions()[surveyTable.getSelectedRow()], tfData.clients().addresses());
				} 
				catch (IOException ioe) {
					ioe.printStackTrace();
					JOptionPane.showMessageDialog(this, "Failed to ask survey: "+ioe.getMessage(),"Survey Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	
}
