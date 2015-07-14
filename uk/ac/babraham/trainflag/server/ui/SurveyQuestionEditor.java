package uk.ac.babraham.trainflag.server.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.trainflag.resources.ColourScheme;
import uk.ac.babraham.trainflag.survey.SurveyAnswer;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;

public class SurveyQuestionEditor extends JDialog implements KeyListener, ActionListener {

	private SurveyQuestion question;
	private JCheckBox randomiseBox;
	private JTextArea questionArea;
	private JTable answerTable;
	private JTextField newAnswerField;
	private JComboBox answerTypeBox;
	
	public SurveyQuestionEditor (SurveyQuestion question) {
		setTitle("Question Editor");
		setModal(true);
		this.question = question;
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=0.5;
		gbc.weighty = 0.01;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		JLabel questionLabel = new JLabel("Question:");
		questionLabel.setFont(new Font("Dialog",Font.BOLD,25));
		questionLabel.setForeground(ColourScheme.WORKING_COLOUR);
		getContentPane().add(questionLabel,gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		
		questionArea = new JTextArea(question.text());
		questionArea.addKeyListener(this);
		getContentPane().add(questionArea,gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.01;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel answersLabel = new JLabel("Answers:");
		answersLabel.setFont(new Font("Dialog",Font.BOLD,25));
		answersLabel.setForeground(ColourScheme.WORKING_COLOUR);

		getContentPane().add(answersLabel,gbc);
		
		JPanel randomisePanel = new JPanel();
		randomisePanel.setLayout(new BorderLayout());
		randomiseBox = new JCheckBox();
		randomiseBox.setSelected(question.randomise());
		randomiseBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				SurveyQuestionEditor.this.question.setRandomise(randomiseBox.isSelected());
			}
		});
		
		randomisePanel.add(randomiseBox,BorderLayout.WEST);
		randomisePanel.add(new JLabel("Randomise answer order on clients"),BorderLayout.CENTER);
		gbc.gridy++;
		getContentPane().add(randomisePanel,gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.9;
		gbc.fill = GridBagConstraints.BOTH;

		AnswerTableModel answerModel = new AnswerTableModel();
		answerTable = new JTable(answerModel);

		getContentPane().add(new JScrollPane(answerTable),gbc);
		
		JPanel newAnswerPanel = new JPanel();
		newAnswerPanel.setLayout(new BorderLayout());
		newAnswerField = new JTextField();
		newAnswerPanel.add(newAnswerField,BorderLayout.CENTER);
		
		JButton newAnswerButton = new JButton("Add Answer");
		newAnswerButton.setActionCommand("new_answer");
		newAnswerButton.addActionListener(this);
		newAnswerPanel.add(newAnswerButton,BorderLayout.EAST);
		
		answerTypeBox = new JComboBox(new String [] {"Opinion","Right","Wrong"});
		newAnswerPanel.add(answerTypeBox,BorderLayout.WEST);
		
		gbc.gridy++;
		gbc.weighty = 0.01;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(newAnswerPanel,gbc);
		
		JPanel closePanel = new JPanel();
		JButton closeButton = new JButton("All Done");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		closePanel.add(closeButton);
		
		gbc.gridy++;
		gbc.weighty = 0.01;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(closePanel,gbc);
		
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(600,700);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}

	public void keyPressed(KeyEvent ke) {}

	public void keyReleased(KeyEvent arg0) {
		question.setText(questionArea.getText());
	}

	public void keyTyped(KeyEvent ke) {}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("new_answer")) {
			SurveyAnswer answer = new SurveyAnswer(newAnswerField.getText());
			question.addAnswer(answer);
			((AnswerTableModel)answerTable.getModel()).fireTableDataChanged();
			newAnswerField.setText("");
		}
	}
	
	private class AnswerTableModel extends AbstractTableModel {

		public int getColumnCount() {
			return 3;
		}

		public String getColumnName (int col) {
			if (col == 0) return "Position";
			if (col == 1) return "Answer";
			if (col == 2) return "Type";
			return null;
		}
		
		public int getRowCount() {
			return question.answers().length;
		}

		public Object getValueAt(int row, int col) {
			if (col == 0) return row+1;
			if (col == 1) return question.answers()[row].text();
			if (col == 2) {
				switch (question.answers()[row].answerType()) {
					case (SurveyAnswer.ANSWER_OPINION): return "Opinion";
					case (SurveyAnswer.ANSWER_RIGHT): return "Right";
					case (SurveyAnswer.ANSWER_WRONG): return "Wrong";
				}
				return null;
			}
			return null;
		}
		
	}

	public static void main (String [] args) {
		SurveyQuestion q = new SurveyQuestion("Why is a raven like a writing desk?");
		q.addAnswer(new SurveyAnswer("It just is"));
		q.addAnswer(new SurveyAnswer("One of its legs is both the same"));
		q.addAnswer(new SurveyAnswer("Pol Pot"));
		q.answers()[0].setAnswerType(SurveyAnswer.ANSWER_WRONG);
		q.answers()[1].setAnswerType(SurveyAnswer.ANSWER_WRONG);
		q.answers()[2].setAnswerType(SurveyAnswer.ANSWER_RIGHT);
		new SurveyQuestionEditor(q);
	}

}
