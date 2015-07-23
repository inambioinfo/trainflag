package uk.ac.babraham.trainflag.survey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import uk.ac.babraham.trainflag.server.data.ClientInstance;
import uk.ac.babraham.trainflag.server.data.ClientSet;

public class SurveyResponseSet {

	private SurveyQuestion question;
	private HashMap<ClientInstance, Integer> answers = new HashMap<ClientInstance, Integer>();
	private ClientSet clients;
	
	public SurveyResponseSet (SurveyQuestion question, ClientSet clients) {
		this.question = question;
		this.clients = clients;
	}
	
	public void addResponse (ClientInstance client, int answer) {
		//TODO: Validate this is a possible answer
		answers.put(client, answer);
	}
	
	public int totalAnswers () {
		return answers.size();
	}
	
	public SurveyQuestion question () {
		return question;
	}
	
	public ClientSet clients () {
		return clients;
	}
	
	public int getCountForAnswer (int answerIndex) {
		Collection<Integer> c = answers.values();
		Iterator<Integer> i = c.iterator();
		
		int count = 0;
		while (i.hasNext()) {
			if (i.next() == answerIndex) ++count;
		}
		
		return count;
	}
}
