import java.awt.event.ActionEvent;

import javax.swing.JPanel;

public interface EmployeeDetailsInterface {
	public boolean checkInput();
	public void setToWhite();
	public JPanel createEmployeeDetailsPanel();
	public void formatFields(JPanel employeeDetailsPanel);
	public void actionPerformed(ActionEvent e);
}
