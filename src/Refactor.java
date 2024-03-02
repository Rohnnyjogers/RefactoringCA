import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	public JPanel createEmployeeDetailsPanel(EmployeeDetails employeeDetails) {
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
		
//		buttonPanel.add(saveChange = new JButton("Save"));
//		saveChange.addActionListener(this);
//		
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
			if (employeeDetailsPanel.getComponent(i) instanceof JTextField) {
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				field.setEditable(false);
				if (field == employeeDetails.ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener((DocumentListener) employeeDetails);
			} 
			else if (employeeDetailsPanel.getComponent(i) instanceof JComboBox && editable == true) {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
				employeeDetailsPanel.getComponent(i).setEnabled(false);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).addItemListener((ItemListener) employeeDetails);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}
				});
			}
			else {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
			}
		} 
	}
}
