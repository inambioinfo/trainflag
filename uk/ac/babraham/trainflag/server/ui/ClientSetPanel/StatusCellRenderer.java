package uk.ac.babraham.trainflag.server.ui.ClientSetPanel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import uk.ac.babraham.trainflag.resources.ColourScheme;

public class StatusCellRenderer extends DefaultTableCellRenderer {

	public StatusCellRenderer () {
		setHorizontalAlignment(JLabel.CENTER);
	}
	
	public Component getTableCellRendererComponent(
			JTable table, 
			Object value, 
			boolean isSelected, 
			boolean hasFocus, 
			int row, 
			int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	      if (value == "Complete") {
	        c.setBackground(ColourScheme.FINISHED_COLOUR);
	        c.setForeground(Color.WHITE);
	      }
	      else if (value == "Ready") {
		        c.setBackground(ColourScheme.READY_COLOUR);
		        c.setForeground(Color.WHITE);
	      }
	      else if (value == "Working") {
		        c.setBackground(ColourScheme.WORKING_COLOUR);
		        c.setForeground(Color.WHITE);
	      }
	      else if (value == "Help") {
		        c.setBackground(ColourScheme.HELP_COLOUR);
		        c.setForeground(Color.WHITE);
	      }
	      return c;
	    }
	
}
