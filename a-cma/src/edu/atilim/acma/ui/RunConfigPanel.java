package edu.atilim.acma.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.ui.ConfigManager.NormalMetric;
import edu.atilim.acma.ui.design.RunConfigPanelBase;
import edu.atilim.acma.util.ACMAUtil;

public class RunConfigPanel extends RunConfigPanelBase implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	public RunConfigPanel() {
		removePresetButton.addActionListener(this);
		duplicateButton.addActionListener(this);
		presetList.addListSelectionListener(this);
		
		updatePresetsList();
	}
	
	private RunConfig getSelectedConfig() {
		Object sel = presetList.getSelectedValue();
		if (sel == null) {
			presetList.setSelectedIndex(0);
			sel = ConfigManager.runConfigs().get(0);
		}
		
		return (RunConfig)sel;
	}
	
	private void updatePresetsList() {
		DefaultListModel lm = new DefaultListModel();
		for (RunConfig c : ConfigManager.runConfigs())
			lm.addElement(c);
		presetList.setModel(lm);
		
		presetList.setSelectedIndex(0);
	}
	
	private void updatePresetConfig() {
		RunConfig curconf = getSelectedConfig();
		
		actionTable.setModel(new ActionTableModel(ConfigManager.getActions(curconf), !curconf.getName().equals("Default")));
		actionTable.getColumnModel().getColumn(0).setPreferredWidth(500);
		actionTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		
		normalMetricsTable.setModel(new NormalMetricsTableModel(ConfigManager.getNormalMetrics(curconf), !curconf.getName().equals("Default")));
		normalMetricsTable.getColumnModel().getColumn(0).setPreferredWidth(500);
		normalMetricsTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		
		availableMetricsTable.setModel(new AvailableMetricsTableModel(ConfigManager.getMetrics(curconf), !curconf.getName().equals("Default")));
		availableMetricsTable.getColumnModel().getColumn(0).setPreferredWidth(280);
		availableMetricsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		availableMetricsTable.getColumnModel().getColumn(2).setPreferredWidth(20);
		availableMetricsTable.getColumnModel().getColumn(3).setPreferredWidth(20);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Actions.CONF_DUPLICATE)) {
			RunConfig current = getSelectedConfig();
			
			String name = null;
			do {
				name = JOptionPane.showInputDialog(this, "Please input a name for duplicated config", String.format("%s Copy", current.getName()));
				
				if (name == null) return;
				
				if (ConfigManager.getRunConfig(name) != null) {
					JOptionPane.showMessageDialog(this, "Configuration with this name already exists.");
					name = null;
				}
			} while (name == null);
			
			RunConfig copy = ACMAUtil.deepCopy(current);
			copy.setName(name);
			ConfigManager.add(copy);
			
			updatePresetsList();
		} else if (e.getActionCommand().equals(Actions.CONF_REMOVE)) {
			RunConfig current = getSelectedConfig();
			if (current.getName().equals("Default")) return;
			
			ConfigManager.remove(current);
			updatePresetsList();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(presetList)) {
			updatePresetConfig();
		}
	}
	
	private static class ActionTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private List<ConfigManager.Action> actions;
		private boolean editable;
		
		private ActionTableModel(List<ConfigManager.Action> actions, boolean editable) {
			this.actions = actions;
			this.editable = editable;
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (!(aValue instanceof Boolean) || columnIndex != 1) return;
			actions.get(rowIndex).setEnabled((Boolean)aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return actions.size();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1 && editable;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 1) return Boolean.class;
			return super.getColumnClass(columnIndex);
		}
		
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 1:
				return "E";
			case 0:
				return "Name";
			}
			return "";
		}

		@Override
		public Object getValueAt(int row, int col) {
			ConfigManager.Action action = actions.get(row);
			switch(col) {
			case 1:
				return action.isEnabled();
			case 0:
				return action.getName();
			}
			return null;
		}
	}
	
	private static class AvailableMetricsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private List<ConfigManager.Metric> metrics;
		private boolean editable;

		private AvailableMetricsTableModel(List<ConfigManager.Metric> metrics, boolean editable) {
			this.metrics = metrics;
			this.editable = editable;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (aValue instanceof Boolean && columnIndex == 3) {
				metrics.get(rowIndex).setEnabled((Boolean)aValue);
			} else if (aValue instanceof String && columnIndex == 1) {
				try {
					double val = Double.parseDouble((String)aValue);
					metrics.get(rowIndex).setWeight(val);
				} catch(NumberFormatException e) { }
			} else {
				return;
			}
			fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public int getRowCount() {
			return metrics.size();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 1 || columnIndex == 3) && editable;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 3 || columnIndex == 2) return Boolean.class;
			if (columnIndex == 2) return Double.class;
			return super.getColumnClass(columnIndex);
		}
		
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 3:
				return "E";
			case 2:
				return "P";
			case 1:
				return "Weight";
			case 0:
				return "Name";
			}
			return "";
		}

		@Override
		public Object getValueAt(int row, int col) {
			ConfigManager.Metric m = metrics.get(row);
			switch(col) {
			case 3:
				return m.isEnabled();
			case 2:
				return m.isPackageMetric();
			case 1:
				return m.getWeight();
			case 0:
				return m.getName();
			}
			return null;
		}
	}
	
	private static class NormalMetricsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private List<ConfigManager.NormalMetric> normalMetrics;
		private boolean editable;

		private NormalMetricsTableModel(List<NormalMetric> normalMetrics, boolean editable) {
			this.normalMetrics = normalMetrics;
			this.editable = editable;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (!(aValue instanceof Boolean) || columnIndex != 1) return;
			normalMetrics.get(rowIndex).setEnabled((Boolean)aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return normalMetrics.size();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1 && editable;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 1) return Boolean.class;
			return super.getColumnClass(columnIndex);
		}
		
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 1:
				return "E";
			case 0:
				return "Name";
			}
			return "";
		}

		@Override
		public Object getValueAt(int row, int col) {
			NormalMetric nm = normalMetrics.get(row);
			switch(col) {
			case 1:
				return nm.isEnabled();
			case 0:
				return nm.getName();
			}
			return null;
		}
	}
}
