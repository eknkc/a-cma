package edu.atilim.acma.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.ui.design.DesignPanelBase;
import edu.atilim.acma.util.ACMAUtil;

public class DesignPanel extends DesignPanelBase {
	private static final long serialVersionUID = 1L;

	private Design design;
	private MetricTable metrics;
	private Set<Action> posActions;
	
	DesignPanel(Design design) {
		this.design = design;
		this.metrics = this.design.getMetrics();
		this.posActions = this.design.getPossibleActions();
		
		initPossibleActions();
		initMetrics();
	}
	
	private void initPossibleActions() {
		final DefaultListModel model = new DefaultListModel();
		for (Action act : posActions) {
			model.addElement(act.toString());
		}
		posActionsList.setModel(model);
		
		btnPosActionsRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				posActionsList.setModel(model);
			}
		});
		
		btnPosActionsChart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HashMap<String, Integer> actionMap = new HashMap<String, Integer>();
				for (Action act : posActions) {
					String type = act.getClass().getEnclosingClass().getSimpleName();
					
					if (actionMap.containsKey(type))
						actionMap.put(type, actionMap.get(type) + 1);
					else
						actionMap.put(type, 1);
				}
				
				DefaultPieDataset dataset = new DefaultPieDataset();
				for (Entry<String, Integer> e : actionMap.entrySet()) {
					dataset.setValue(ACMAUtil.splitCamelCase(e.getKey()), e.getValue());
				}
				
				JFreeChart chart = ChartFactory.createPieChart3D("Action Distribution", dataset, true, false, false);
				chart.setBackgroundPaint(new Color(255, 255, 255, 0));
				
				PiePlot plot = (PiePlot)chart.getPlot();
				plot.setBackgroundPaint(new Color(255, 255, 255, 0));
				plot.setOutlineVisible(false);
				plot.setForegroundAlpha(0.75f);
				ChartPanel panel = new ChartPanel(chart);
				panel.setOpaque(false);
				PanelDialog.display(panel, 700, 470);
			}
		});
	}
	
	private void initMetrics() {
		metricTable.setDefaultRenderer(Object.class, new MetricTableRenderer());
		metricTable.setModel(new MetricTableModel());
		metricTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		
		metricTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateMetricsChart();
					}
				});
			}
		});
		
		lblValNumMetrics.setText(String.valueOf(metrics.getCols().size()));
		lblValNumItems.setText(String.valueOf(metrics.getRows().size()));
		lblValWeightedSum.setText(String.format("%.2f", metrics.getWeightedSum()));
		
		updateMetricsChart();
	}
	
	private void updateMetricsChart() {
		ArrayList<String> cols = new ArrayList<String>(metrics.getCols().keySet());
		
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		
		for (int i = 0; i < cols.size(); i++) {
			ds.addValue(Math.log(metrics.getAverage(cols.get(i)) + 1), "Averages", cols.get(i));
		}
		
		if (metricTable.getSelectedRow() >= 0) {
			for (int i = 0; i < cols.size(); i++)
				ds.addValue(Math.log(metrics.get(metricTable.getSelectedRow(), i) + 1), "Selected", cols.get(i));
		}
		
		JFreeChart chart = ChartFactory.createBarChart("", "", "log(value)", ds, PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.getDomainAxis().setVisible(false);
		ChartPanel panel = new ChartPanel(chart);
		
		chartPanel.removeAll();
		chartPanel.add(panel);
		chartPanel.validate();
	}
	
	private class MetricTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public int getColumnCount() {
			return metrics.getCols().size() + 1;
		}
		
		@Override
		public String getColumnName(int arg0) {
			if (arg0 == 0)
				return "Name";
			
			return getMetric(arg0 - 1);
		}

		@Override
		public int getRowCount() {
			return metrics.getRows().size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			if (col == 0)
				return getRow(row);
			
			return metrics.get(row, col - 1);
		}
		
		private String getMetric(int index) {
			Map<String, Integer> cols = metrics.getCols();
			for (Entry<String, Integer> e : cols.entrySet()) {
				if (e.getValue().equals(index))
					return e.getKey();
			}
			return null;
		}
		
		private Object getRow(int index) {
			Map<String, Integer> cols = metrics.getRows();
			for (Entry<String, Integer> e : cols.entrySet()) {
				if (e.getValue().equals(index)) {
					Type t = design.getType(e.getKey());
					if (t != null)
						return t;
					return e.getKey();
				}
			}
			return null;
		}
	}
	
	private static class MetricTableRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		private static final Icon packageIcon = new ImageIcon(MetricTableRenderer.class.getResource("/resources/icons/java/package.gif"));
		private static final Icon classIcon = new ImageIcon(MetricTableRenderer.class.getResource("/resources/icons/java/class.gif"));
		private static final Icon interfaceIcon = new ImageIcon(MetricTableRenderer.class.getResource("/resources/icons/java/interface.gif"));
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			
			setIcon(null);
			
			if (value instanceof Double) {
				double dval = (Double)value;
				if (Double.isNaN(dval)) {
					setText("");
				}
			} else if (value instanceof Type) {
				Type tval = (Type)value;
				if (tval.isInterface())
					setIcon(interfaceIcon);
				else
					setIcon(classIcon);
			} else if (value instanceof String) {
				setIcon(packageIcon);
			}
			
			return this;
		}
	}
}
