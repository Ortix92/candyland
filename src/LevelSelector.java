import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class LevelSelector extends JPanel {

	private static final long serialVersionUID = 7370335504310144938L;
	private JButton btnMap1;
	private JButton btnMap2;
	private JButton btnMap3;
	private JButton btnMap4;
	private final Action action = new LoadMapAction();
	private JButton btnReturn;
	public static boolean chucknorris = false;
	public static boolean blue = false;

	/**
	 * Create the panel.
	 */
	public LevelSelector() {
		setLayout(null);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		btnMap1 = new JButton();
		btnMap1.setAction(action);
		btnMap1.setActionCommand("map1");
		btnMap1.setIcon(new ImageIcon(LevelSelector.class
				.getResource("/assets/maps/map1.jpg")));
		btnMap1.setBounds(55, 52, 200, 200);

		add(btnMap1);

		btnMap2 = new JButton();
		btnMap2.setAction(action);
		btnMap2.setActionCommand("map2");
		btnMap2.setIcon(new ImageIcon(LevelSelector.class
				.getResource("/assets/maps/map2.jpg")));
		btnMap2.setBounds(265, 52, 200, 200);
		add(btnMap2);

		btnMap3 = new JButton();
		btnMap3.setAction(action);
		btnMap3.setActionCommand("map3");
		btnMap3.setIcon(new ImageIcon(LevelSelector.class
				.getResource("/assets/maps/map3.jpg")));
		btnMap3.setBounds(55, 315, 200, 200);
		add(btnMap3);

		btnMap4 = new JButton();
		btnMap4.setAction(action);
		btnMap4.setActionCommand("map4");
		btnMap4.setIcon(new ImageIcon(LevelSelector.class
				.getResource("/assets/maps/map4.jpg")));
		btnMap4.setBounds(265, 315, 200, 200);
		add(btnMap4);

		btnReturn = new JButton("Return");
		btnReturn.setAction(action);
		btnReturn.setText("Return!");
		btnReturn.setActionCommand("return");
		btnReturn.setBounds(508, 52, 200, 200);
		add(btnReturn);

		JButton btnLoadExportedMap = new JButton();
		btnLoadExportedMap.setAction(action);
		btnLoadExportedMap.setText("Load Exported Map");
		btnLoadExportedMap.setActionCommand("exported");
		btnLoadExportedMap.setBounds(508, 315, 200, 200);
		add(btnLoadExportedMap);

	}

	/**
	 * Class containing all the actions which occur after a button press
	 * 
	 * @author ntsutsunava
	 * 
	 */
	private class LoadMapAction extends AbstractAction {

		private static final long serialVersionUID = 3928887640385948568L;

		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();

			switch (action) {
			case "return":
				Game.gsm.setGameState(Game.gsm.MENU_STATE);
				break;
			case "exported":
				Maze.mazeFile = "map_export.txt";
				JOptionPane
						.showMessageDialog(LevelSelector.this,
								"Your exported map has been selected. Time to kick some NYAN ass~");
				break;
			case "map1":
				Maze.mazeFile = "src/assets/maps/" + action + ".txt";

				JOptionPane
						.showMessageDialog(LevelSelector.this,
								"You are a pathetic soul. You will dwell for all of eternity in hell.");
				System.out.println("Map Selected: " + Maze.mazeFile);
				break;
			case "map2":
				Maze.mazeFile = "src/assets/maps/" + action + ".txt";

				JOptionPane.showMessageDialog(LevelSelector.this,
						"You have some skill, warrior, but you still suck.");
				System.out.println("Map Selected: " + Maze.mazeFile);
				break;
			case "map3":
				Maze.mazeFile = "src/assets/maps/" + action + ".txt";
				JOptionPane.showMessageDialog(LevelSelector.this,
						"You will avenge your fallen brothers, Godlike human.");
				System.out.println("Map Selected: " + Maze.mazeFile);
				blue = true;
				break;
			case "map4":
				Maze.mazeFile = "src/assets/maps/" + action + ".txt";
				JOptionPane.showMessageDialog(LevelSelector.this,
						"Right this way sir... do you want some coffee to go?");
				System.out.println("Map Selected: " + Maze.mazeFile);
				chucknorris = true;
				break;
			}

		}
	}
}
