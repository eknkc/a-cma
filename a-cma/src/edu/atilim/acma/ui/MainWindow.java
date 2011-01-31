package edu.atilim.acma.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import edu.atilim.acma.ui.design.MainWindowBase;
import edu.atilim.acma.util.WeakHashSet;

public class MainWindow extends MainWindowBase {
	private static final long serialVersionUID = 1L;
	
	private static MainWindow instance;
	private static Listener listener;
	
	public static MainWindow getInstance() {
		if (instance == null) instance = new MainWindow();
		return instance;
	}
	
	private WeakHashSet<WindowEventListener> eventListeners;
	
	void addEventListener(WindowEventListener listener) {
		eventListeners.add(listener);
	}
	
	void fireEvent(Object e) {
		if (e == null) return;
		for (WindowEventListener listener : eventListeners)
			listener.onWindowEvent(e);
	}
	
	public MainWindow() {
		eventListeners = new WeakHashSet<MainWindow.WindowEventListener>();
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
			String ac = e.getActionCommand();
			
			if (ac.equals(Actions.DESIGN_LOAD)) {
				new LoadDesignDialog().setVisible(true);
			} else if (ac.equals(Actions.EXIT)) {
				System.exit(0);
			} else if (ac.equals(Actions.CLEAR_CONSOLE)) {
				MainWindow.getInstance().console.clear();
			} else if (ac.equals(Actions.CONFIG_RUN)) {
				new RunConfigDialog().setVisible(true);
			}
		}
	}
	
	public static interface WindowEventListener {
		public void onWindowEvent(Object e);
	}
}
