package uk.ac.babraham.trainflag.resources;

import java.awt.Color;

import uk.ac.babraham.trainflag.server.ClientInstance;

public class ColourScheme {
	
	public static final Color READY_COLOUR = new Color(217,95,2);
	public static final Color WORKING_COLOUR = new Color(56,108,176);
	public static final Color FINISHED_COLOUR = new Color(51,160,44);
	public static final Color HELP_COLOUR = new Color(227,26,28);
	
	public static Color getColourForState (int state) {
		if (state == ClientInstance.STATE_COMPLETE) return FINISHED_COLOUR;
		if (state == ClientInstance.STATE_HELP) return HELP_COLOUR;
		if (state == ClientInstance.STATE_READY) return READY_COLOUR;
		if (state == ClientInstance.STATE_WORKING) return WORKING_COLOUR;
		
		return Color.BLACK;
	}
	
	
	
}
