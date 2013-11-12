import java.awt.event.*;

import javax.media.opengl.GLCanvas;

/**
 * The UserInput class is an extension of the Control class. It also implements three 
 * interfaces, each providing handler methods for the different kinds of user input.
 * <p>
 * For making the assignment, only some of these handler methods are needed for the 
 * desired functionality. The rest can effectively be left empty (i.e. the methods 
 * under 'Unused event handlers').  
 * <p>
 * Note: because of how java is designed, it is not possible for the game window to
 * react to user input if it does not have focus. The user must first click the window 
 * (or alt-tab or something) before further events, such as keyboard presses, will 
 * function.
 * 
 * @author Mattijs Driel
 *
 */
public class UserInput extends Control 
		implements MouseListener, MouseMotionListener, KeyListener
{
	// TODO: Add fields to help calculate mouse movement
	private int initialX;
	private int initialY;
	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas)
	{
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
	}
	
	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */

	@Override
	public void update()
	{
		// TODO: Set dX and dY to values corresponding to mouse movement
		//this.getdX();
		
	}

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{
		// TODO: Detect the location where the mouse has been pressed
		
		initialX = event.getX();
		initialY = event.getY();
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{		
		// TODO: Detect mouse movement while the mouse button is down
		dX = event.getX()-initialX;
		dY = event.getY()-initialY;
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		this.setdX(0);
		this.setdY(0);
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{

			if(event.getKeyChar() == 'w') {
				forward = true;
			}
			if(event.getKeyChar() == 'a') {
				left = true;
			}
			if(event.getKeyChar() == 's') {
				back = true;
			}
			if(event.getKeyChar() == 'd') {
				right = true;
			}
		
	}

	@Override
	public void keyReleased(KeyEvent event)
	{

		if(event.getKeyChar() == 'w') {
			forward = false;
		}
		if(event.getKeyChar() == 'a') {
			left = false;
		}
		if(event.getKeyChar() == 's') {
			back = false;
		}
		if(event.getKeyChar() == 'd') {
			right = false;
		}
		
	}

	/*
	 * **********************************************
	 * *		Unused event handlers				*
	 * **********************************************
	 */
	
	@Override
	public void mouseMoved(MouseEvent event)
	{
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}




}
