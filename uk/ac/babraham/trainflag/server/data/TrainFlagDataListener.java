package uk.ac.babraham.trainflag.server.data;

import uk.ac.babraham.trainflag.survey.SurveyAnswer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;
import uk.ac.babraham.trainflag.survey.SurveyResponseSet;

public interface TrainFlagDataListener {

	public void clientAdded (ClientInstance client);
	public void clientRemoved (ClientInstance client);
	public void clientStateChanged (ClientInstance client, int status);
	public void clientNameChanged (ClientInstance client, String name);
	public void questionAdded (SurveyQuestion question);
	public void questionRemoved (SurveyQuestion question);
	public void answerAdded (SurveyQuestion question, SurveyAnswer answer);
	public void answerRemoved (SurveyQuestion question, SurveyAnswer answer);
	public void surveyStarted (SurveyResponseSet responseSet);
	public void answerReceived (SurveyResponseSet responseSet);
	public void surveyEnded (SurveyResponseSet responseSet);
	

	
	
}
