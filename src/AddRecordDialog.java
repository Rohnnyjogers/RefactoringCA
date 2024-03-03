/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends JDialog implements ActionListener, EmployeeDetailsInterface {
	protected JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	protected JComboBox<String> genderComboBox, deptComboBox, fullTimeComboBox;
	JButton saveBtn, cancelBtn;
	EmployeeDetails employeeDetails;
	Refactor refactor = new Refactor();
	// constructor for add record dialog
	public AddRecordDialog(EmployeeDetails employeeDetails) {
		setTitle("Add Record");
		setModal(true);
		this.employeeDetails = employeeDetails;
		this.employeeDetails.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane(dialogPane());
		setContentPane(scrollPane);
		
		getRootPane().setDefaultButton(saveBtn);
		
		setSize(500, 370);
		setLocation(350, 250);
		setVisible(true);
	}// end AddRecordDialog

	// initialize dialog container
	public Container dialogPane() {
		JPanel employeeDetailsPanel = createEmployeeDetailsPanel();
		formatFields(employeeDetailsPanel);
		idField.setText(Integer.toString(this.employeeDetails.getNextFreeId()));
		return employeeDetailsPanel;
	}
	
	@Override
	public JPanel createEmployeeDetailsPanel() {
		JPanel employeeDetailsPanel = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();

		employeeDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		employeeDetailsPanel.add(new JLabel("ID:"), "growx, pushx");
		employeeDetailsPanel.add(idField = new JTextField(20), "growx, pushx, wrap");
		idField.setEditable(false);
		

		employeeDetailsPanel.add(new JLabel("PPS Number:"), "growx, pushx");
		employeeDetailsPanel.add(ppsField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Surname:"), "growx, pushx");
		employeeDetailsPanel.add(surnameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("First Name:"), "growx, pushx");
		employeeDetailsPanel.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Gender:"), "growx, pushx");
		employeeDetailsPanel.add(genderComboBox = new JComboBox<String>(employeeDetails.gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(deptComboBox = new JComboBox<String>(employeeDetails.department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(fullTimeComboBox = new JComboBox<String>(employeeDetails.fullTime), "growx, pushx, wrap");
		
		buttonPanel.add(saveBtn = new JButton("Save"));
		saveBtn.addActionListener(this);
		saveBtn.requestFocus();
		buttonPanel.add(cancelBtn = new JButton("Cancel"));
		cancelBtn.addActionListener(this);
		
		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");
	
		return employeeDetailsPanel;
	}
	
	@Override
	public void formatFields(JPanel employeeDetailsPanel){
		JTextField field;
		
		for (int i = 0; i < employeeDetailsPanel.getComponentCount(); i++) {
			employeeDetailsPanel.getComponent(i).setFont(this.employeeDetails.font);
			if (employeeDetailsPanel.getComponent(i) instanceof JComboBox) {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
			}// end if
			else if(employeeDetailsPanel.getComponent(i) instanceof JTextField){
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				if(field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
				field.setDocument(new JTextFieldLimit(20));
			}// end else if
		}// end for
	}

	// add record to file
	public void addRecord() {
		boolean fullTime = false;
		Employee employee;

		if (((String) fullTimeComboBox.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		employee = new Employee(
				Integer.parseInt(idField.getText()), 
				ppsField.getText().toUpperCase(), 
				surnameField.getText().toUpperCase(),
				firstNameField.getText().toUpperCase(),
				genderComboBox.getSelectedItem().toString().charAt(0),
				deptComboBox.getSelectedItem().toString(), 
				Double.parseDouble(salaryField.getText()), 
				fullTime);
		this.employeeDetails.currentEmployee = employee;
		this.employeeDetails.addRecord(employee);
		this.employeeDetails.displayRecords(employee);
	}

	// check for input in text fields
	@Override
	public boolean checkInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display message
		if (ppsField.getText().equals("")) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		if (this.employeeDetails.correctPps(this.ppsField.getText().trim(), -1)) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		if (surnameField.getText().isEmpty()) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		if (firstNameField.getText().isEmpty()) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		if (genderComboBox.getSelectedIndex() == 0) {
			genderComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		if (deptComboBox.getSelectedIndex() == 0) {
			deptComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			}// end if
		}// end try
		catch (NumberFormatException num) {
			salaryField.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end catch
		if (fullTimeComboBox.getSelectedIndex() == 0) {
			fullTimeComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		}// end if
		return valid;
	}// end checkInput

	// set text field to white colour
	@Override
	public void setToWhite() {
		ppsField.setBackground(Color.WHITE);
		surnameField.setBackground(Color.WHITE);
		firstNameField.setBackground(Color.WHITE);
		salaryField.setBackground(Color.WHITE);
		genderComboBox.setBackground(Color.WHITE);
		deptComboBox.setBackground(Color.WHITE);
		fullTimeComboBox.setBackground(Color.WHITE);
	}// end setToWhite

	// action performed
	@Override
	public void actionPerformed(ActionEvent e) {
		// if chosen option save, save record to file
		if (e.getSource() == saveBtn) {
			// if inputs correct, save record
			if (checkInput()) {
				addRecord();// add record to file
				dispose();// dispose dialog
				this.employeeDetails.fileChange = true;
			}// end if
			// else display message and set text fields to white colour
			else {
				JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
				setToWhite();
			}// end else
		}// end if
		else if (e.getSource() == cancelBtn)
			dispose();// dispose dialog
	}// end actionPerformed
}// end class AddRecordDialog