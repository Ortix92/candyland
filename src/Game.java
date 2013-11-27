import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame implements KeyListener {
	private GameState currentPanel;
	private GameStateManager gsm;

	public Game() {
		gsm = new GameStateManager();
		gsm.setCurrentState(27);
		currentPanel = gsm.getCurrentState();

		setTitle("CandyLand Nyancat Slayer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(currentPanel);

		addKeyListener(this);
		setFocusable(true);
		/*
		 * currentPanel.addKeyListener(this); currentPanel.setFocusable(true);
		 */

		this.setSize(1024, 768);

		// game.setResizable(true);
		// window.pack();
		setVisible(true);
	}

	/*
	 * remove(currentPanel); currentPanel = new MazeState(); add(currentPanel);
	 */

	public static void main(String[] args) {
		Game game = new Game();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		remove(currentPanel);
		gsm.setCurrentState(e.getKeyCode());
		currentPanel = gsm.getCurrentState();
		add(currentPanel);
		currentPanel.draw();
		setVisible(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}