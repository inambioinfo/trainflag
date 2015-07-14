package uk.ac.babraham.trainflag.server;

import java.net.InetAddress;

public class ClientInstance {

	/**
	 * This is a class which records the details of an individual client which has
	 * registered with the server.
	 */
	
	/*
	 * The flag values for the state of the client
	 */
	
	public static final int STATE_READY = 2501;
	public static final int STATE_COMPLETE = 2502;
	public static final int STATE_HELP = 2503;
	public static final int STATE_WORKING = 2504;
	
	
	private String studentName = "Not known";
	private InetAddress ipAddress;
	private int port;
	private int currentState = STATE_READY;
	
	
	public ClientInstance (InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public static boolean isValidState (int state) {
		if (state == STATE_COMPLETE || state == STATE_HELP || state == STATE_READY || state == STATE_WORKING) {
			return true;
		}
		return false;
	}
	
	
	public int state () {
		return currentState;
	}
	
	protected void setState(int newState) {
		if (! ClientInstance.isValidState(newState)) {
			throw new IllegalArgumentException("Value "+newState+" isnt a valid state");
		}
		currentState = newState;
	}
	
	public InetAddress address () {
		return ipAddress;
	}
	
	protected void setStudentName (String studentName) {
		this.studentName = studentName;
	}
	
	public String studentName () {
		return studentName;
	}
	
	
}
