package uk.ac.babraham.trainflag.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BlockButton extends JPanel implements MouseListener {

	private Vector<ActionListener> listeners = new Vector<ActionListener>();
	private String actionCommand = null;
	
	public BlockButton (String text, Color colour) {
		setOpaque(true);
		setBackground(colour);
		setLayout(new BorderLayout());
		JLabel label = new JLabel(text,JLabel.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		add(label,BorderLayout.CENTER);
		addMouseListener(this);
	}
	
	public void deselect () {
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}
	
	public void addActionListener (ActionListener l) {
		listeners.add(l);
	}
	
	public void setActionCommand (String command) {
		actionCommand = command;
	}

	public void mouseClicked(MouseEvent me) {

		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,5));

		Enumeration<ActionListener> en = listeners.elements();
		
		while (en.hasMoreElements()) {
			en.nextElement().actionPerformed(new ActionEvent(this, 0, actionCommand));
		}
	}

	public void mouseEntered(MouseEvent me) {}

	public void mouseExited(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {}

	public void mouseReleased(MouseEvent me) {}
}
