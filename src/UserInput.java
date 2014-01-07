import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Robot;
import java.awt.event.*;

import javax.media.opengl.GLCanvas;
import javax.swing.SwingUtilities;

/**
 * The UserInput class is an extension of the Control class. It also implements
 * three interfaces, each providing handler methods for the different kinds of
 * user input.
 * <p>
 * For making the assignment, only some of these handler methods are needed for
 * the desired functionality. The rest can effectively be left empty (i.e. the
 * methods under 'Unused event handlers').
 * <p>
 * Note: because of how java is designed, it is not possible for the game window
 * to react to user input if it does not have focus. The user must first click
 * the window (or alt-tab or something) before further events, such as keyboard
 * presses, will function.
 * 
 * @author Mattijs Driel
 * 
 */
public class UserInput extends Control implements MouseListener,
		MouseMotionListener, KeyListener {
	// TODO: Add fields to help calculate mouse movement

	int x = 0;
	int y = 0;
	int dx = 0;
	int dy = 0;
	int sx = 0;
	int sy = 0;
	GLCanvas canvas;
	private int initX;
	private int initY;
	public static boolean pause = false;
	public static boolean zoom = false;
	public static boolean dead = false;
	public static boolean backspace = false;

	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners need
	 * to be added to a GLCanvas.
	 * 
	 * @param canvas
	 *            The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas) {
		this.canvas = canvas;
		initX = canvas.getWidth() / 2;
		initY = canvas.getHeight() / 2;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);

	}

	/*
	 * **********************************************
	 * * Updating * **********************************************
	 */

	@Override
	public void update() {
		dX = dx;
		dY = dy;
		dx = 0;
		dy = 0;

	}

	/*
	 * **********************************************
	 * * Input event handlers * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getButton() == 1) {
			shoot = true;
		}

		if (Weapon.getNewWeapon() == 2) {
			if (event.getButton() == 3) {
				zoom = true;
			}
		}

		// System.out.println(event.getButton());

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == 1) {
			shoot = false;
		}
		if (Weapon.getNewWeapon() == 2) {
			if (event.getButton() == 3) {
				zoom = false;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		mouseMoved(event);

	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (!zoom) {
			if (event.getKeyCode() == KeyEvent.VK_W) {
				forward = true;
			} else if (event.getKeyCode() == KeyEvent.VK_S) {
				back = true;
			} else if (event.getKeyCode() == KeyEvent.VK_A) {
				left = true;
			} else if (event.getKeyCode() == KeyEvent.VK_D) {
				right = true;
			} else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
				jump = true;
			} else if (event.getKeyCode() == KeyEvent.VK_CONTROL) {
				duck = true;
			} else if (event.getKeyCode() == KeyEvent.VK_ESCAPE
					&& pause == false) {
				pause = true;
			} else if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
				sprint = true;
			} else if ((event.getKeyCode() == KeyEvent.VK_ESCAPE && pause == true)
					|| (event.getKeyCode() == KeyEvent.VK_P && pause == true)) {
				pause = false;
			} else if (event.getKeyCode() == KeyEvent.VK_ENTER
					&& UserInput.pause == true) {
				Game.frame = Game.gsm.setGameState(GameStateManager.MENU_STATE);
				pause = false;
			} else if (event.getKeyCode() == KeyEvent.VK_ENTER
					&& Game.gsm.getGameState() == GameStateManager.DEAD_STATE) {
				Game.gsm.scores.AddHighScore();
				ScoreScreen.naam = "";
				Game.frame = Game.gsm
						.setGameState(GameStateManager.SCORE_STATE);
			} else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				backspace = true;
			} else if (event.getKeyCode() == KeyEvent.VK_ENTER
					&& Game.gsm.getGameState() == GameStateManager.SCORE_STATE) {
				Game.frame = Game.gsm.setGameState(GameStateManager.MENU_STATE);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_W) {
			forward = false;
		} else if (event.getKeyCode() == KeyEvent.VK_S) {
			back = false;
		} else if (event.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		} else if (event.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		} else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			jump = false;
		} else if (event.getKeyCode() == KeyEvent.VK_CONTROL) {
			duck = false;
			net_gebukt = true;
		} else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			backspace = false;
		} else if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
			sprint = false;
		}

	}

	public void setdead() {
		dead = true;
	}

	public void isDead() {
		Game.gsm.setGameState(GameStateManager.DEAD_STATE);
	}

	/*
	 * **********************************************
	 * * Unused event handlers * **********************************************
	 */

	@Override
	public void mouseMoved(MouseEvent event) {

		int xOnScreen = event.getXOnScreen();
		int yOnScreen = event.getYOnScreen();

		dx = xOnScreen - initX;
		dy = yOnScreen - initY;

		x = initX;
		y = initY;

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.mouseMove(initX, initY);

	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (Game.gsm.getGameState() == GameStateManager.DEAD_STATE) {
			if (event.isShiftDown()) {
				ScoreScreen.letter = Character.toString(Character
						.toUpperCase(event.getKeyChar()));
			} else {
				ScoreScreen.letter = Character.toString(event.getKeyChar());
			}

		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {

	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

}
