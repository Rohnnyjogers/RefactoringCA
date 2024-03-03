/*
 * 
 * This is the dialog for Employee search by ID
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchByIdDialog extends JDialog implements ActionListener, SearchInterface {
	EmployeeDetails employeeDetails;
	JButton searchBtn, cancelBtn;
	JTextField searchField;
	// constructor for SearchByIdDialog 
	public SearchByIdDialog(EmployeeDetails employeeDetails) {
		setTitle("Search by Surname");
		setModal(true);
		this.employeeDetails = employeeDetails;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(searchPane());
		setContentPane(scrollPane);

		getRootPane().setDefaultButton(searchBtn);
		
		setSize(500, 190);
		setLocation(350, 250);
		setVisible(true);
	}// end SearchByIdDialog
	
	// initialize search container
	@Override
	public Container searchPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3, 1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;

		searchPanel.add(new JLabel("Search by ID"));

		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter ID:"));
		searchLabel.setFont(this.employeeDetails.font);
		textPanel.add(searchField = new JTextField(20));
		searchField.setFont(this.employeeDetails.font);
		searchField.setDocument(new JTextFieldLimit(20));
		
		buttonPanel.add(searchBtn = new JButton("Search"));
		searchBtn.addActionListener(this);
		searchBtn.requestFocus();
		
		buttonPanel.add(cancelBtn = new JButton("Cancel"));
		cancelBtn.addActionListener(this);

		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);

		return searchPanel;
	}// end searchPane

	// action listener for save and cancel button
	@Override
	public void actionPerformed(ActionEvent e) {
		// if option search, search for Employee
		if (e.getSource() == searchBtn) {
			// try get correct valus from text field
			try {
				Double.parseDouble(searchField.getText());
				this.employeeDetails.searchByIdField.setText(searchField.getText());
				// search Employee by ID
				this.employeeDetails.searchEmployeeById();
				dispose();// dispose dialog
			}// end try
			catch (NumberFormatException num) {
				// display message and set colour to text field if entry is wrong
				searchField.setBackground(new Color(255, 150, 150));
				JOptionPane.showMessageDialog(null, "Wrong ID format!");
			}// end catch
		}// end if
		// else dispose dialog
		else if (e.getSource() == cancelBtn)
			dispose();
	}// end actionPerformed
}// end class searchByIdDialog
