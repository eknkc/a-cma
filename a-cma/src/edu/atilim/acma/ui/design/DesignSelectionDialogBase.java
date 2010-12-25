package edu.atilim.acma.ui.design;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DesignSelectionDialogBase extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	protected JButton btnLoadDesign;
	protected JButton btnExit;
	protected JPanel panelPredefined;
	protected JComboBox comboBoxDesigns;
	protected JTextArea txtrPredefinedDesc;
	protected JPanel panel;
	protected JTextArea txtrCustomDesc;
	protected JTextField textFieldPath;
	protected JButton btnBrowse;
	protected Component horizontalStrut;

	/**
	 * Create the dialog.
	 */
	public DesignSelectionDialogBase() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("A-CMA Design Loader");
		setIconImage(Toolkit.getDefaultToolkit().getImage(DesignSelectionDialogBase.class.getResource("/resources/acmaicon.png")));
		setBounds(100, 100, 455, 303);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		panelPredefined = new JPanel();
		panelPredefined.setBorder(new TitledBorder(null, "Predefined Design", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPredefined.setBounds(10, 11, 429, 117);
		contentPanel.add(panelPredefined);
		panelPredefined.setLayout(null);
		
		comboBoxDesigns = new JComboBox();
		comboBoxDesigns.setBounds(10, 86, 409, 20);
		panelPredefined.add(comboBoxDesigns);
		
		txtrPredefinedDesc = new JTextArea();
		txtrPredefinedDesc.setForeground(SystemColor.textInactiveText);
		txtrPredefinedDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrPredefinedDesc.setBackground(SystemColor.control);
		txtrPredefinedDesc.setText("You can select a preloaded design from the list below. Also, you can place any zip\r\nfile containing the bin folder of any java project to the input/benchmarks folder if\r\nyou want them to be listed here.");
		txtrPredefinedDesc.setEditable(false);
		txtrPredefinedDesc.setRows(3);
		txtrPredefinedDesc.setBounds(10, 25, 409, 46);
		panelPredefined.add(txtrPredefinedDesc);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Custom Design", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 139, 429, 97);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		txtrCustomDesc = new JTextArea();
		txtrCustomDesc.setBounds(10, 25, 399, 39);
		txtrCustomDesc.setText("Browse for any folder containing compiled Java code. That would be a \"bin\" folder,\r\ngenerally...");
		txtrCustomDesc.setRows(3);
		txtrCustomDesc.setForeground(SystemColor.textInactiveText);
		txtrCustomDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrCustomDesc.setEditable(false);
		txtrCustomDesc.setBackground(SystemColor.menu);
		panel.add(txtrCustomDesc);
		
		textFieldPath = new JTextField();
		textFieldPath.setEditable(false);
		textFieldPath.setBounds(10, 64, 359, 23);
		panel.add(textFieldPath);
		textFieldPath.setColumns(10);
		
		btnBrowse = new JButton("...");
		btnBrowse.setBounds(379, 64, 40, 23);
		panel.add(btnBrowse);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(null);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			btnLoadDesign = new JButton("Load Design");
			buttonPane.add(btnLoadDesign);
			
			btnExit = new JButton("Exit");
			btnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			buttonPane.add(btnExit);
			
			horizontalStrut = Box.createHorizontalStrut(1);
			buttonPane.add(horizontalStrut);
		}
	}
}
