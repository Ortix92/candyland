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
		btnMap2.setBounds(513, 52, 200, 200);
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
		btnMap4.setBounds(513, 315, 200, 200);
		add(btnMap4);

		btnReturn = new JButton("Return");
		btnReturn.setAction(action);
		btnReturn.setText("Return!");
		btnReturn.setActionCommand("return");
		btnReturn.setBounds(345, 246, 89, 87);
		add(btnReturn);

	}

	private class LoadMapAction extends AbstractAction {

		private static final long serialVersionUID = 3928887640385948568L;

		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();

			if (action.equals("return")) {
				Game.frame = Game.gsm.setGameState(GameStateManager.MENU_STATE);
			} else if (action.equals("exported")) {
				Maze.mazeFile = "map_export.txt";
			} else {
				Maze.mazeFile = "src/assets/maps/" + action + ".txt";
				JOptionPane
						.showMessageDialog(LevelSelector.this,
								"Your map has been selected. Time to kick some NYAN ass~");
				System.out.println("Map Selected: " + Maze.mazeFile);

			}

		}
	}

}
