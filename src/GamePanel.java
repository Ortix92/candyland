import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = -3478812483258109832L;
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	private GameStateManager gsm;
	
	private JPanel current;

	public GamePanel() {
		super();
		
		init();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		
		current = new MenuState(gsm);
		add(current);
		
		setVisible(true);
		
		//current.addKeyListener(this);
		addKeyListener(this);
		//requestFocus();
	}
	
	// initializes variables
	private void init() {
		gsm = new GameStateManager();
	}


	// draws the game onto an off-screen buffered image
	public void draw() {
		System.out.println("GamePanel.draw");
		int currentState = gsm.getCurrentState();
		switch(currentState) {
		case GameStateManager.MENUSTATE : current = new MenuState(gsm); // draw menu		add(menuPanel)
			break;
		case GameStateManager.MAZESTATE : System.out.println("add(maze)");// draw maze		add(mazePanel)
			break;
		}
		add(current);
	}

	// draws the off-screen buffered image to the screen
	private void drawToScreen() {

	}

	public void keyTyped(KeyEvent k) {
		System.out.println(" dsafjalsjdfl");

	}

	public void keyPressed(KeyEvent k) {
		System.out.println("GamePanel.keyPressed");
		gsm.keyPressed(k.getKeyCode());
	}

	public void keyReleased(KeyEvent k) {
		gsm.keyPressed(k.getKeyCode());
	}

}