package edu.atilim.acma.ui.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import edu.atilim.acma.ui.Actions;
import edu.atilim.acma.ui.Console;
import edu.atilim.acma.ui.LoadedDesigns;
import edu.atilim.acma.ui.MainWindow;
import edu.atilim.acma.ui.TasksPanel;
import java.awt.Component;
import javax.swing.Box;

public class MainWindowBase extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	protected JPanel leftPanel;
	protected LoadedDesigns loadedDesigns;
	protected TasksPanel tasksPanel;
	protected JPanel topPanel;
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenu menu_1;
	protected JToolBar toolBar;
	protected JButton btnLoad;
	protected JPanel mainPanel;
	protected JPanel rightPanel;
	protected JTabbedPane mainTabs;
	protected Console console;
	protected JButton btnConfigureMetrics;
	protected JButton btnConfigureActions;
	protected Component horizontalGlue;
	protected JButton btnAbout;

	public MainWindowBase() {
		setTitle("A-CMA");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindowBase.class.getResource("/resources/acmaicon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		
		leftPanel = new JPanel();
		leftPanel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
		
		loadedDesigns = new LoadedDesigns();
		
		tasksPanel = new TasksPanel();
		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tasksPanel, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
				.addComponent(loadedDesigns, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
		);
		gl_leftPanel.setVerticalGroup(
			gl_leftPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addComponent(loadedDesigns, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tasksPanel, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
		);
		leftPanel.setLayout(gl_leftPanel);
		
		rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, 356, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
				.addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
		);
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		mainTabs = new JTabbedPane(JTabbedPane.TOP);
		rightPanel.add(mainTabs, BorderLayout.CENTER);
		
		console = new Console();
		mainTabs.addTab("Console", new ImageIcon(MainWindowBase.class.getResource("/resources/icons/note.png")), console, null);
		mainPanel.setLayout(gl_mainPanel);
		
		topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		topPanel.setLayout(new BorderLayout(0, 0));
		
		menuBar = new JMenuBar();
		topPanel.add(menuBar, BorderLayout.NORTH);
		
		menu = new JMenu("New menu");
		menuBar.add(menu);
		
		menu_1 = new JMenu("New menu");
		menuBar.add(menu_1);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		topPanel.add(toolBar, BorderLayout.SOUTH);
		
		btnLoad = new JButton("Load Design");
		btnLoad.setActionCommand(Actions.DESIGN_LOAD);
		btnLoad.addActionListener(MainWindow.getListener());
		btnLoad.setIcon(new ImageIcon(MainWindowBase.class.getResource("/resources/icons/java/design.gif")));
		toolBar.add(btnLoad);
		
		btnConfigureMetrics = new JButton("Configure Metrics");
		btnConfigureMetrics.setIcon(new ImageIcon(MainWindowBase.class.getResource("/resources/icons/statistics2_16.png")));
		toolBar.add(btnConfigureMetrics);
		
		btnConfigureActions = new JButton("Configure Actions");
		btnConfigureActions.setIcon(new ImageIcon(MainWindowBase.class.getResource("/resources/icons/engine_16.png")));
		toolBar.add(btnConfigureActions);
		
		horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		
		btnAbout = new JButton("About");
		btnAbout.setIcon(new ImageIcon(MainWindowBase.class.getResource("/resources/icons/info_16.png")));
		toolBar.add(btnAbout);
	}
}
