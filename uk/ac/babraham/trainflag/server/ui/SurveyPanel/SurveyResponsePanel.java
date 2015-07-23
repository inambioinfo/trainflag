package uk.ac.babraham.trainflag.server.ui.SurveyPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.babraham.trainflag.server.data.ClientInstance;
import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.server.data.TrainFlagDataListener;
import uk.ac.babraham.trainflag.server.network.ClientInstaceForServer;
import uk.ac.babraham.trainflag.survey.SurveyAnswer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;
import uk.ac.babraham.trainflag.survey.SurveyResponseSet;;

public class SurveyResponsePanel extends JPanel implements TrainFlagDataListener, ActionListener {

	private TrainFlagData tfData;
	private SurveyQuestion question = null;
	private SurveyResponseSet responses = null;
	private SurveyQuestionPanel questionPanel;

	private JButton actionButton;


	public SurveyResponsePanel (TrainFlagData tfData) {
		this.tfData = tfData;
		tfData.addTrainFlagDataListener(this);
		setLayout(new BorderLayout());	
		questionPanel = new SurveyQuestionPanel();
		add(questionPanel,BorderLayout.CENTER);

		actionButton = new JButton("Ask Question");
		actionButton.addActionListener(this);
		add(actionButton,BorderLayout.EAST);

	}

	public void setQuestion (SurveyQuestion question) {
		this.question = question;
		questionPanel.setQuestion(question);
	}

	public void setSurveyReponse (SurveyResponseSet responses) {
		this.question = responses.question();
		questionPanel.setQuestion(responses.question());
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (question != null && responses == null) {
			// We're asking a question
			responses = new SurveyResponseSet(question,tfData.clients());
			try {
				ClientInstaceForServer.sendSurveyQuestion(responses);
			} 
			catch (IOException ioe) {
				ioe.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to ask survey: "+ioe.getMessage(),"Survey Failed",JOptionPane.ERROR_MESSAGE);
			}
			actionButton.setText("Show Answers");
		}
		else if (responses != null) {
			// We're showing the answers

			//TODO: Put up a dialog with the answers and tell the clients to show
			// the results too.

			responses = null;
			actionButton.setText("Ask Question");
		}

	}

	@Override
	public void clientAdded(ClientInstance client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientRemoved(ClientInstance client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientStateChanged(ClientInstance client, int status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientNameChanged(ClientInstance client, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void questionAdded(SurveyQuestion question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void questionRemoved(SurveyQuestion question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void answerAdded(SurveyQuestion question, SurveyAnswer answer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void answerRemoved(SurveyQuestion question, SurveyAnswer answer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surveyStarted(SurveyResponseSet responseSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerReceived(SurveyResponseSet responseSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surveyEnded(SurveyResponseSet responseSet) {
		// TODO Auto-generated method stub
		
	}


}
