package leveleditor;

import java.awt.EventQueue;

import javax.media.opengl.GL;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.Box;

import java.awt.Color;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class editor {

	private JFrame frame;
	private final Action action = new SwingAction();
	private World world;
	private int editorPanelWidth = 600;
	private int editorPanelHeight = 600;
	private Painter editorPanel;

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

		// Load maze button
		JButton btnLoadMaze = new JButton("Load Maze");
		btnLoadMaze.setAction(action);
		btnLoadMaze.setText("Load Maze");

		JLabel lblVisualEditor = new JLabel("Visual Editor");

		// New Maze Button
		JButton btnNewMaze = new JButton("New Maze");

		// JOGL Panel
		this.editorPanel = new Painter(editorPanelWidth, editorPanelHeight);

		// Export the maze
		JButton btnExportMaze = new JButton("Export Maze");
		btnExportMaze.setAction(action);
		btnExportMaze.setText("Export Maze");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(23)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																btnNewMaze,
																GroupLayout.PREFERRED_SIZE,
																120,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																btnLoadMaze,
																GroupLayout.PREFERRED_SIZE,
																120,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																btnExportMaze,
																GroupLayout.PREFERRED_SIZE,
																120,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												283, Short.MAX_VALUE)
										.addComponent(editorPanel,
												GroupLayout.PREFERRED_SIZE,
												600, GroupLayout.PREFERRED_SIZE)
										.addGap(38))
						.addGroup(
								groupLayout.createSequentialGroup()
										.addContainerGap(695, Short.MAX_VALUE)
										.addComponent(lblVisualEditor)
										.addGap(311)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(23)
																		.addComponent(
																				lblVisualEditor)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				editorPanel,
																				GroupLayout.PREFERRED_SIZE,
																				600,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(103)
																		.addComponent(
																				btnNewMaze,
																				GroupLayout.PREFERRED_SIZE,
																				64,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(18)
																		.addComponent(
																				btnLoadMaze,
																				GroupLayout.PREFERRED_SIZE,
																				64,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(18)
																		.addComponent(
																				btnExportMaze,
																				GroupLayout.PREFERRED_SIZE,
																				64,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(48, Short.MAX_VALUE)));
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
			}

		}

		private void exportMaze() {
			ArrayList<ArrayList<Integer>> map = editor.this.editorPanel
					.getMaze();
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("map.txt"));				
				
			} catch (IOException e) {
				System.out.println("Could not write to file!");
				e.printStackTrace();
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
				world.loadMapFromFile();
				editor.this.editorPanel.setMaze(world.getMap());
				editor.this.drawMap(true);

			}
		}
	}
}
