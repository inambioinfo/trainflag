package uk.ac.babraham.trainflag.server.data;

import java.util.Vector;

import uk.ac.babraham.trainflag.survey.SurveySet;

public class TrainFlagData {

	private ClientSet clients;
	private SurveySet surveys;
	
	private Vector<TrainFlagDataListener>listeners = new Vector<TrainFlagDataListener>();

	
	public TrainFlagData () {
		clients = new ClientSet(this);
		surveys = new SurveySet(this);
	}
	
	public ClientSet clients () {
		return clients;
	}
	
	public SurveySet surveys () {
		return surveys;
	}
	
	public void addTrainFlagDataListener (TrainFlagDataListener l) {
		if (l != null && ! listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeTrainFlagDataListener (TrainFlagDataListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	public TrainFlagDataListener [] listeners () {
		return listeners.toArray(new TrainFlagDataListener[0]);
	}
	
}
