package uk.ac.babraham.trainflag.survey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.server.data.TrainFlagDataListener;

public class SurveySet {

	private ArrayList<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
	
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
	
	
	public void saveSurveySet (File file) throws IOException {
		
		PrintWriter pr = new PrintWriter(new FileWriter(file));
		
		for (SurveyQuestion q : questions) {
			
			SurveyAnswer [] answers = q.answers();
			
			// We first print the question.  The first line is the number of answers followed by 
			// a 1/0 value for whether to randomise, followed by the question text
			int randomise = 0;
			if (q.randomise()) randomise = 1;
			pr.println(""+answers.length+"\t"+randomise+"\t"+q.text());
			
			for (int a=0;a<answers.length;a++) {
				pr.println(""+answers[a].answerType()+"\t"+answers[a].text());
			}
			
		}
		
		pr.close();
		
	}
	
	
	public void loadSurveySet (File file) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		
		while ((line = br.readLine()) != null) {
			
			// First line is the number of answers and the question
			String [] questionSections = line.split("\t");
			if (questionSections.length != 3) {
				throw new IOException("Wrong number of sections in question line '"+line+"'");
			}
			
			int numberOfAnswers = Integer.parseInt(questionSections[0]);
			boolean randomise = false;
			if (questionSections[1].equals("1")) randomise = true;

			SurveyQuestion question = new SurveyQuestion(questionSections[2]);
			question.setRandomise(randomise);
			
			for (int a=0;a<numberOfAnswers;a++) {
				line = br.readLine();
				
				String [] answerSections = line.split("\t");
				
				if (answerSections.length != 2) {
					throw new IOException("Wrong number of sections in answer line '"+line+"'");
				}
				
				SurveyAnswer answer = new SurveyAnswer(answerSections[1]);
				answer.setAnswerType(Integer.parseInt(answerSections[0]));
				
				question.addAnswer(answer);
			}
			
			addQuestion(question);			
		}
		
		br.close();
	}
	
}
