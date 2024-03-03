import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

public class Refactor {
	
	/*Font for labels, text fields and combo boxes within
	 *an employee details panel.*/
	Font font = new Font("SansSerif", Font.BOLD, 16);
	
	/* A function that returns a JPanel populated with fields used to
	 * collect employee data. The JPanel is configured in a mig layout
	 * and includes a button JPanel for saving data or canceling a data 
	 * entry. */ 
	public JPanel createEmployeeDetailsPanel(	EmployeeDetails employeeDetails) {
		JPanel employeeDetailsPanel = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();

		employeeDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		employeeDetailsPanel.add(new JLabel("ID:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.idField = new JTextField(20), "growx, pushx, wrap");
		employeeDetails.idField.setEditable(false);
		

		employeeDetailsPanel.add(new JLabel("PPS Number:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.ppsField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Surname:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.surnameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("First Name:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.firstNameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Gender:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.genderComboBox = new JComboBox<String>(employeeDetails.gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.deptComboBox = new JComboBox<String>(employeeDetails.department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(employeeDetails.fullTimeComboBox = new JComboBox<String>(employeeDetails.fullTime), "growx, pushx, wrap");
		
		buttonPanel.add(employeeDetails.saveBtn = new JButton("Save"));
		employeeDetails.saveBtn.addActionListener(employeeDetails);
		employeeDetails.saveBtn.setVisible(false);
		employeeDetails.saveBtn.setToolTipText("Save changes");
		buttonPanel.add(employeeDetails.cancelBtn = new JButton("Cancel"));
		employeeDetails.cancelBtn.addActionListener(employeeDetails);
		employeeDetails.cancelBtn.setVisible(false);
		employeeDetails.cancelBtn.setToolTipText("Cancel edit");
		
		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");
	
		return employeeDetailsPanel;
	}
	
	public JPanel createEmployeeDetailsPanel(AddRecordDialog addRecordDetails) {
		JPanel employeeDetailsPanel = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();

		employeeDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		employeeDetailsPanel.add(new JLabel("ID:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.idField = new JTextField(20), "growx, pushx, wrap");
		addRecordDetails.idField.setEditable(false);
		

		employeeDetailsPanel.add(new JLabel("PPS Number:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.ppsField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Surname:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.surnameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("First Name:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.firstNameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Gender:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.genderComboBox = new JComboBox<String>(addRecordDetails.employeeDetails.gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.deptComboBox = new JComboBox<String>(addRecordDetails.employeeDetails.department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(addRecordDetails.fullTimeComboBox = new JComboBox<String>(addRecordDetails.employeeDetails.fullTime), "growx, pushx, wrap");
		
		buttonPanel.add(addRecordDetails.saveBtn = new JButton("Save"));
		addRecordDetails.saveBtn.addActionListener(addRecordDetails);
		addRecordDetails.saveBtn.requestFocus();
		buttonPanel.add(addRecordDetails.cancelBtn = new JButton("Cancel"));
		addRecordDetails.cancelBtn.addActionListener(addRecordDetails);
		
		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");
	
		return employeeDetailsPanel;
	}
	
	/*A method that iterates through each component present in the
	 *JPanel parameter passed, formats each field and attaches listeners. */
	public void addStyleAndListeners(	JPanel employeeDetailsPanel, 
										EmployeeDetails employeeDetails,
										boolean editable
										) {
		JTextField field;
		
		for (int i = 0; i < employeeDetailsPanel.getComponentCount(); i++) {
			employeeDetailsPanel.getComponent(i).setFont(font);
			if (employeeDetailsPanel.getComponent(i) instanceof JTextField && !editable) {
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				field.setEditable(false);
				if (field == employeeDetails.ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(employeeDetails);
			} 
			else if (employeeDetailsPanel.getComponent(i) instanceof JComboBox && !editable) {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
				employeeDetailsPanel.getComponent(i).setEnabled(false);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).addItemListener(employeeDetails);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}
				});
			}
			else if(employeeDetailsPanel.getComponent(i) instanceof JComboBox){
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
			}
			else if(employeeDetailsPanel.getComponent(i) instanceof JComboBox){
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				if(field == employeeDetails.ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
				field.setDocument(new JTextFieldLimit(20));
			}
		}
	}
}
