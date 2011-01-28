package edu.atilim.acma.ui.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import edu.atilim.acma.ui.Actions;
import edu.atilim.acma.ui.MainWindow;

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
	protected JPanel posActionsPanel;
	protected JPanel posActionsListPanel;
	protected JScrollPane postActionsListScroller;
	protected JList posActionsList;
	protected JButton btnPosActionsRefresh;
	protected JButton btnPosActionsChart;
	protected JPanel algorithmsPanel;
	protected JTabbedPane algorithmsTabPane;
	protected JPanel hillClimbingPanel;
	protected JLabel lblRestartCount;
	protected JSpinner hcRestartCount;
	protected JLabel lblRestartDepth;
	protected JSpinner hcRestartDepth;
	protected JButton hcBtnStart;
	protected Component hg1;
	protected Component hs1;
	protected Component hs3;
	protected Component hs2;
	protected JPanel simAnnPanel;
	protected JLabel lblIterationCount;
	protected Component hs4;
	protected JSpinner saIterationCnt;
	protected Component hg2;
	protected JButton saBtnStart;
	protected JPanel beamSearchPanel;
	protected JLabel lblBeamLength;
	protected Component hs5;
	protected JSpinner bsBeamLength;
	protected Component hg3;
	protected JButton bsBtnStart;

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
		btnConfiguration.setActionCommand(Actions.CONFIG_METRICS);
		btnConfiguration.addActionListener(MainWindow.getListener());
		infoPanel.add(btnConfiguration);
		chartPanel.setLayout(new BorderLayout(0, 0));
		metricsPanel.setLayout(gl_metricsPanel);
		
		actionsPanel = new JPanel();
		actionsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		actionsPanel.setOpaque(false);
		tabbedPane.addTab("Refactor", new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/misc3_16.png")), actionsPanel, null);
		
		posActionsPanel = new JPanel();
		posActionsPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Possible Actions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
		posActionsPanel.setOpaque(false);
		
		algorithmsPanel = new JPanel();
		algorithmsPanel.setOpaque(false);
		algorithmsPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Initiate Search", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
		GroupLayout gl_actionsPanel = new GroupLayout(actionsPanel);
		gl_actionsPanel.setHorizontalGroup(
			gl_actionsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(algorithmsPanel, GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
				.addComponent(posActionsPanel, GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
		);
		gl_actionsPanel.setVerticalGroup(
			gl_actionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_actionsPanel.createSequentialGroup()
					.addComponent(algorithmsPanel, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(posActionsPanel, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
		);
		
		algorithmsTabPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_algorithmsPanel = new GroupLayout(algorithmsPanel);
		gl_algorithmsPanel.setHorizontalGroup(
			gl_algorithmsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(algorithmsTabPane, GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
		);
		gl_algorithmsPanel.setVerticalGroup(
			gl_algorithmsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_algorithmsPanel.createSequentialGroup()
					.addComponent(algorithmsTabPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		
		hillClimbingPanel = new JPanel();
		hillClimbingPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		hillClimbingPanel.setOpaque(false);
		algorithmsTabPane.addTab("Hill Climbing", null, hillClimbingPanel, null);
		hillClimbingPanel.setLayout(new BoxLayout(hillClimbingPanel, BoxLayout.X_AXIS));
		
		lblRestartCount = new JLabel("Restart Count:");
		hillClimbingPanel.add(lblRestartCount);
		
		hs1 = Box.createHorizontalStrut(5);
		hillClimbingPanel.add(hs1);
		
		hcRestartCount = new JSpinner();
		hcRestartCount.setModel(new SpinnerNumberModel(5, 0, 100, 1));
		hillClimbingPanel.add(hcRestartCount);
		
		hs2 = Box.createHorizontalStrut(20);
		hillClimbingPanel.add(hs2);
		
		lblRestartDepth = new JLabel("Restart Depth:");
		hillClimbingPanel.add(lblRestartDepth);
		
		hs3 = Box.createHorizontalStrut(5);
		hillClimbingPanel.add(hs3);
		
		hcRestartDepth = new JSpinner();
		hcRestartDepth.setModel(new SpinnerNumberModel(20, 0, 1000, 1));
		hillClimbingPanel.add(hcRestartDepth);
		
		hg1 = Box.createHorizontalGlue();
		hillClimbingPanel.add(hg1);
		
		hcBtnStart = new JButton("Start");
		hcBtnStart.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/next_16.png")));
		hillClimbingPanel.add(hcBtnStart);
		
		simAnnPanel = new JPanel();
		simAnnPanel.setOpaque(false);
		simAnnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		algorithmsTabPane.addTab("Simulated Annealing", null, simAnnPanel, null);
		simAnnPanel.setLayout(new BoxLayout(simAnnPanel, BoxLayout.X_AXIS));
		
		lblIterationCount = new JLabel("Iteration Count:");
		simAnnPanel.add(lblIterationCount);
		
		hs4 = Box.createHorizontalStrut(5);
		simAnnPanel.add(hs4);
		
		saIterationCnt = new JSpinner();
		saIterationCnt.setModel(new SpinnerNumberModel(5000, 100, 1000000, 250));
		simAnnPanel.add(saIterationCnt);
		
		hg2 = Box.createHorizontalGlue();
		simAnnPanel.add(hg2);
		
		saBtnStart = new JButton("Start");
		saBtnStart.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/next_16.png")));
		simAnnPanel.add(saBtnStart);
		
		beamSearchPanel = new JPanel();
		beamSearchPanel.setOpaque(false);
		beamSearchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		algorithmsTabPane.addTab("Beam Search", null, beamSearchPanel, null);
		beamSearchPanel.setLayout(new BoxLayout(beamSearchPanel, BoxLayout.X_AXIS));
		
		lblBeamLength = new JLabel("Beam Length:");
		beamSearchPanel.add(lblBeamLength);
		
		hs5 = Box.createHorizontalStrut(5);
		beamSearchPanel.add(hs5);
		
		bsBeamLength = new JSpinner();
		bsBeamLength.setModel(new SpinnerNumberModel(50, 10, 500, 1));
		beamSearchPanel.add(bsBeamLength);
		
		hg3 = Box.createHorizontalGlue();
		beamSearchPanel.add(hg3);
		
		bsBtnStart = new JButton("Start");
		bsBtnStart.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/next_16.png")));
		beamSearchPanel.add(bsBtnStart);
		algorithmsPanel.setLayout(gl_algorithmsPanel);
		
		posActionsListPanel = new JPanel();
		posActionsListPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		posActionsListPanel.setOpaque(false);
		
		btnPosActionsRefresh = new JButton("");
		btnPosActionsRefresh.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/refresh.png")));
		
		btnPosActionsChart = new JButton("");
		btnPosActionsChart.setIcon(new ImageIcon(DesignPanelBase.class.getResource("/resources/icons/statistics2_16.png")));
		GroupLayout gl_posActionsPanel = new GroupLayout(posActionsPanel);
		gl_posActionsPanel.setHorizontalGroup(
			gl_posActionsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_posActionsPanel.createSequentialGroup()
					.addComponent(posActionsListPanel, GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_posActionsPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnPosActionsChart, 0, 0, Short.MAX_VALUE)
						.addComponent(btnPosActionsRefresh, GroupLayout.PREFERRED_SIZE, 42, Short.MAX_VALUE)))
		);
		gl_posActionsPanel.setVerticalGroup(
			gl_posActionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_posActionsPanel.createSequentialGroup()
					.addComponent(btnPosActionsRefresh)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPosActionsChart)
					.addContainerGap(147, Short.MAX_VALUE))
				.addComponent(posActionsListPanel, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
		);
		posActionsListPanel.setLayout(new BorderLayout(0, 0));
		
		postActionsListScroller = new JScrollPane();
		postActionsListScroller.setBorder(null);
		posActionsListPanel.add(postActionsListScroller);
		
		posActionsList = new JList();
		posActionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		posActionsList.setCellRenderer(new PossibleActionsRenderer());
		postActionsListScroller.setViewportView(posActionsList);
		posActionsPanel.setLayout(gl_posActionsPanel);
		actionsPanel.setLayout(gl_actionsPanel);
	}
	
	private static class PossibleActionsRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		private static final Icon actionIcon = new ImageIcon(PossibleActionsRenderer.class.getResource("/resources/icons/misc3_16.png"));
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			
			setIcon(actionIcon);
			
			return this;
		}
	}
}
