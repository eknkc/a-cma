package edu.atilim.acma.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import edu.atilim.acma.ui.design.MainWindowBase;

public class MainWindow extends MainWindowBase {
	private static final long serialVersionUID = 1L;
	
	private static MainWindow instance;
	private static Listener listener;
	
	public static MainWindow getInstance() {
		if (instance == null) instance = new MainWindow();
		return instance;
	}
	
	LoadedDesigns getLoadedDesigns() {
		return loadedDesigns;
	}
	
	JTabbedPane getTabs() {
		return mainTabs;
	}
	
	public static Listener getListener() {
		if (listener == null) listener = new Listener();
		return listener;
	}
	
	private static class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(Actions.DESIGN_LOAD)) {
				new LoadDesignDialog().setVisible(true);
			}
		}
	}
}
