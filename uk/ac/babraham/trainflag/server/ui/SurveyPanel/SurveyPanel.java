package uk.ac.babraham.trainflag.server.ui.SurveyPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.trainflag.server.data.TrainFlagData;
import uk.ac.babraham.trainflag.survey.SurveyQuestion;

public class SurveyPanel extends JSplitPane implements MouseListener, ActionListener {

	private JTable surveyTable;
	private SurveyResponsePanel questionPanel;
	private TrainFlagData tfData;

	public SurveyPanel (TrainFlagData tfData) {
		super(JSplitPane.VERTICAL_SPLIT);
		this.tfData = tfData;

		surveyTable = new JTable(new SurveySetTableModel(tfData.surveys()));
		surveyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		surveyTable.addMouseListener(this);
		surveyTable.setFont(new Font("Arial", Font.PLAIN, 20));
		surveyTable.setRowHeight(30);
		setTopComponent(new JScrollPane(surveyTable));

		JPanel questionAndButtonPanel = new JPanel();
		questionAndButtonPanel.setLayout(new BorderLayout());

		questionPanel = new SurveyResponsePanel(tfData);
		questionAndButtonPanel.add(questionPanel,BorderLayout.CENTER);

		JPanel surveyButtonPanel = new JPanel();

		JButton loadSurveysButton = new JButton("Load Surveys");
		loadSurveysButton.setActionCommand("load_surveys");
		loadSurveysButton.addActionListener(this);
		surveyButtonPanel.add(loadSurveysButton);

		JButton saveSurveysButton = new JButton("Save Surveys");
		saveSurveysButton.setActionCommand("save_surveys");
		saveSurveysButton.addActionListener(this);
		surveyButtonPanel.add(saveSurveysButton);

		JButton newSurveyButton = new JButton("Add Question");
		newSurveyButton.setActionCommand("new_question");
		newSurveyButton.addActionListener(this);
		surveyButtonPanel.add(newSurveyButton);

		questionAndButtonPanel.add(surveyButtonPanel,BorderLayout.SOUTH);

		setBottomComponent(questionAndButtonPanel);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == surveyTable) {
			// This comes from the survey table
			questionPanel.setQuestion(tfData.surveys().questions()[surveyTable.getSelectedRow()]);
			if (me.getClickCount() == 2) {
				new SurveyQuestionEditor(tfData.surveys().questions()[surveyTable.getSelectedRow()]);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("new_question")) {
			SurveyQuestion newQuestion = new SurveyQuestion("Why is a raven like a writing desk?");
			new SurveyQuestionEditor(newQuestion);
			SurveyPanel.this.tfData.surveys().addQuestion(newQuestion);
		}
		else if (ae.getActionCommand().equals("load_surveys")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileFilter(new FileFilter() {

				public String getDescription() {
					return "TrainFlag Survey Set Files";
				}

				public boolean accept(File f) {
					if (f.isDirectory() || f.getName().toLowerCase().endsWith(".tfs")) {
						return true;
					}
					else {
						return false;
					}
				}

			});

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.CANCEL_OPTION) return;

			File file = chooser.getSelectedFile();

			try {
				tfData.surveys().loadSurveySet(file);
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
				JOptionPane.showMessageDialog(this, ioe.getMessage(), "Failed to load survey", JOptionPane.ERROR_MESSAGE);
			}

		}
		else if (ae.getActionCommand().equals("save_surveys")) {
			while (true) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileFilter(new FileFilter() {

					public String getDescription() {
						return "TrainFlag Survey Set Files";
					}

					public boolean accept(File f) {
						if (f.isDirectory() || f.getName().toLowerCase().endsWith(".tfs")) {
							return true;
						}
						else {
							return false;
						}
					}

				});

				int result = chooser.showSaveDialog(this);
				if (result == JFileChooser.CANCEL_OPTION) return;

				File file = chooser.getSelectedFile();
				if (! file.getPath().toLowerCase().endsWith(".tfs")) {
					file = new File(file.getPath()+".tfs");
				}

				// Check if we're stepping on anyone's toes...
				if (file.exists()) {
					int answer = JOptionPane.showOptionDialog(this,file.getName()+" exists.  Do you want to overwrite the existing file?","Overwrite file?",0,JOptionPane.QUESTION_MESSAGE,null,new String [] {"Overwrite and Save","Cancel"},"Overwrite and Save");

					if (answer > 0) {
						continue;
					}
				}
				try {
				tfData.surveys().saveSurveySet(file);
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					JOptionPane.showMessageDialog(this, ioe.getMessage(), "Failed to save survey", JOptionPane.ERROR_MESSAGE);
				}
				
				break;
			}
		}
	}
}