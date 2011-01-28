package edu.atilim.acma.ui.design;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class RunConfigPanelBase extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JPanel pnlPreset;
	protected JList presetList;
	protected JPanel pnlPresetButtons;
	protected JButton btnAddPreset;
	protected JButton btnDeletePreset;
	protected JTabbedPane tabbedPane;
	protected JPanel panel;
	protected JPanel panel_1;

	public RunConfigPanelBase() {
		setOpaque(false);
		pnlPreset = new JPanel();
		pnlPreset.setOpaque(false);
		pnlPreset.setBorder(new TitledBorder(null, "Preset", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(pnlPreset, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
						.addComponent(pnlPreset, GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
					.addGap(0))
		);
		
		panel = new JPanel();
		panel.setOpaque(false);
		tabbedPane.addTab("Metric Configuration", new ImageIcon(RunConfigPanelBase.class.getResource("/resources/icons/statistics2_16.png")), panel, null);
		
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		tabbedPane.addTab("Action Configuration", new ImageIcon(RunConfigPanelBase.class.getResource("/resources/icons/engine_16.png")), panel_1, null);
		pnlPreset.setLayout(new BorderLayout(0, 0));
		
		presetList = new JList();
		presetList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlPreset.add(presetList, BorderLayout.CENTER);
		
		pnlPresetButtons = new JPanel();
		FlowLayout fl_pnlPresetButtons = (FlowLayout) pnlPresetButtons.getLayout();
		fl_pnlPresetButtons.setAlignment(FlowLayout.RIGHT);
		pnlPreset.add(pnlPresetButtons, BorderLayout.SOUTH);
		
		btnAddPreset = new JButton("");
		btnAddPreset.setIcon(new ImageIcon(RunConfigPanelBase.class.getResource("/resources/icons/add.png")));
		pnlPresetButtons.add(btnAddPreset);
		
		btnDeletePreset = new JButton("");
		btnDeletePreset.setIcon(new ImageIcon(RunConfigPanelBase.class.getResource("/resources/icons/delete.png")));
		pnlPresetButtons.add(btnDeletePreset);
		setLayout(groupLayout);

	}
}
