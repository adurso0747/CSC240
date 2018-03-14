package jtextedit;

/**
 *
 * @author Alex Durso <ad901965@wcupa.edu>
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import views.MyFrame;

public class Controller {

	//creating dedicated data members to remember what has been done
	private boolean fileLoaded = false;
	private boolean newFile = false;
	private boolean modified = false;
	private boolean modifiable = false;

	private final MyFrame frame = new MyFrame();

	JMenu fileMenu = frame.getFileMenu();
	JMenuItem openItem = frame.getOpenMenuItem();
	JMenuItem newItem = frame.getNewMenuItem();
	JMenuItem saveItem = frame.getSaveMenuItem();
	JMenuItem saveAsItem = frame.getSaveAsMenuItem();
	JTextField textField = frame.getTextField();
	JTextArea textArea = frame.getTextArea();
	JLabel modifiedLabel = frame.getModifiedLabel();

	public Controller() {
		frame.setTitle("Editor");
		frame.setLocationRelativeTo(null);
		frame.setSize(500, 500);

		// event handlers
		fileMenu.addMenuListener(new MenuListener() {
			@Override
			//Determine which menu items to enable by checking data members
			public void menuSelected(MenuEvent e) {
				if (!fileLoaded && !newFile) {
					saveAsItem.setEnabled(false);
					saveItem.setEnabled(false);
				} else if (newFile) {
					saveAsItem.setEnabled(true);
					saveItem.setEnabled(false);
					textArea.setEditable(true);
				} else {
					saveAsItem.setEnabled(true);
					saveItem.setEnabled(true);
					textArea.setEditable(true);
				}
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = Controller.getFileChooser();

				// invoke the chooser dialog for opening a file
				int status = chooser.showOpenDialog(frame);

				// test for approval
				if (status != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File file = chooser.getSelectedFile();
				Path path = file.toPath();
				
				//serialize object so it can be referenced from save without invoking chooser dialog
				File saveFile = new File("saved.dat");
				try {
					writeObject(file, saveFile);
				} catch (IOException ex) {
					Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
				}
				if (modified) {
					int reply = JOptionPane.showConfirmDialog(frame, "OK to discard changes?");
					if (reply == 0) {
						openFile(path, file);
						modifiedLabel.setText("           ");
					}
				} else {
					openFile(path, file);
					modifiedLabel.setText("           ");
				}
			}
		});

		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//If the file has been modified ensure user is ok discarding changes
				if (modified) {
					int reply = JOptionPane.showConfirmDialog(frame, "OK to discard changes?");
					if (reply == 0) {
						newFile();
					}
				} else {
					newFile();
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Retrieve path from textfield
				String content = textArea.getText();
				Object obj = null;
				
				//deserialize saved obj so it's path can be read
				try {
					obj = readObject(new File("saved.dat"));
				} catch (IOException ex) {
					Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
				}
				File file = (File) obj;
				Path path = file.toPath();

				//Reset modified data member and write file
				try {
					modified = false;
					Files.deleteIfExists(path);
					Files.write(path, content.getBytes());
					modifiedLabel.setText("           ");
				} catch (IOException ex) {
					System.err.println("---- FAILED ----");
					System.err.println("message:   " + ex.getMessage());
					System.err.println("exception: " + ex.getClass());
				}

			}
		});

		saveAsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = Controller.getFileChooser();

				// invoke the chooser dialog for opening a file
				int status = chooser.showSaveDialog(frame);

				// test for approval
				if (status != JFileChooser.APPROVE_OPTION) {
					return;
				}

				//Retrieve file extension. If none exists, add .txt
				File file = chooser.getSelectedFile();
				String extension = getFileExtension(file);
				if (extension == "") {
					String filePath = file.getAbsolutePath();
					file = new File(filePath + ".txt");
				}

				//Retrieve relative path for path field
				Path pathAbsolute = file.toPath();
				Path pathBase = Paths.get(System.getProperty("user.dir"));
				Path pathRelative = pathBase.relativize(pathAbsolute);
				Path path = file.toPath();
				
				//serialize object so it can be referenced from save without invoking chooser dialog
				File saveFile = new File("saved.dat");
				try {
					writeObject(file, saveFile);
				} catch (IOException ex) {
					Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
				}

				//If file exists determine if ok to overwrite
				if (Files.exists(path)) {
					int reply = JOptionPane.showConfirmDialog(frame, "OK to overwrite existing file?");
					if (reply == 0) {
						saveFileAs(path, pathRelative, file);
					}
				} else {
					saveFileAs(path, pathRelative, file);
				}

			}
		});

		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				//When a key is typed display modified symbol on modified label and set modified to true
				if (modifiable) {
					modifiedLabel.setText("     *     ");
					modified = true;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				//If file has been modified ensure its ok to discard changes
				if (modified) {
					frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
					int reply = JOptionPane.showConfirmDialog(frame, "OK to discard changes?");
					if (reply == 0) {
						System.exit(0);
					}
				} else {
					frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
				}

			}
		});
	}

	/**
	 * Gets the file extension from a file
	 *
	 * @param File the file that the extension is being retrieved from
	 * @return the string of the file's extension
	 */
	private String getFileExtension(File file) {
		String name = file.getName();
		String extension = "";
		try {
			int i = name.lastIndexOf('.');
			if (i >= 0) {
				extension = name.substring(i + 1);
			}
			return extension;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Returns a file chooser with specific properties
	 *
	 * @return the specified file chooser
	 */
	private static JFileChooser getFileChooser() {
		JFileChooser chooser = new JFileChooser();

		// specify where chooser should open up
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		// define a set of "Editable Files" files by extension
		chooser.addChoosableFileFilter(
				new FileNameExtensionFilter("Text Files", "txt")
		);

		// Adds an option to select other types of files
		chooser.setAcceptAllFileFilterUsed(true);

		return chooser;
	}

	/**
	 * Opens a file into the editor
	 *
	 * @param path the path which will be opened
	 * @param file the file whose name will be retrieved
	 */
	private void openFile(Path path, File file) {
		try {
			//Set data members
			fileLoaded = true;
			modifiable = true;
			newFile = false;
			modified = false;

			//Read file to textArea
			String content = new String(Files.readAllBytes(path));
			String fileName = file.getName();
			textArea.setText(content);
			textArea.setCaretPosition(0);
			textField.setText(fileName);
			textArea.setEditable(true);
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(frame, "Cannot open file " + file);
		}
	}

	/**
	 * sets up a new file
	 */
	private void newFile() {
		//Set data members
		modified = false;
		modifiable = true;
		newFile = true;

		//Set up a new file 
		textField.setText("<NEW FILE>");
		textArea.setText("");
		textArea.setEditable(true);
		modifiedLabel.setText("           ");
	}

	/**
	 * Saves a file with a specific name
	 
	 * @param path the path of the file being saved
	 * @param pathRelative the files relative path to usr.dir
	 * @param file which will be referenced in case of IO exception
	 */
	private void saveFileAs(Path path, Path pathRelative, File file) {
		try {
			//Set data members and write file
			modified = false;
			fileLoaded = true;
			newFile = false;
			String content = textArea.getText();
			Files.write(path, content.getBytes());
			modifiedLabel.setText("           ");
			textField.setText(pathRelative.toString());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(frame, "Cannot open file " + file);
		}
	}

	/**
	 * Writes object to file
	 
	 * @param obj the obj being saved
	 * @param f the File which it will be saved into
	 */
	private static void writeObject(Object obj, File f) throws IOException {
		FileOutputStream ostr = new FileOutputStream(f);
		ObjectOutputStream obj_ostr = new ObjectOutputStream(ostr);
		obj_ostr.writeObject(obj);
		obj_ostr.close();
	}
	
	/**
	 * Reads object from file
	 
	 * @param f the File which will be read
	 * @return the obj which was serialized
	 */
	private static Object readObject(File f)
			throws IOException, ClassNotFoundException {
		FileInputStream istr = new FileInputStream(f);
		ObjectInputStream obj_istr = new ObjectInputStream(istr);
		Object obj = obj_istr.readObject();
		obj_istr.close();
		return obj;
	}

	public static void main(String[] args) {
		Controller app = new Controller();
		app.frame.setVisible(true);
	}
}
