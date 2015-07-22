package uk.ac.babraham.trainflag.survey;

import java.util.Vector;

import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.server.data.TrainFlagDataListener;

public class SurveySet {

	private Vector<SurveyQuestion> questions = new Vector<SurveyQuestion>();
	
	private TrainFlagData tfData;
	
	public SurveySet (TrainFlagData tfData) {
		this.tfData = tfData;
	}
	
	
	public void addSurveySetListener (TrainFlagDataListener l) {
		tfData.addTrainFlagDataListener(l);
	}
	
	public void removeSurveySetListener (TrainFlagDataListener l) {
		tfData.removeTrainFlagDataListener(l);
	}
	
	public void addQuestion (SurveyQuestion question) {
		questions.add(question);
		TrainFlagDataListener [] ls = tfData.listeners();
		for (int l=0;l<ls.length;l++) {
			ls[l].questionAdded(question);
		}
	}

	public void removeQuestion (SurveyQuestion question) {
		if (questions.contains(question)) {
			questions.remove(question);
			TrainFlagDataListener [] ls = tfData.listeners();
			for (int l=0;l<ls.length;l++) {
				ls[l].questionRemoved(question);
			}
		}
	}

	
	
	public int questionCount () {
		return questions.size();
	}
	
	public SurveyQuestion [] questions () {
		return questions.toArray(new SurveyQuestion[0]);
	}
	
}
