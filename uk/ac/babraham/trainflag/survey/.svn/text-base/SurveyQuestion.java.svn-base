package uk.ac.babraham.trainflag.survey;

import java.util.Vector;

public class SurveyQuestion {

	public static final int SINGLE_SELECTION = 4225;
	public static final int MULTIPLE_SELECTION = 4226;
	
	private String text;
	private Vector<SurveyAnswer> answers = new Vector<SurveyAnswer>();
	private boolean randomise = false;
	private int selectionType = SINGLE_SELECTION;
	
	
	public SurveyQuestion (String text) {
		this.text = text;
	}
	
	public String text () {
		return text;
	}
	
	public void addAnswer (SurveyAnswer answer) {
		answers.add(answer);
	}
	
	public void removeAnswer (SurveyAnswer answer) {
		answers.remove(answer);
	}
	
	public SurveyAnswer [] answers () {
		return answers.toArray(new SurveyAnswer[0]);
	}
	
	public void setRandomise (boolean randomise) {
		this.randomise = randomise;
	}
	
	public boolean randomise () {
		return randomise;
	}
	
	public int selectionType () {
		return selectionType;
	}
	
	public void setSelectionType (int type) {
		if (!(type == SINGLE_SELECTION || type == MULTIPLE_SELECTION)) {
			throw new IllegalArgumentException("Unknown selection type "+type);
		}
		
		selectionType = type;
	}
	
	
}
