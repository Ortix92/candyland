import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

/**
 * Player represents the actual player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Player, ie. 
 * where it is looking at and the player's speed. 
 * <p>
 * For the player to move, a reference to a Control object can be set, which can then
 * be polled directly for the most recent input. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Guy extends GameObject {	
	
	private Control control = null;
	/**
	 * The Player constructor.
	 * <p>
	 * This is the constructor that should be used when creating a Player. It sets
	 * the starting location and orientation.
	 * <p>
	 * Note that the starting location should be somewhere within the maze of 
	 * MazeRunner, though this is not enforced by any means.
	 * 
	 * @param x		the x-coordinate of the location
	 * @param y		the y-coordinate of the location
	 * @param z		the z-coordinate of the location
	 * @param h		the horizontal angle of the orientation in degrees
	 * @param v		the vertical angle of the orientation in degrees
	 */
	public Guy() {
		
	}
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	
public void setControl(Control control)
	{
		this.control = control;
	}

public Control getControl()
{
	return control;
}
			
		
	
	public void display(GL gl, Player player) {
		GLUT glut = new GLUT();
		 float wallColour[] = { 0.5f, 10.0f, 30.0f, 1.0f };			
	        gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
	        gl.glDisable(GL.GL_DEPTH_TEST);
	        gl.glPushMatrix();
	        
		if (control.getForward()) {
			gl.glTranslated(player.getLocationX(), -2.0, player.getLocationZ());
			gl.glRotated(player.getHorAngle() - 90,0.0, 1.0, 0.0);
			gl.glRotated(player.getSpeed()*90, 0 , 0, 1);
			glut.glutSolidCube(2.0f); }
		if (control.getBack()) {
			gl.glTranslated(player.getLocationX(), -2.0, player.getLocationZ());
			gl.glRotated(player.getHorAngle() - 90,0.0, 1.0, 0.0);
			gl.glRotated(-player.getSpeed()*90, 0 , 0, 1);
			glut.glutSolidCube(2.0f); }
		if (control.getLeft()) {
			gl.glTranslated(player.getLocationX(), -2.0, player.getLocationZ());
			gl.glRotated(player.getHorAngle() - 90,0.0, 1.0, 0.0);
			gl.glRotated(player.getSpeed()*90, 1 , 0, 0);
			glut.glutSolidCube(2.0f); }
		if (control.getRight()) {
			gl.glTranslated(player.getLocationX(), -2.0, player.getLocationZ());
			gl.glRotated(player.getHorAngle() - 90,0.0, 1.0, 0.0);
			gl.glRotated(-player.getSpeed()*90, 1 , 0, 0);
			glut.glutSolidCube(2.0f); 
		} else {	
			gl.glTranslated(player.getLocationX(), -2.0, player.getLocationZ());
			gl.glRotated(player.getHorAngle() - 90,0.0, 1.0, 0.0);
			glut.glutSolidCube(2.0f);
			
			
		}
		
		gl.glPopMatrix();
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	public void update(int deltaTime) {
		
	}
}