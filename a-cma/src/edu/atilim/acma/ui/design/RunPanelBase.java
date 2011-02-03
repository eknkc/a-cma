package edu.atilim.acma.ui.design;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RunPanelBase extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTabbedPane tabbedPane;
	protected JPanel runPanel;
	protected JPanel statusPanel;
	protected JProgressBar progressBar;
	protected JButton pauseContinueButton;
	protected JPanel logPanel;
	protected JScrollPane scrollPane;
	protected JTextArea textArea;

	public RunPanelBase() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		runPanel = new JPanel();
		runPanel.setOpaque(false);
		tabbedPane.addTab("Refactoring Run", new ImageIcon(RunPanelBase.class.getResource("/resources/icons/info_16.png")), runPanel, null);
		runPanel.setLayout(new BorderLayout(0, 0));
		
		statusPanel = new JPanel();
		statusPanel.setOpaque(false);
		statusPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0))));
		runPanel.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setLayout(new BorderLayout(5, 5));
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		statusPanel.add(progressBar);
		
		pauseContinueButton = new JButton("");
		pauseContinueButton.setIcon(new ImageIcon(RunPanelBase.class.getResource("/resources/icons/play_16.png")));
		statusPanel.add(pauseContinueButton, BorderLayout.EAST);
		
		logPanel = new JPanel();
		logPanel.setOpaque(false);
		logPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Run Log", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0))));
		runPanel.add(logPanel, BorderLayout.CENTER);
		logPanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		logPanel.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

	}
}
