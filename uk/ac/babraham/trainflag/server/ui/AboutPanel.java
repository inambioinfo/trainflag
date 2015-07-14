package uk.ac.babraham.trainflag.server.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

//import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.ac.babraham.trainflag.resources.ColourScheme;
import uk.ac.babraham.trainflag.server.TrainFlagServer;


public class AboutPanel extends JPanel {

	public AboutPanel() {
		setLayout(new BorderLayout(5,1));

//		ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("uk/ac/babraham/SeqMonk/Resources/bi_logo.png"));
//		ImageIcon monk = new ImageIcon(ClassLoader.getSystemResource("uk/ac/babraham/SeqMonk/Resources/monk100.png"));

//		add(new JLabel("",logo,JLabel.CENTER),BorderLayout.WEST);
//		add(new JLabel("",monk,JLabel.CENTER),BorderLayout.EAST);
		JPanel c = new JPanel();
		c.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx=1;
		constraints.gridy=1;
		constraints.weightx = 1;
		constraints.weighty=1;
		constraints.insets = new Insets(6, 6, 0, 0);
		constraints.fill = GridBagConstraints.NONE;

		JLabel program = new SmoothJLabel("TrainFlag training status program",JLabel.CENTER);
		program.setFont(new Font("Dialog",Font.BOLD,26));
		program.setForeground(ColourScheme.WORKING_COLOUR);
		c.add(program,constraints);

		constraints.gridy++;
		JLabel version = new SmoothJLabel("Version: "+TrainFlagServer.VERSION, JLabel.CENTER);
		version.setFont(new Font("Dialog",Font.BOLD,15));
		version.setForeground(ColourScheme.WORKING_COLOUR);
		c.add(version,constraints);

		constraints.gridy++;
		// Use a text field so they can copy this
		JTextField website = new JTextField(" www.bioinformatics.babraham.ac.uk/projects/ ");
		website.setFont(new Font("Dialog",Font.PLAIN,14));
		website.setEditable(false);
		website.setBorder(null);
		website.setOpaque(false);
		website.setHorizontalAlignment(JTextField.CENTER);
		c.add(website,constraints);
		constraints.gridy++;

		JLabel copyright = new JLabel("\u00a9 Simon Andrews Babraham Bioinformatics, 2015", JLabel.CENTER);
		copyright.setFont(new Font("Dialog",Font.PLAIN,12));
		c.add(copyright,constraints);
		constraints.gridy++;

		add(c,BorderLayout.CENTER);
	}


	/**
	 * A JLabel with anti-aliasing enabled.  Takes the same constructor
	 * arguments as JLabel
	 */
	private class SmoothJLabel extends JLabel {

		/**
		 * Creates a new label
		 * 
		 * @param text The text
		 * @param position The JLabel constant position for alignment
		 */
		public SmoothJLabel (String text, int position) {
			super(text,position);
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent (Graphics g) {
			if (g instanceof Graphics2D) {
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			super.paintComponent(g);
		}

	}

}
