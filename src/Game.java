import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener {
	public static GameStateManager gsm;

	public static JFrame frame = new JFrame();
	
	
	public Game() {
		gsm = new GameStateManager();
		frame.add(gsm.menu);
		frame.setSize(1024, 768);
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.setVisible(true);
		UserInput input;
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		}
 
	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
