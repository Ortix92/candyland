import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MenuState extends GameState {

	public MenuState(GameStateManager gsm) {

		Game.window.setTitle("CandyLand Nycan Slayer - Menu");
		drawMenu();
		this.gsm = gsm;
	}
	
	public void draw() {
		
	}
	
	public void drawMenu() {


		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JButton button_start = new JButton(new ImageIcon(
				"images/menu/button_start.png"));
		JButton button_load = new JButton(new ImageIcon(
				"images/menu/button_load.png"));
		JButton button_level_editor = new JButton(new ImageIcon(
				"images/menu/button_level_editor.png"));
		JButton button_options = new JButton(new ImageIcon(
				"images/menu/button_options.png"));
		JButton button_exit = new JButton(new ImageIcon(
				"images/menu/button_exit.png"));

		JPanel menuPanel = new JPanel();

		JLabel background = new JLabel(new ImageIcon(
				"images/menu/Nyan-Cat-menu.gif"));

		background.setBounds(0, 0, 1024, 768);
		button_start.setBounds(0, 146, 561, 57);
		button_load.setBounds(0, 235, 561, 57);
		button_level_editor.setBounds(0, 324, 561, 57);
		button_options.setBounds(0, 413, 561, 57);
		button_exit.setBounds(0, 502, 561, 57);

		menuPanel.add(button_start);
		menuPanel.add(button_load);
		menuPanel.add(background);
		menuPanel.add(button_level_editor);
		menuPanel.add(button_options);
		menuPanel.add(button_exit);

		Game.window.add(menuPanel);
		Game.window.setSize(1024, 768);
		Game.window.setVisible(true);

		final JButton[] knoppen = new JButton[5];
		knoppen[0] = button_start;
		knoppen[1] = button_load;
		knoppen[2] = button_level_editor;
		knoppen[3] = button_options;
		knoppen[4] = button_exit;

		final String[] knopnamen = new String[5];
		knopnamen[0] = "button_start";
		knopnamen[1] = "button_load";
		knopnamen[2] = "button_level_editor";
		knopnamen[3] = "button_options";
		knopnamen[4] = "button_exit";

		for (int i = 0; i < knoppen.length; i++) {

			final int j = i;
			knoppen[i].addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					String name = knopnamen[j];
					String imageplace = "images/menu/" + name + "_active.png";
					knoppen[j].setIcon(new ImageIcon(imageplace));
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					String name = knopnamen[j];
					String imageplace = "images/menu/" + name + ".png";
					knoppen[j].setIcon(new ImageIcon(imageplace));
				}

				public void mouseClicked(java.awt.event.MouseEvent evt) {
					// Sound.play("sounds/sonic_ring.wav");

					switch (j) {
					case 0:
						GameRunner.StateUpdater(GameRunner.State.GAME);
						GameRunner.state_changed = true;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						System.exit(0);
					}
				}
			});

		}

	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			gsm.setCurrentState(GameStateManager.MAZESTATE);
	
		}
	}

	public void keyReleased(int k) {

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
