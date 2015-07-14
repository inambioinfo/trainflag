package uk.ac.babraham.trainflag.survey;

public class SurveyAnswer {

	public static final int ANSWER_OPINION = 1432;
	public static final int ANSWER_RIGHT = 1433;
	public static final int ANSWER_WRONG = 1434;
	
	
	private String text;
	private int answerType = ANSWER_OPINION;
	
	public SurveyAnswer (String text) {
		this.text = text;
	}
	
	public String text () {
		return text;
	}
	
	public void setAnswerType (int type) {
		if (!(type == ANSWER_OPINION || type == ANSWER_RIGHT || type == ANSWER_WRONG)) {
			throw new IllegalArgumentException("Unknown answer type "+type);
		}
		
		answerType = type;
	}
	
	public int answerType () {
		return answerType;
	}
	
}
