/**
 *
 * @author Alex Durso <ad901965@wcupa.edu>
 */
package controller;

import figure.Figure;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import views.AddCircleDialog;
import views.AddRectDialog;
import views.Canvas;
import views.FigureFrame;

public class Controller {

	private final FigureFrame frame = new FigureFrame();
	private final Canvas canvas = frame.getCanvas();

	// figures which appear in canvas and in list
	private final List<Figure> figureList = new ArrayList<>();

	// model for Figure selection list
	private final DefaultListModel listModel = new DefaultListModel();

	// dialogs
	private AddRectDialog addRectDialog = new AddRectDialog(frame, true);
	private AddCircleDialog addCircleDialog = new AddCircleDialog(frame, true);

	//Reference variables
	private int lastX;
	private int lastY;
	private Figure selectedFig = null;

	public Controller() {
		frame.setTitle("Figures");
		frame.setLocationRelativeTo(null);
		frame.setSize(800, 500);

		canvas.setFigures(figureList);

		frame.getFigureList().setModel(listModel);

		// keep figureList from taking too much vertical space
		frame.getFigureList().getParent().setPreferredSize(new Dimension(0, 0));

		// permit only single element selection
		frame.getFigureList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// set the spinner model
		frame.getScaleSpinner().setModel(new SpinnerNumberModel(1.0, 0.1, 5.0, 0.05));
		
		//Event handlers
		frame.getLoadSamples().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				List<Figure> samples = Helpers.getSampleFigureList();
				figureList.clear();

				for (Figure sample : samples) {
					figureList.add(sample);
				}

				listModel.removeAllElements();
				for (Figure figure : figureList) {
					listModel.addElement(figure);
				}
				canvas.repaint();
			}
		});

		// Invoke the addRect dialog
		frame.getAddRectDialog().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addRectDialog.setLocationRelativeTo(null);
				addRectDialog.setTitle("Add a Rectangle Figure");

				addRectDialog.getHeightField().setText("" + 100);
				addRectDialog.getWidthField().setText("" + 200);
				addRectDialog.getStrokeWidthField().setText("" + 1);
				addRectDialog.getTitleField().setText("");

				addRectDialog.getLineColorField().setEditable(false);
				addRectDialog.getFillColorField().setEditable(false);

				addRectDialog.getLineColorField().setBackground(Color.black);
				addRectDialog.getFillColorField().setBackground(Color.white);

				addRectDialog.setVisible(true);
			}
		});

		//Invoke addCircle dialog
		frame.getAddCircleDialog().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCircleDialog.setLocationRelativeTo(null);
				addCircleDialog.setTitle("Add a Circle Figure");

				addCircleDialog.getDiameterField().setText("" + 200);
				addCircleDialog.getStrokeWidthField().setText("" + 1);
				addCircleDialog.getTitleField().setText("");

				addCircleDialog.getLineColorField().setEditable(false);
				addCircleDialog.getFillColorField().setEditable(false);

				addCircleDialog.getLineColorField().setBackground(Color.black);
				addCircleDialog.getFillColorField().setBackground(Color.white);

				addCircleDialog.setVisible(true);
			}
		});

		// addRectDialog and addCircleDialog need the remaining arguments to do its work in events
		Helpers.addEventHandlers(addRectDialog, figureList, listModel, frame, canvas);
		Helpers.addEventHandlers(addCircleDialog, figureList, listModel, frame, canvas);

		frame.getScaleSpinner().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (figureList.isEmpty()) {
					return;
				}
				double scale = (Double) frame.getScaleSpinner().getValue();
				Figure top = (Figure) figureList.get(0);
				top.setScale(scale);
				canvas.repaint();
			}
		});
		
		//Add mouse listeners to allow movement of a figure
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (figureList == null) {
					return;
				}
				int x = e.getX();
				int y = e.getY();

				for (Figure figure : figureList) {
					if (figure.getPositionShape().contains(x, y)) {
						selectedFig = figure;
						break;
					}
				}
				lastX = x;
				lastY = y;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				selectedFig = null;
			}
		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectedFig == null) {
					return;
				}
				int x = e.getX();
				int y = e.getY();

				int incX = x - lastX;
				int incY = y - lastY;

				lastX = x;
				lastY = y;

				selectedFig.incLocation(incX, incY);
				canvas.repaint();
			}
		});

		frame.getMoveToTopButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Move selected figure to the top by repainting
				Figure figSelection = (Figure) frame.getFigureList().getSelectedValue();
				if (figSelection == null) {
					return;
				}
				figureList.remove(figSelection);
				figureList.add(0, figSelection);
				canvas.setFigures(figureList);
				canvas.repaint();
				listModel.removeAllElements();
				//loop to put figures back into listmodel
				for (Figure figure : figureList) {
					listModel.addElement(figure);
				}
				double scale = figSelection.getScale();
				frame.getScaleSpinner().getModel().setValue(scale);
			}
		});

		frame.getRemoveButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//remove selected fig
				Figure figSelection = (Figure) frame.getFigureList().getSelectedValue();
				if (figSelection == null) {
					return;
				}
				figureList.remove(figSelection);
				canvas.setFigures(figureList);
				canvas.repaint();
				listModel.removeAllElements();
				//if list is empty set spinner value to 1
				if (figureList.isEmpty()) {
					frame.getScaleSpinner().getModel().setValue(1);
					return;
				}
				//loop to add figures back to listmodel
				for (Figure figure : figureList) {
					listModel.addElement(figure);
				}
				//set spinner value to top figure's scale
				double scale = ((Figure) figureList.get(0)).getScale();
				frame.getScaleSpinner().getModel().setValue(scale);
			}

		});
		frame.getSaveToFile().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					if (figureList.isEmpty()) {
						throw new Exception("Cannot save empty list");
					}
					//File chooser dialog
					JFileChooser fileChooser = getFileChooser();
					int status = fileChooser.showSaveDialog(frame);
					if (status != 0) {
						return;
					}
					File file = fileChooser.getSelectedFile();

					//add fig extension to files with no extension
					if (!file.getName().contains(".")) {
						file = new File(file.toString() + ".fig");
					}
					//Write object to file
					FileOutputStream ostr = new FileOutputStream(file);
					ObjectOutputStream oostr = new ObjectOutputStream(ostr);
					oostr.writeObject(figureList);
					oostr.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex);
				}

			}

		});

		frame.getLoadFromFile().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//File chooser dialog
				JFileChooser fileChooser = Controller.getFileChooser();

				int status = fileChooser.showOpenDialog(frame);
				if (status != 0) {
					return;
				}
				File f = fileChooser.getSelectedFile();
				try {
					//read object from file
					FileInputStream istr = new FileInputStream(f);
					ObjectInputStream oistr = new ObjectInputStream(istr);

					List<Figure> figList = (List) oistr.readObject();
					oistr.close();
					figureList.clear();
					for (Figure fig : figList) {
						figureList.add(fig);
					}

					//add figures from list to listmodel
					listModel.removeAllElements();
					for (Figure figure : figureList) {
						listModel.addElement(figure);
					}

					if (figureList.isEmpty()) {
						return;
					}
					//set spinner size to top most scale
					double scale = ((Figure) figureList.get(0)).getScale();
					frame.getScaleSpinner().getModel().setValue(scale);

					canvas.repaint();

				} catch (ClassNotFoundException ex) {
					ex.printStackTrace(System.err);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame, ex);
				}

			}

		});

	}

	/**
	 * Returns a file chooser with specific properties
	 *
	 * @return the specified file chooser
	 */
	private static JFileChooser getFileChooser() {
		JFileChooser chooser = new JFileChooser();
		//Specify where chooser opens up
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		//define a set of "Editable Files" files by extension
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Figure Files", "fig"));
		//Do not allow the selection of other file types
		chooser.setAcceptAllFileFilterUsed(false);
		return chooser;
	}

	public static void main(String[] args) {
		Controller app = new Controller();
		app.frame.setVisible(true);
	}
}
