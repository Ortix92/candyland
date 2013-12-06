import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Robot;
import java.awt.event.*;

import javax.media.opengl.GLCanvas;
import javax.swing.SwingUtilities;
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

	private int x;
	private int y;
	private int X;
	private int Y;
	private int robot=1;

	
	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas)
	{	canvas.addMouseListener(this);
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
	{ 		dX = X-MazeRunner.getScreenWidth()/2;
			dY = Y-MazeRunner.getScreenHeight()/2;
			System.out.println(dX+" "+dY);			
						
		// TODO: Set dX and dY to values corresponding to mouse movement
			
					 
	}
			
	

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{/*
		// TODO: Detect the location where the mouse has been pressed
		if (SwingUtilities.isLeftMouseButton(event)) {
			x =	event.getX();
			y = event.getY();
			X = x;
			Y = y;
		}
	*/
	}

	@Override
	public void mouseDragged(MouseEvent event){
		
	}
	/*{	
		//if (SwingUtilities.isLeftMouseButton(event)) {
		x = X;
		y = Y;
	 X = event.getX();
	 Y = event.getY();//}
	}
*/
	@Override
	public void keyPressed(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		
		if (event.getKeyCode() == KeyEvent.VK_W) {
			 forward = true; }
			else if (event.getKeyCode() == KeyEvent.VK_S) { 
			 back = true;}
			else if (event.getKeyCode() == KeyEvent.VK_A){
			 left = true;}
			else if (event.getKeyCode() == KeyEvent.VK_D){
			 right = true;}
			else if (event.getKeyCode() == KeyEvent.VK_F){
			shoot = true;
				
			}
		
		}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		if (event.getKeyCode() == KeyEvent.VK_W) {
		 forward = false; }
		else if (event.getKeyCode() == KeyEvent.VK_S) { 
		 back = false; }
		else if (event.getKeyCode() == KeyEvent.VK_A){
		 left = false; }
		else if (event.getKeyCode() == KeyEvent.VK_D){
		 right = false; }
		else if (event.getKeyCode() == KeyEvent.VK_F){
			shoot = false; }
		else if(event.getKeyCode()==KeyEvent.VK_ESCAPE){
			// iets...
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
				X = event.getX()+9;
				Y = event.getY()+36;
				 try {					 
						Robot Rob=new Robot();
						Rob.mouseMove(MazeRunner.getScreenWidth()/2, MazeRunner.getScreenHeight()/2);
						
					} catch (AWTException e) {
						System.out.println("De robot faalt.");
					}	
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}


}
