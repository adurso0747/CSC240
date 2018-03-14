package passwordchanger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import views.MyFrame;

public class Controller {
  
  //Creating user variables for database ArrayList
  private final User user1 = new User("ab111111@wcupa.edu", "a2Foofoo");
  private final User user2 = new User("cd222222@wcupa.edu", "b.Barbar");
  private final User user3 = new User("ef333333@wcupa.edu", "123456.x");	
  
  private final MyFrame frame = new MyFrame();
  
  public Controller() {
    frame.setTitle( getClass().getSimpleName() );
    frame.setLocationRelativeTo(null);
	
	//Creating users database ArrayList
	ArrayList<User> users = new ArrayList<>();	
			users.add(user1);
			users.add(user2);
			users.add(user3);
			
    // event handlers
	
	JButton changeButton = frame.changePword();
	JButton dumpButton = frame.dumpButton();
	JTextField login = frame.getLoginField();
	JTextField cPword = frame.getcPwordField();
	JTextField nPword = frame.getnPwordField();
	JTextArea messages = frame.getMessagesField();
	
	//Seed values for testing
	login.setText("ab111111@wcupa.edu");
	cPword.setText("a2Foofoo");
	nPword.setText("a2fooFoo");
	
	changeButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//Retrieving and trimming values from frame
			String log = login.getText().toLowerCase();
			String cPass = cPword.getText().trim();
			String nPass = nPword.getText().trim();
			
			int count = 0; // Value to keep count of password characteristics
			messages.setText("");
			if(!log.matches("[a-zA-Z]{2}\\d{6}@wcupa.edu")){
				messages.setText("Validation: Login format incorrect");
			}
			else if(!loginCheck(log, users)){
				messages.setText("Validation: No such user");
			}
			else if(!passwordCheck(log, cPass, users)){
				messages.setText("Validation: Current password incorrect");
			}
			else if(nPass.length()<8){
				messages.setText("Validation: OK \nNew password: Too short");
			}
			else if(nPass.equals(cPass)){
				messages.setText("Validation: OK \nNew password: Cannot be the current password");
			}
			
			//Testing characteristics of new password
			else {
				if (nPass.matches(".*([a-z]).*")) {
					count++;
				}
				if (nPass.matches(".*([A-Z]).*")) {
					count++;
				}
				if (nPass.matches(".*([0-9]).*")) {
					count++;
				}
				if (nPass.matches(".*([^a-zA-Z0-9]).*")) {
					count++;
				}
				if (count < 3) {
					messages.setText("Validation: OK \nNew password: Too weak");
				} else {
					messages.setText("Validation: OK \nNew password: OK");
					passwordChange(log, nPass, users);
				}
			}
			
		}
	});
	
	dumpButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < 3; i++){
				User example = users.get(i);
				System.out.println("Username: " + example.getUsername() + " Password: " + example.getPassword());
		}
		}
	});
  }

/**
 * Checks that the entered login is within the database
 * @param login the login being checked
 * @param database the database which login is being checked against
 * @return true if login is within database, false otherwise
 */
 private boolean loginCheck(String login, ArrayList<User> database){
	 for(int i = 0; i<3; i++){
		 User test = database.get(i);
		 if(test.getUsername().equals(login)){
			 return true;
		 }		 
	 }
	 return false;	
 }
 
 /**
 * Checks that the entered password matches the entered login within the database
 * @param login the login being checked
 * @param password the password being checked
 * @param database the database which login is being checked against
 * @return true if password matches the login within the database, false otherwise
 */
  private boolean passwordCheck(String login, String password, ArrayList<User> database){
	 for(int i = 0; i<3; i++){
		 User test = database.get(i);
		 if(test.getUsername().equals(login) && test.getPassword().equals(password)){
			 return true;
		 }		 
	 }
	 return false;	
 }
  
/**
 * Changes a password within a database
 * @param login the login that has a password being changed
 * @param password the new password which will be entered into the database
 * @param database the database which will be changes
 */
  private void passwordChange(String login, String password, ArrayList<User> database){
	  User updated = new User(login, password);
	  for(int i = 0; i<3; i++){
		  User test = database.get(i);
		  if(test.getUsername().equals(login)){
			  database.set(i, updated);
		  }
	  }
  }
  
  public static void main(String[] args) {
    Controller app = new Controller();
    app.frame.setVisible(true);
  }
}