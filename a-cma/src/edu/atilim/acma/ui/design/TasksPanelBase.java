package edu.atilim.acma.ui.design;

import java.awt.BorderLayout;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TasksPanelBase extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JPanel panel;
	protected JList taskList;

	public TasksPanelBase() {
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Tasks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, BorderLayout.CENTER);
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout(0, 0));
		
		taskList = new JList();
		taskList.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"Asd", "BCD"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		taskList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(taskList);
	}
}
