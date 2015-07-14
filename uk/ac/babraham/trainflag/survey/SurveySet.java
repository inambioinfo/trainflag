package uk.ac.babraham.trainflag.survey;

import java.util.Enumeration;
import java.util.Vector;

public class SurveySet {

	private Vector<SurveySetListener> listeners = new Vector<SurveySetListener>();
	private Vector<SurveyQuestion> questions = new Vector<SurveyQuestion>();
	
	
	public void addSurveySetListener (SurveySetListener l) {
		if (l != null && ! listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeSurveySetListener (SurveySetListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	public void addQuestion (SurveyQuestion question) {
		questions.add(question);
		Enumeration<SurveySetListener> en = listeners.elements();
		while (en.hasMoreElements()) {
			en.nextElement().questionAdded(question);
		}
	}

	public void removeQuestion (SurveyQuestion question) {
		if (questions.contains(question)) {
			questions.remove(question);
			Enumeration<SurveySetListener> en = listeners.elements();
			while (en.hasMoreElements()) {
				en.nextElement().questionRemoved(question);
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
