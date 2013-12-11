package leveleditor;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JPanel;

public class editor {

	private JFrame frame;
	private final Action action = new SwingAction();
	private World world;
	private int editorPanelWidth = 600;
	private int editorPanelHeight = 600;
	private Painter editorPanel;
	private JSlider resolutionSlider;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					editor window = new editor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * set the listener whether we are drawing or not
	 */
	public void drawMap(boolean draw) {
		this.editorPanel.setDrawMapListener(draw);
	}

	/**
	 * Create the application.
	 */
	public editor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1080, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open File..");
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As..");
		mnFile.add(mntmSaveAs);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmNyangroep = new JMenuItem("NYANGROEP8");
		mnAbout.add(mntmNyangroep);

		panel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				Alignment.LEADING,
				groupLayout
						.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 1062,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(60, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE));
		panel.setLayout(null);

		// New Maze Button
		JButton btnNewMaze = new JButton("New Maze");
		btnNewMaze.setBounds(47, 173, 126, 45);
		panel.add(btnNewMaze);
		btnNewMaze.setAction(action);
		btnNewMaze.setText("New Maze");

		// Load maze button
		JButton btnLoadMaze = new JButton("Load Maze");
		btnLoadMaze.setBounds(47, 229, 126, 41);
		panel.add(btnLoadMaze);
		btnLoadMaze.setAction(action);
		btnLoadMaze.setText("Load Maze");

		// Export the maze
		JButton btnExportMaze = new JButton("Export Maze");
		btnExportMaze.setBounds(47, 281, 126, 53);
		panel.add(btnExportMaze);
		btnExportMaze.setAction(action);
		btnExportMaze.setText("Export Maze");

		resolutionSlider = new JSlider();
		resolutionSlider.setBounds(242, 148, 43, 200);
		panel.add(resolutionSlider);
		resolutionSlider.setOrientation(SwingConstants.VERTICAL);
		resolutionSlider.setMaximum(50);
		resolutionSlider.setToolTipText("Select the resolution of the grid. ");
		resolutionSlider.setSnapToTicks(true);
		resolutionSlider.setPaintLabels(true);
		resolutionSlider.setPaintTicks(true);
		resolutionSlider.setMinorTickSpacing(5);
		resolutionSlider.setMinimum(0);
		resolutionSlider.setMajorTickSpacing(20);

		JLabel lblOptions = new JLabel("Options");
		lblOptions.setBounds(70, 148, 37, 14);
		panel.add(lblOptions);

		JLabel lblGridResolution = new JLabel("Grid Resolution");
		lblGridResolution.setBounds(217, 123, 72, 14);
		panel.add(lblGridResolution);

		// JOGL Panel
		this.editorPanel = new Painter(editorPanelWidth, editorPanelHeight);
		editorPanel.setBounds(430, 44, 610, 610);
		panel.add(editorPanel);

		JLabel lblVisualEditor = new JLabel("Visual Editor");
		lblVisualEditor.setBounds(717, 19, 58, 14);
		panel.add(lblVisualEditor);
		frame.getContentPane().setLayout(groupLayout);
	}

	private class SwingAction extends AbstractAction {

		private static final long serialVersionUID = 3928887640385948568L;

		public SwingAction() {
			// putValue(NAME, "Load Maze");
			// putValue(SHORT_DESCRIPTION, "Load the maze text file");
		}

		public void actionPerformed(ActionEvent e) {
			String button = ((JButton) e.getSource()).getText();
			switch (button) {
			case "Load Maze":
				this.loadMaze();
				break;
			case "Export Maze":
				this.exportMaze();
				break;
			case "New Maze":
				this.newMaze();
				break;

			}

		}

		private void newMaze() {
			int resolution = editor.this.resolutionSlider.getValue();
			if (resolution != 0) {
				editor.this.editorPanel.setResolution(resolution);
				editor.this.editorPanel.setDrawGridListener(true);

				World emptyMap = new World(null); // we pass null instead of a
													// path,
													// that's fine

				editor.this.editorPanel.setMaze(emptyMap
						.getZeroesMatrix(resolution));
				editor.this.drawMap(true);
			} else {
				JOptionPane.showMessageDialog(frame,
						"0 is not a valid grid size!");
			}
		}

		private void exportMaze() {
			ArrayList<ArrayList<Integer>> map = editor.this.editorPanel
					.getMaze();
			String filename = "map_export.txt";
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				for (int i = 0; i < map.size(); i++) {
					for (int j = 0; j < map.size(); j++) {
						bw.write(map.get(i).get(j) + " ");
					}
					// prevent new line if end of matrix reached
					if (i < map.size() - 1) {
						bw.write("\r\n");
					}
				}
				JOptionPane.showMessageDialog(frame, "Exported to file \""
						+ filename + "\"");
				System.out.println("Exported to file \"" + filename + "\"");
				bw.close();

			} catch (IOException e) {

				JOptionPane
						.showMessageDialog(frame, "Could not write to file!");
				System.out.println(e.getMessage());
			} catch (NullPointerException e) {
				JOptionPane
						.showMessageDialog(frame,
								"Error exporting map. Either the maze is not square or is empty");
				System.out.println(e.getMessage());
			}
		}

		private void loadMaze() {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: "
						+ chooser.getSelectedFile().getAbsolutePath());
				// Create new World
				world = new World(chooser.getSelectedFile().getAbsolutePath());
				try {
					world.loadMapFromFile();
					editor.this.editorPanel.setMaze(world.getMap());
					editor.this.drawMap(true);
				} catch (IllegalArgumentException | FileNotFoundException e) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Failed loading maze with error: "
											+ e.getMessage());
				}
			}
		}
	}
}
