package edu.atilim.acma.ui.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

public class DesignPanelBase extends JPanel {

	private static final long serialVersionUID = 1L;
	protected JTabbedPane tabbedPane;
	protected JPanel metricsPanel;
	protected JScrollPane scrollPane;
	protected JTable metricTable;
	protected JPanel actionsPanel;
	protected JPanel chartPanel;
	protected JPanel infoPanel;
	protected JLabel lblNumberOfMetrics;
	protected JLabel lblValNumMetrics;
	protected JLabel lblItems;
	protected JLabel lblValNumItems;
	protected JLabel dummyLabel1;
	protected JLabel dummyLabel2;
	protected JLabel lblweightedSum;
	protected JLabel lblValWeightedSum;
	protected JLabel dummyLabel3;
	protected JLabel dummyLabel4;
	protected JLabel dummyLabel5;
	protected JButton btnConfiguration;

	public DesignPanelBase() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(tabbedPane);
		
		metricsPanel = new JPanel();
		metricsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		metricsPanel.setOpaque(false);
		tabbedPane.addTab("Metrics", new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/statistics2_16.png")), metricsPanel, null);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		
		metricTable = new JTable();
		metricTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		metricTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(metricTable);
		
		chartPanel = new JPanel();
		chartPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		chartPanel.setOpaque(false);
		
		infoPanel = new JPanel();
		infoPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
		infoPanel.setOpaque(false);
		GroupLayout gl_metricsPanel = new GroupLayout(metricsPanel);
		gl_metricsPanel.setHorizontalGroup(
			gl_metricsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_metricsPanel.createSequentialGroup()
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
		);
		gl_metricsPanel.setVerticalGroup(
			gl_metricsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_metricsPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_metricsPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
		);
		infoPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblNumberOfMetrics = new JLabel("# Metrics:");
		infoPanel.add(lblNumberOfMetrics);
		
		lblValNumMetrics = new JLabel("");
		lblValNumMetrics.setHorizontalAlignment(SwingConstants.RIGHT);
		infoPanel.add(lblValNumMetrics);
		
		lblItems = new JLabel("# Items:");
		infoPanel.add(lblItems);
		
		lblValNumItems = new JLabel("");
		lblValNumItems.setHorizontalAlignment(SwingConstants.RIGHT);
		infoPanel.add(lblValNumItems);
		
		dummyLabel1 = new JLabel("");
		infoPanel.add(dummyLabel1);
		
		dummyLabel2 = new JLabel("");
		infoPanel.add(dummyLabel2);
		
		lblweightedSum = new JLabel("<html><b>Weighted Sum:</b></html>");
		infoPanel.add(lblweightedSum);
		
		lblValWeightedSum = new JLabel("");
		lblValWeightedSum.setHorizontalAlignment(SwingConstants.RIGHT);
		infoPanel.add(lblValWeightedSum);
		
		dummyLabel3 = new JLabel("");
		infoPanel.add(dummyLabel3);
		
		dummyLabel4 = new JLabel("");
		infoPanel.add(dummyLabel4);
		
		dummyLabel5 = new JLabel("");
		infoPanel.add(dummyLabel5);
		
		btnConfiguration = new JButton("Config");
		btnConfiguration.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/engine_16.png")));
		infoPanel.add(btnConfiguration);
		chartPanel.setLayout(new BorderLayout(0, 0));
		metricsPanel.setLayout(gl_metricsPanel);
		
		actionsPanel = new JPanel();
		actionsPanel.setOpaque(false);
		tabbedPane.addTab("Possible Actions", new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/engine_16.png")), actionsPanel, null);
	}
}
