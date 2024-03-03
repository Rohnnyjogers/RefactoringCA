
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener, RefactorInterface {
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	// hold object start position in file
	private long currentByteStart = 0;
	
	private RandomFile application = new RandomFile();
	// display files in File Chooser only with extension .dat
	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	// hold file name and path for current file in use
	private File file;
	// holds true or false if any changes are made for text fields
	private boolean textFieldChange = false;
	// holds true or false if any changes are made for file content
	boolean fileChange = false;
	
	private JMenuItem	openMenuItm, 
						saveMenuItm, 
						saveAsMenuItm, 
						createMenuItm, 
						modifyMenuItm, 
						deleteMenuItm, 
						firstMenuItm, 
						lastMenuItm, 
						nextMenuItm, 
						prevMenuItm, 
						searchByIdMenuItm,
						searchBySurnameMenuItm, 
						listAllMenuItm, 
						closeAppMenuItm;
	
	protected JButton firstBtn, 
					previousBtn, 
					nextBtn, 
					lastBtn, 
					addBtn, 
					editBtn, 
					deleteBtn, 
					displayAllBtn, 
					searchIdBtn, 
					searchSurnameBtn,
					saveBtn, 
					cancelBtn;
	
	protected JComboBox<String>	genderComboBox, deptComboBox, fullTimeComboBox;
	
	protected JTextField	idField, 
							surnameField, 
							firstNameField, 
							salaryField, 
							ppsField, 
							searchByIdField, 
							searchBySurnameField;
	
	private static EmployeeDetails frame = new EmployeeDetails();
	/*Font for labels, text fields and combo boxes within
	 *an employee details panel.*/
	protected Font font = new Font("SansSerif", Font.BOLD, 16);
	// holds automatically generated file name
	protected String generatedFileName;
	// holds current Employee object
	protected Employee currentEmployee;
	// gender combo box values
	protected String[] gender = { "", "M", "F" };
	// department combo box values
	protected String[] department = { "", "Administration", "Production", "Transport", "Management" };
	// full time combo box values
	protected String[] fullTime = { "", "Yes", "No" };

	Refactor refactor = new Refactor();
	
	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(openMenuItm = new JMenuItem("Open")).addActionListener(this);
		openMenuItm.setMnemonic(KeyEvent.VK_O);
		openMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(saveMenuItm = new JMenuItem("Save")).addActionListener(this);
		saveMenuItm.setMnemonic(KeyEvent.VK_S);
		saveMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAsMenuItm = new JMenuItem("Save As")).addActionListener(this);
		saveAsMenuItm.setMnemonic(KeyEvent.VK_F2);
		saveAsMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(createMenuItm = new JMenuItem("Create new Record")).addActionListener(this);
		createMenuItm.setMnemonic(KeyEvent.VK_N);
		createMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modifyMenuItm = new JMenuItem("Modify Record")).addActionListener(this);
		modifyMenuItm.setMnemonic(KeyEvent.VK_E);
		modifyMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(deleteMenuItm = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstMenuItm = new JMenuItem("First"));
		firstMenuItm.addActionListener(this);
		navigateMenu.add(prevMenuItm = new JMenuItem("Previous"));
		prevMenuItm.addActionListener(this);
		navigateMenu.add(nextMenuItm = new JMenuItem("Next"));
		nextMenuItm.addActionListener(this);
		navigateMenu.add(lastMenuItm = new JMenuItem("Last"));
		lastMenuItm.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchByIdMenuItm = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurnameMenuItm = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAllMenuItm = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeAppMenuItm = new JMenuItem("Close")).addActionListener(this);
		closeAppMenuItm.setMnemonic(KeyEvent.VK_F4);
		closeAppMenuItm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchIdBtn = new JButton("Go"),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchIdBtn.addActionListener(this);
		searchIdBtn.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurnameBtn = new JButton("Go"),"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurnameBtn.addActionListener(this);
		searchSurnameBtn.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigatePanel() {
		JPanel navigatePanel = new JPanel();

		navigatePanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigatePanel.add(firstBtn = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		firstBtn.setPreferredSize(new Dimension(17, 17));
		firstBtn.addActionListener(this);
		firstBtn.setToolTipText("Display first Record");

		navigatePanel.add(previousBtn = new JButton(new ImageIcon(new ImageIcon("prev.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previousBtn.setPreferredSize(new Dimension(17, 17));
		previousBtn.addActionListener(this);
		previousBtn.setToolTipText("Display next Record");

		navigatePanel.add(nextBtn = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		nextBtn.setPreferredSize(new Dimension(17, 17));
		nextBtn.addActionListener(this);
		nextBtn.setToolTipText("Display previous Record");

		navigatePanel.add(lastBtn = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		lastBtn.setPreferredSize(new Dimension(17, 17));
		lastBtn.addActionListener(this);
		lastBtn.setToolTipText("Display last Record");

		return navigatePanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(addBtn = new JButton("Add Record"), "growx, pushx");
		addBtn.addActionListener(this);
		addBtn.setToolTipText("Add new Employee Record");
		buttonPanel.add(editBtn = new JButton("Edit Record"), "growx, pushx");
		editBtn.addActionListener(this);
		editBtn.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteBtn = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteBtn.addActionListener(this);
		deleteBtn.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAllBtn = new JButton("List all Records"), "growx, pushx");
		displayAllBtn.addActionListener(this);
		displayAllBtn.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel employeeDetailsPanel = createEmployeeDetailsPanel();
		formatFields(employeeDetailsPanel);
		
		return employeeDetailsPanel;
	}// end detailsPanel
	
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
		employeeDetailsPanel.add(genderComboBox = new JComboBox<String>(gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(deptComboBox = new JComboBox<String>(department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(fullTimeComboBox = new JComboBox<String>(fullTime), "growx, pushx, wrap");
		
		buttonPanel.add(saveBtn = new JButton("Save"));
		saveBtn.addActionListener(this);
		saveBtn.setVisible(false);
		saveBtn.setToolTipText("Save changes");
		buttonPanel.add(cancelBtn = new JButton("Cancel"));
		cancelBtn.addActionListener(this);
		cancelBtn.setVisible(false);
		cancelBtn.setToolTipText("Cancel edit");
		
		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");
	
		return employeeDetailsPanel;
	}
	
	@Override
	public void formatFields(JPanel employeeDetailsPanel) {
		JTextField field;
		
		for (int i = 0; i < employeeDetailsPanel.getComponentCount(); i++) {
		employeeDetailsPanel.getComponent(i).setFont(font);
		if (employeeDetailsPanel.getComponent(i) instanceof JTextField) {
			field = (JTextField) employeeDetailsPanel.getComponent(i);
			field.setEditable(false);
			if (field == ppsField)
				field.setDocument(new JTextFieldLimit(9));
			else
				field.setDocument(new JTextFieldLimit(20));
			field.getDocument().addDocumentListener(this);
		} // end if
		else if (employeeDetailsPanel.getComponent(i) instanceof JComboBox) {
			employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
			employeeDetailsPanel.getComponent(i).setEnabled(false);
			((JComboBox<String>) employeeDetailsPanel.getComponent(i)).addItemListener(this);
			((JComboBox<String>) employeeDetailsPanel.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
				// set foreground to combo boxes
				public void paint(Graphics g) {
					setForeground(new Color(65, 65, 65));
					super.paint(g);
				}// end paint
			});
		} // end else if
	} // end for
	}

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
		int countGender = 0;
		int countDep = 0;
		boolean found = false;

		searchByIdField.setText("");
		searchBySurnameField.setText("");
		// if Employee is null or ID is 0 do nothing else display Employee
		// details
		if (thisEmployee == null) {
		} else if (thisEmployee.getEmployeeId() == 0) {
		} else {
			// find corresponding gender combo box value to current employee
			while (!found && countGender < gender.length - 1) {
				if (Character.toString(thisEmployee.getGender()).equalsIgnoreCase(gender[countGender]))
					found = true;
				else
					countGender++;
			} // end while
			found = false;
			// find corresponding department combo box value to current employee
			while (!found && countDep < department.length - 1) {
				if (thisEmployee.getDepartment().trim().equalsIgnoreCase(department[countDep]))
					found = true;
				else
					countDep++;
			} // end while
			idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
			ppsField.setText(thisEmployee.getPps().trim());
			surnameField.setText(thisEmployee.getSurname().trim());
			firstNameField.setText(thisEmployee.getFirstName());
			genderComboBox.setSelectedIndex(countGender);
			deptComboBox.setSelectedIndex(countDep);
			salaryField.setText(format.format(thisEmployee.getSalary()));
			// set corresponding full time combo box value to current employee
			if (thisEmployee.getFullTime() == true)
				fullTimeComboBox.setSelectedIndex(1);
			else
				fullTimeComboBox.setSelectedIndex(2);
		}
		textFieldChange = false;
	}// end display records

	// display Employee summary dialog
	private void displayEmployeeSummaryDialog() {
		// display Employee summary dialog if these is someone to display
		if (recordValid())
			new EmployeeSummaryDialog(getAllEmloyees());
	}// end displaySummaryDialog

	// display search by ID dialog
	private void displaySearchByIdDialog() {
		if (recordValid())
			new SearchByIdDialog(EmployeeDetails.this);
	}// end displaySearchByIdDialog

	// display search by surname dialog
	private void displaySearchBySurnameDialog() {
		if (recordValid())
			new SearchBySurnameDialog(EmployeeDetails.this);
	}// end displaySearchBySurnameDialog

	// find byte start in file for first active record
	private void firstRecord() {
		// if any active record in file look for first record
		if (recordValid()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for first record
			currentByteStart = application.getFirst();
			// assign current Employee to first record in file
			currentEmployee = application.readRecords(currentByteStart);
			application.closeReadFile();// close file for reading
			// if first record is inactive look for next record
			if (currentEmployee.getEmployeeId() == 0)
				nextRecord();// look for next record
		} // end if
	}// end firstRecord

	// find byte start in file for previous active record
	private void previousRecord() {
		// if any active record in file look for first record
		if (recordValid()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for previous record
			currentByteStart = application.getPrevious(currentByteStart);
			// assign current Employee to previous record in file
			currentEmployee = application.readRecords(currentByteStart);
			// loop to previous record until Employee is active - ID is not 0
			while (currentEmployee.getEmployeeId() == 0) {
				// get byte start in file for previous record
				currentByteStart = application.getPrevious(currentByteStart);
				// assign current Employee to previous record in file
				currentEmployee = application.readRecords(currentByteStart);
			} // end while
			application.closeReadFile();// close file for reading
		}
	}// end previousRecord

	// find byte start in file for next active record
	private void nextRecord() {
		// if any active record in file look for first record
		if (recordValid()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for next record
			currentByteStart = application.getNext(currentByteStart);
			// assign current Employee to record in file
			currentEmployee = application.readRecords(currentByteStart);
			// loop to previous next until Employee is active - ID is not 0
			while (currentEmployee.getEmployeeId() == 0) {
				// get byte start in file for next record
				currentByteStart = application.getNext(currentByteStart);
				// assign current Employee to next record in file
				currentEmployee = application.readRecords(currentByteStart);
			} // end while
			application.closeReadFile();// close file for reading
		} // end if
	}// end nextRecord

	// find byte start in file for last active record
	private void lastRecord() {
		// if any active record in file look for first record
		if (recordValid()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for last record
			currentByteStart = application.getLast();
			// assign current Employee to first record in file
			currentEmployee = application.readRecords(currentByteStart);
			application.closeReadFile();// close file for reading
			// if last record is inactive look for previous record
			if (currentEmployee.getEmployeeId() == 0)
				previousRecord();// look for previous record
		} // end if
	}// end lastRecord

	// search Employee by ID
	public void searchEmployeeById() {
		boolean found = false;

		try {// try to read correct correct from input
				// if any active Employee record search for ID else do nothing
			if (recordValid()) {
				firstRecord();// look for first record
				int firstId = currentEmployee.getEmployeeId();
				// if ID to search is already displayed do nothing else loop
				// through records
				if (searchByIdField.getText().trim().equals(idField.getText().trim()))
					found = true;
				else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
					found = true;
					displayRecords(currentEmployee);
				} // end else if
				else {
					nextRecord();// look for next record
					// loop until Employee found or until all Employees have
					// been checked
					while (firstId != currentEmployee.getEmployeeId()) {
						// if found break from loop and display Employee details
						// else look for next record
						if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
							found = true;
							displayRecords(currentEmployee);
							break;
						} else
							nextRecord();// look for next record
					} // end while
				} // end else
					// if Employee not found display message
				if (!found)
					JOptionPane.showMessageDialog(null, "Employee not found!");
			} // end if
		} // end try
		catch (NumberFormatException e) {
			searchByIdField.setBackground(new Color(255, 150, 150));
			JOptionPane.showMessageDialog(null, "Wrong ID format!");
		} // end catch
		searchByIdField.setBackground(Color.WHITE);
		searchByIdField.setText("");
	}// end searchEmployeeByID

	// search Employee by surname
	public void searchEmployeeBySurname() {
		boolean found = false;
		// if any active Employee record search for ID else do nothing
		if (recordValid()) {
			firstRecord();// look for first record
			String firstSurname = currentEmployee.getSurname().trim();
			// if ID to search is already displayed do nothing else loop through
			// records
			if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
				found = true;
			else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
				found = true;
				displayRecords(currentEmployee);
			} // end else if
			else {
				nextRecord();// look for next record
				// loop until Employee found or until all Employees have been
				// checked
				while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
					// if found break from loop and display Employee details
					// else look for next record
					if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
						found = true;
						displayRecords(currentEmployee);
						break;
					} // end if
					else
						nextRecord();// look for next record
				} // end while
			} // end else
				// if Employee not found display message
			if (!found)
				JOptionPane.showMessageDialog(null, "Employee not found!");
		} // end if
		searchBySurnameField.setText("");
	}// end searchEmployeeBySurname

	// get next free ID from Employees in the file
	public int getNextFreeId() {
		int nextFreeId = 0;
		// if file is empty or all records are empty start with ID 1 else look
		// for last active record
		if (file.length() == 0 || !recordValid())
			nextFreeId++;
		else {
			lastRecord();// look for last active record
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId

	// get values from text fields and create Employee object
	private Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeComboBox.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;

		theEmployee = new Employee(	Integer.parseInt(idField.getText()), 
									ppsField.getText().toUpperCase(),
									surnameField.getText().toUpperCase(), 
									firstNameField.getText().toUpperCase(),
									genderComboBox.getSelectedItem().toString().charAt(0), 
									deptComboBox.getSelectedItem().toString(),
									Double.parseDouble(salaryField.getText()), fullTime);

		return theEmployee;
	}// end getChangedDetails

	// add Employee object to fail
	public void addRecord(Employee newEmployee) {
		// open file for writing
		application.openWriteFile(file.getAbsolutePath());
		// write into a file
		currentByteStart = application.addRecords(newEmployee);
		application.closeWriteFile();// close file for writing
	}// end addRecord

	// delete (make inactive - empty) record from file
	private void deleteRecord() {
		if (recordValid()) {// if any active record in file display
									// message and delete record
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// if answer yes delete (make inactive - empty) record
			if (returnVal == JOptionPane.YES_OPTION) {
				// open file for writing
				application.openWriteFile(file.getAbsolutePath());
				// delete (make inactive - empty) record in file proper position
				application.deleteRecords(currentByteStart);
				application.closeWriteFile();// close file for writing
				// if any active record in file display next record
				if (recordValid()) {
					nextRecord();// look for next record
					displayRecords(currentEmployee);
				} // end if
			} // end if
		} // end if
	}// end deleteDecord

	// create vector of vectors with all Employee details
	private Vector<Object> getAllEmloyees() {
		// vector of Employee objects
		Vector<Object> allEmployee = new Vector<Object>();
		Vector<Object> empDetails;// vector of each employee details
		long byteStart = currentByteStart;
		int firstId;

		firstRecord();// look for first record
		firstId = currentEmployee.getEmployeeId();
		// loop until all Employees are added to vector
		do {
			empDetails = new Vector<Object>();
			empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
			empDetails.addElement(currentEmployee.getPps());
			empDetails.addElement(currentEmployee.getSurname());
			empDetails.addElement(currentEmployee.getFirstName());
			empDetails.addElement(new Character(currentEmployee.getGender()));
			empDetails.addElement(currentEmployee.getDepartment());
			empDetails.addElement(new Double(currentEmployee.getSalary()));
			empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

			allEmployee.addElement(empDetails);
			nextRecord();// look for next record
		} while (firstId != currentEmployee.getEmployeeId());// end do - while
		currentByteStart = byteStart;

		return allEmployee;
	}// end getAllEmployees

	// activate field for editing
	private void editDetails() {
		// activate field for editing if there is records to display
		if (recordValid()) {
			// remove euro sign from salary text field
			salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
			textFieldChange = false;
			setEnabled(true);// enable text fields for editing
		} // end if
	}// end editDetails

	// ignore changes and set text field unenabled
	private void cancelChange() {
		setEnabled(false);
		displayRecords(currentEmployee);
	}// end cancelChange

	// check if any of records in file is active - ID is not 0
	private boolean recordValid() {
		boolean valid = false;
		// open file for reading
		application.openReadFile(file.getAbsolutePath());
		// check if any of records in file is active - ID is not 0
		valid = application.isRecordValid();
		application.closeReadFile();// close file for reading
		// if no records found clear all text fields and display message
		if (!valid) {
			currentEmployee = null;
			idField.setText("");
			ppsField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderComboBox.setSelectedIndex(0);
			deptComboBox.setSelectedIndex(0);
			fullTimeComboBox.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
		return valid;
	}// end isSomeoneToDisplay

	// check for correct PPS format and look if PPS already in use
	public boolean correctPps(String pps, long currentByte) {
		boolean ppsExist = false;
		// check for correct PPS format based on assignment description
		if (pps.length() == 8 || pps.length() == 9) {
			if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
					&& Character.isDigit(pps.charAt(2))	&& Character.isDigit(pps.charAt(3)) 
					&& Character.isDigit(pps.charAt(4))	&& Character.isDigit(pps.charAt(5)) 
					&& Character.isDigit(pps.charAt(6))	&& Character.isLetter(pps.charAt(7))
					&& (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
				// open file for reading
				application.openReadFile(file.getAbsolutePath());
				// look in file is PPS already in use
				ppsExist = application.isPpsExist(pps, currentByte);
				application.closeReadFile();// close file for reading
			} // end if
			else
				ppsExist = true;
		} // end if
		else
			ppsExist = true;

		return ppsExist;
	}// end correctPPS

	// check if file name has extension .dat
	private boolean checkFileName(File fileName) {
		boolean checkFile = false;
		int length = fileName.toString().length();

		// check if last characters in file name is .dat
		if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
				&& fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
			checkFile = true;
		return checkFile;
	}// end checkFileName

	// check if any changes text field where made
	private boolean checkForChanges() {
		boolean anyChanges = false;
		// if changes where made, allow user to save there changes
		if (textFieldChange) {
			saveChanges();// save changes
			anyChanges = true;
		} // end if
			// if no changes made, set text fields as unenabled and display
			// current Employee
		else {
			setEnabled(false);
			displayRecords(currentEmployee);
		} // end else

		return anyChanges;
	}// end checkForChanges

	// check for input in text fields
	@Override
	public boolean checkInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display
		// message
		if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (ppsField.isEditable() && correctPps(ppsField.getText().trim(), currentByteStart)) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (genderComboBox.getSelectedIndex() == 0 && genderComboBox.isEnabled()) {
			genderComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (deptComboBox.getSelectedIndex() == 0 && deptComboBox.isEnabled()) {
			deptComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end try
		catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end catch
		if (fullTimeComboBox.getSelectedIndex() == 0 && fullTimeComboBox.isEnabled()) {
			fullTimeComboBox.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
			// display message if any input or format is wrong
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		// set text field to white colour if text fields are editable
		if (ppsField.isEditable())
			setToWhite();

		return valid;
	}

	// set text field background colour to white
	@Override
	public void setToWhite() {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderComboBox.setBackground(UIManager.getColor("TextField.background"));
		deptComboBox.setBackground(UIManager.getColor("TextField.background"));
		fullTimeComboBox.setBackground(UIManager.getColor("TextField.background"));
	}// end setToWhite

	// enable text fields for editing
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderComboBox.setEnabled(booleanValue);
		deptComboBox.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeComboBox.setEnabled(booleanValue);
		saveBtn.setVisible(booleanValue);
		cancelBtn.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchIdBtn.setEnabled(search);
		searchSurnameBtn.setEnabled(search);
	}// end setEnabled

	// open file
	private void openFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open");
		// display files in File Chooser only with extension .dat
		fc.setFileFilter(datfilter);
		File newFile; // holds opened file name and path
		// if old file is not empty or changes has been made, offer user to save
		// old file
		if (file.length() != 0 || textFieldChange) {
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// if user wants to save file, save it
			if (returnVal == JOptionPane.YES_OPTION) {
				saveFile();// save file
			} // end if
		} // end if

		int returnVal = fc.showOpenDialog(EmployeeDetails.this);
		// if file been chosen, open it
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			// if old file wasn't saved and its name is generated file name,
			// delete this file
			if (file.getName().equals(generatedFileName))
				file.delete();// delete file
			file = newFile;// assign opened file to file
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			firstRecord();// look for first record
			displayRecords(currentEmployee);
			application.closeReadFile();// close file for reading
		} // end if
	}// end openFile

	// save file
	private void saveFile() {
		// if file name is generated file name, save file as 'save as' else save
		// changes to file
		if (file.getName().equals(generatedFileName))
			saveFileAs();// save file as 'save as'
		else {
			// if changes has been made to text field offer user to save these
			// changes
			if (textFieldChange) {
				int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				// save changes if user choose this option
				if (returnVal == JOptionPane.YES_OPTION) {
					// save changes if ID field is not empty
					if (!idField.getText().equals("")) {
						// open file for writing
						application.openWriteFile(file.getAbsolutePath());
						// get changes for current Employee
						currentEmployee = getChangedDetails();
						// write changes to file for corresponding Employee
						// record
						application.changeRecords(currentEmployee, currentByteStart);
						application.closeWriteFile();// close file for writing
					} // end if
				} // end if
			} // end if

			displayRecords(currentEmployee);
			setEnabled(false);
		} // end else
	}// end saveFile

	// save changes to current Employee
	private void saveChanges() {
		int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes to current Employee?", "Save",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		// if user choose to save changes, save changes
		if (returnVal == JOptionPane.YES_OPTION) {
			// open file for writing
			application.openWriteFile(file.getAbsolutePath());
			// get changes for current Employee
			currentEmployee = getChangedDetails();
			// write changes to file for corresponding Employee record
			application.changeRecords(currentEmployee, currentByteStart);
			application.closeWriteFile();// close file for writing
			fileChange = false;// state that all changes has bee saved
		} // end if
		displayRecords(currentEmployee);
		setEnabled(false);
	}// end saveChanges

	// save file as 'save as'
	private void saveFileAs() {
		final JFileChooser fc = new JFileChooser();
		File newFile;
		String defaultFileName = "new_Employee.dat";
		fc.setDialogTitle("Save As");
		// display files only with .dat extension
		fc.setFileFilter(datfilter);
		fc.setApproveButtonText("Save");
		fc.setSelectedFile(new File(defaultFileName));

		int returnVal = fc.showSaveDialog(EmployeeDetails.this);
		// if file has chosen or written, save old file in new file
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			// check for file name
			if (!checkFileName(newFile)) {
				// add .dat extension if it was not there
				newFile = new File(newFile.getAbsolutePath() + ".dat");
				// create new file
				application.createFile(newFile.getAbsolutePath());
			} // end id
			else
				// create new file
				application.createFile(newFile.getAbsolutePath());

			try {// try to copy old file to new file
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// if old file name was generated file name, delete it
				if (file.getName().equals(generatedFileName))
					file.delete();// delete file
				file = newFile;// assign new file to file
			} // end try
			catch (IOException e) {
			} // end catch
		} // end if
		fileChange = false;
	}// end saveFileAs

	// allow to save changes to file when exiting the application
	private void exitApp() {
		// if file is not empty allow to save changes
		if (file.length() != 0) {
			if (fileChange) {
				int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				// if user chooses to save file, save file
				if (returnVal == JOptionPane.YES_OPTION) {
					saveFile();// save file
					// delete generated file if user saved details to other file
					if (file.getName().equals(generatedFileName))
						file.delete();// delete file
					System.exit(0);// exit application
				} // end if
					// else exit application
				else if (returnVal == JOptionPane.NO_OPTION) {
					// delete generated file if user chooses not to save file
					if (file.getName().equals(generatedFileName))
						file.delete();// delete file
					System.exit(0);// exit application
				} // end else if
			} // end if
			else {
				// delete generated file if user chooses not to save file
				if (file.getName().equals(generatedFileName))
					file.delete();// delete file
				System.exit(0);// exit application
			} // end else
				// else exit application
		} else {
			// delete generated file if user chooses not to save file
			if (file.getName().equals(generatedFileName))
				file.delete();// delete file
			System.exit(0);// exit application
		} // end else
	}// end exitApp

	// generate 20 character long file name
	private String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}// end getFileName

	// create file with generated file name when application is opened
	private void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		// assign generated file name to file
		file = new File(generatedFileName);
		// create file
		application.createFile(file.getName());
	}// end createRandomFile

	// action listener for buttons, text field and menu items
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == closeAppMenuItm) {
			if (checkInput() && !checkForChanges())
				exitApp();
		} else if (e.getSource() == openMenuItm) {
			if (checkInput() && !checkForChanges())
				openFile();
		} else if (e.getSource() == saveMenuItm) {
			if (checkInput() && !checkForChanges())
				saveFile();
			textFieldChange = false;
		} else if (e.getSource() == saveAsMenuItm) {
			if (checkInput() && !checkForChanges())
				saveFileAs();
			textFieldChange = false;
		} else if (e.getSource() == searchByIdMenuItm) {
			if (checkInput() && !checkForChanges())
				displaySearchByIdDialog();
		} else if (e.getSource() == searchBySurnameMenuItm) {
			if (checkInput() && !checkForChanges())
				displaySearchBySurnameDialog();
		} else if (e.getSource() == searchIdBtn || e.getSource() == searchByIdField)
			searchEmployeeById();
		else if (e.getSource() == searchSurnameBtn || e.getSource() == searchBySurnameField)
			searchEmployeeBySurname();
		else if (e.getSource() == saveBtn) {
			if (checkInput() && !checkForChanges())
				;
		} else if (e.getSource() == cancelBtn)
			cancelChange();
		else if (e.getSource() == firstMenuItm || e.getSource() == firstBtn) {
			if (checkInput() && !checkForChanges()) {
				firstRecord();
				displayRecords(currentEmployee);
			}
		} else if (e.getSource() == prevMenuItm || e.getSource() == previousBtn) {
			if (checkInput() && !checkForChanges()) {
				previousRecord();
				displayRecords(currentEmployee);
			}
		} else if (e.getSource() == nextMenuItm || e.getSource() == nextBtn) {
			if (checkInput() && !checkForChanges()) {
				nextRecord();
				displayRecords(currentEmployee);
			}
		} else if (e.getSource() == lastMenuItm || e.getSource() == lastBtn) {
			if (checkInput() && !checkForChanges()) {
				lastRecord();
				displayRecords(currentEmployee);
			}
		} else if (e.getSource() == listAllMenuItm || e.getSource() == displayAllBtn) {
			if (checkInput() && !checkForChanges())
				if (recordValid())
					displayEmployeeSummaryDialog();
		} else if (e.getSource() == createMenuItm || e.getSource() == addBtn) {
			if (checkInput() && !checkForChanges())
				new AddRecordDialog(EmployeeDetails.this);
		} else if (e.getSource() == modifyMenuItm || e.getSource() == editBtn) {
			if (checkInput() && !checkForChanges())
				editDetails();
		} else if (e.getSource() == deleteMenuItm || e.getSource() == deleteBtn) {
			if (checkInput() && !checkForChanges())
				deleteRecord();
		} else if (e.getSource() == searchBySurnameMenuItm) {
			if (checkInput() && !checkForChanges())
				new SearchBySurnameDialog(EmployeeDetails.this);
		}
	}// end actionPerformed

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigatePanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}// end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		textFieldChange = true;
		new JTextFieldLimit(20);
	}

	public void insertUpdate(DocumentEvent d) {
		textFieldChange = true;
		new JTextFieldLimit(20);
	}

	public void removeUpdate(DocumentEvent d) {
		textFieldChange = true;
		new JTextFieldLimit(20);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		textFieldChange = true;
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		exitApp();
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}// end class EmployeeDetails
