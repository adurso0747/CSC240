package hellogui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import views.MyFrame;

public class Controller {

	private final MyFrame frame = new MyFrame();

	public Controller() {
		frame.setTitle(getClass().getSimpleName());
		frame.setLocationRelativeTo(null);

		// event handlers
		JButton hello = frame.getHelloButton();
		JTextField sampleTextField = frame.getSampleTextField();
		JTextArea sampleTextArea = frame.getSampleTextArea();

		hello.addActionListener(new ActionListener() {
			private int count = 0;
      @Override
      public void actionPerformed(ActionEvent e) {
        String sample_text = sampleTextField.getText();
        sampleTextField.setText("");
 
        String new_line 
            = String.format("%s: %s\n", ++count, sample_text.toUpperCase());
 
        String area_text = sampleTextArea.getText();
        sampleTextArea.setText(area_text + new_line);//To change body of generated methods, choose Tools | Templates.
			}
		});

	}

	public static void main(String[] args) {
		Controller app = new Controller();
		app.frame.setVisible(true);
	}
}
