package uk.ac.babraham.trainflag.server.ui.SurveyPanel;

import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.trainflag.server.data.ClientInstance;
import uk.ac.babraham.trainflag.server.data.TrainFlagDataListener;
import uk.ac.babraham.trainflag.survey.SurveyAnswer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;
import uk.ac.babraham.trainflag.survey.SurveyResponseSet;
import uk.ac.babraham.trainflag.survey.SurveySet;

public class SurveySetTableModel extends AbstractTableModel implements TrainFlagDataListener {

	private SurveySet surveySet;
	
	public SurveySetTableModel (SurveySet surverySet) {
		this.surveySet = surverySet;
		surveySet.addSurveySetListener(this);
	}
	
	public int getColumnCount() {
		return 4;
	}
	
	public String getColumnName (int col) {
		if (col == 0) return "Survey No";
		if (col == 1) return "Question";
		if (col == 2) return "Answers";
		if (col == 3) return "Randomise";
		return null;
	}
	

	public int getRowCount() {
		return surveySet.questionCount();
	}

	public Object getValueAt(int row, int col) {
		if (col == 0) return row+1;
		if (col == 1) return surveySet.questions()[row].text();
		if (col == 2) return surveySet.questions()[row].answers().length;
		if (col == 3) {
			if (surveySet.questions()[row].randomise()) {
				return "Yes";
			}
			return "No";
		};
		return null;

	}

	public void questionAdded(SurveyQuestion question) {
		fireTableDataChanged();
	}

	public void questionRemoved(SurveyQuestion question) {
		fireTableDataChanged();	}

	public void answerAdded(SurveyQuestion question, SurveyAnswer answer) {
		fireTableDataChanged();	}

	public void answerRemoved(SurveyQuestion question, SurveyAnswer answer) {
		fireTableDataChanged();
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
