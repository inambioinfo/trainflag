package uk.ac.babraham.trainflag.server.ui.SurveyPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import uk.ac.babraham.trainflag.survey.SurveyQuestion;

public class SurveyQuestionPanel extends JPanel {

	SurveyQuestion question = null;
	
	public void setQuestion (SurveyQuestion question) {
		this.question = question;
		repaint();
	}
	
	public Dimension getPreferredSize () {
		return (new Dimension(600,200));
	}
	
	public void paint (Graphics g) {
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		String text = "No question selected";

		g.setColor(Color.GRAY);
		
		if (question != null) {
			g.setColor(Color.BLACK);
			text = question.text();
		}
		
		// Work out the size we can make this text
		
		// TODO: Account for multiple lines
		
		int fontSize = 30;
		g.setFont(new Font("Arial", Font.PLAIN, fontSize));

		while (g.getFontMetrics().stringWidth(text) > getWidth()) {
			fontSize --;
			g.setFont(new Font("Arial", Font.PLAIN, fontSize));
		}
		
		g.drawString(text, (getWidth()-g.getFontMetrics().stringWidth(text))/2, getHeight()/2);
		
	}
	
}
