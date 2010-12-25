package edu.atilim.acma.ui.design;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class PleaseWaitDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	protected JPanel panel;
	protected JLabel lblPleaseWait;

	/**
	 * Create the dialog.
	 */
	public PleaseWaitDialog() {
		setUndecorated(true);
		setTitle("Working");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 163, 75);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblPleaseWait = new JLabel("Please wait...");
		lblPleaseWait.setIcon(new ImageIcon(PleaseWaitDialog.class.getResource("/resources/icons/button-yellow.png")));
		lblPleaseWait.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseWait.setBounds(10, 11, 143, 53);
		panel.add(lblPleaseWait);

	}

}
