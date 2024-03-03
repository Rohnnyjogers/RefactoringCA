/*
 * 
 * This is a dialog for searching Employees by their surname.
 * 
 * */

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchBySurnameDialog extends JDialog implements ActionListener, SearchInterface{
	EmployeeDetails employeeDetails;
	JButton searchBtn, cancelBtn;
	JTextField searchField;
	// constructor for search by surname dialog
	public SearchBySurnameDialog(EmployeeDetails employeeDetails) {
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
	}// end SearchBySurnameDialog
	
	// initialize search container
	@Override
	public Container searchPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3,1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;

		searchPanel.add(new JLabel("Search by Surname"));
	
		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter Surname:"));
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
		if(e.getSource() == searchBtn){
			this.employeeDetails.searchBySurnameField.setText(searchField.getText());
			// search Employee by surname
			this.employeeDetails.searchEmployeeBySurname();
			dispose();// dispose dialog
		}// end if
		// else dispose dialog
		else if(e.getSource() == cancelBtn)
			dispose();// dispose dialog
	}// end actionPerformed
}// end class SearchBySurnameDialog
