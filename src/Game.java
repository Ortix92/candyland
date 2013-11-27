import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener {

	public GameStateManager gsm = new GameStateManager();
	public MazeRunner mazerunner = new MazeRunner();
	public Menu menu = new Menu(gsm);
	public JFrame frame = new JFrame();

	public Game() {
		frame.add(menu);

		frame.setSize(1024, 768);
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		gsm.setGameState(e.getKeyCode());
		
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
