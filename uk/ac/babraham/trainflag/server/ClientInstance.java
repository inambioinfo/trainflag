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
	private int currentState = STATE_READY;
	
	private float xPosition = 0;
	private float yPosition = 0;
		
	
	public ClientInstance (InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public static boolean isValidState (int state) {
		if (state == STATE_COMPLETE || state == STATE_HELP || state == STATE_READY || state == STATE_WORKING) {
			return true;
		}
		return false;
	}
	
	public boolean matchesAddress (InetAddress address) {
		
		/** 
		 * This is a more lose matching than the standard 'equals'.  A match on either
		 * IP address, or hostname is enough to pass.
		 */
		
		if (ipAddress.equals(address)) return true;
		
		// We can also make a less specific check for either the ip 
		// address or the hostname being the same.
		
		if (ipAddress.getHostAddress().equals(address.getHostAddress())) return true;
		if (ipAddress.getHostName().equals(address.getHostName())) return true;
		
		return false;
	}
	
	/**
	 * Provide a relative position for this client within the room.  The x and
	 * y positions are relative positions on a scale of 0 to 1.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition (float x, float y) {
		if (x<0) x=0;
		if (x>1) x=1;
		if (y<0) y=0;
		if (y>1) y=1;
		
		xPosition = x;
		yPosition = y;
	}
	
	public float x () {
		return xPosition;
	}
	
	public float y () {
		return yPosition;
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
	
	public String hostname () {
		String hostname = address().getHostName();
		if (hostname.equals(address().getHostAddress())) {
			return hostname;
		}
		
		String [] sections = hostname.split("\\.");
		
		return sections[0];

	}
	
	protected void setStudentName (String studentName) {
		this.studentName = studentName;
	}
	
	public String studentName () {
		return studentName;
	}
	
	
}
