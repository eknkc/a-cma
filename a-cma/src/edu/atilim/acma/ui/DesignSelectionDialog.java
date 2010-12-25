package edu.atilim.acma.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.DesignReader;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.ui.design.DesignSelectionDialogBase;
import edu.atilim.acma.ui.design.PleaseWaitDialog;
import edu.atilim.acma.util.Delegate.Void;

public class DesignSelectionDialog extends DesignSelectionDialogBase {
	private static final long serialVersionUID = 1L;
	
	private Void onDesignLoad;
	private Design loadedDesign;
	
	public Design getLoadedDesign() {
		return loadedDesign;
	}

	public void setOnDesignLoad(Void onDesignLoad) {
		this.onDesignLoad = onDesignLoad;
	}

	public static Design loadDesign() {
		final DesignSelectionDialog dsd = new DesignSelectionDialog();
		dsd.setVisible(true);
		
		while(true) {
			if (!dsd.isVisible() && dsd.loadedDesign == null)
				System.exit(0);
			if (dsd.loadedDesign != null)
				return dsd.loadedDesign;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public DesignSelectionDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		File bmdir = new File("./data/benchmarks");
		
		comboBoxDesigns.addItem(new PredefinedDesign(null));
		
		for (File f : bmdir.listFiles()) {
			if (!f.isDirectory() && f.getName().endsWith(".zip")) {
				comboBoxDesigns.addItem(new PredefinedDesign(f));
				textFieldPath.setText("");
			}
		}
		
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setDialogTitle("Please select the bin directory of Java project");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				
				if (fc.showOpenDialog(DesignSelectionDialog.this) == JFileChooser.APPROVE_OPTION) {
					textFieldPath.setText(fc.getSelectedFile().getAbsolutePath());
					comboBoxDesigns.setSelectedIndex(0);
				}
			}
		});
		
		btnLoadDesign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final PleaseWaitDialog pwd = new PleaseWaitDialog();
				pwd.setLocationRelativeTo(DesignSelectionDialog.this);
				
				if (comboBoxDesigns.getSelectedIndex() != 0) {
					DesignSelectionDialog.this.setEnabled(false);
					pwd.setVisible(true);
					
					PredefinedDesign design = (PredefinedDesign)comboBoxDesigns.getSelectedItem();
					final ZIPDesignReader dr = new ZIPDesignReader(design.getFile().getAbsolutePath());
					
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							loadedDesign = dr.read();
							
							if (onDesignLoad != null)
								onDesignLoad.run();
							
							pwd.setVisible(false);
							setVisible(false);
						}
					});
				} else if (textFieldPath.getText() != null && !textFieldPath.getText().equals("")) {	
					File file = new File(textFieldPath.getText());
					if (file.exists() && file.isDirectory()) {
						DesignSelectionDialog.this.setEnabled(false);
						pwd.setVisible(true);
						
						final DesignReader dr = new DesignReader(file.getAbsolutePath());
						
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								loadedDesign = dr.read();
								
								if (onDesignLoad != null)
									onDesignLoad.run();
								
								pwd.setVisible(false);
								setVisible(false);
							}
						});
					}
				} else {
					JOptionPane.showMessageDialog(DesignSelectionDialog.this, "Please select a design first.");
				}
			}
		});
	}
	
	private class PredefinedDesign {
		private File file;
		
		public File getFile() {
			return file;
		}

		public PredefinedDesign(File file) {
			this.file = file;
		}

		@Override
		public String toString() {
			if (file == null)
				return "Select Design";
			
			return file.getName();
		}
	}
}
