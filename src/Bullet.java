import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

/**
 * A class which governs and creates the bullets fired from the guns
 * @author Michiel Schaap
 *
 */
public class Bullet extends GameObject {

	private int time;
	private boolean bullet;
	
	/**
	 * The constructor which creates the bullet
	 * @param x
	 * @param y
	 * @param z
	 * @param HorAngle
	 * @param VerAngle
	 */
	public Bullet(double x, double y, double z, double HorAngle, double VerAngle) {
	super( x,y,z );
	bullet = true;
	time = 300;
	}
	
	
	public void update(int deltaTime) {
    time = time - 1;
    if (time <= 0){
    	BulletStop();
    }
	}
	
	/**
	 * Stops the bullets from firing
	 */
	public void BulletStop() {
		bullet = false;
	}
	
	/**
	 * 
	 * @return true if the bullet is firing, false if not
	 */
	public boolean getBulletState() {
		return bullet;
	}
	   
	public void display(GL gl) {
		GLUT glut = new GLUT();
		    float wallColour[] = { 0.5f, 10.0f, 30.0f, 1.0f };			
	        gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
		
	    gl.glPushMatrix();
	    gl.glTranslated(getLocationX(), getLocationY(), getLocationZ());
		
	    glut.glutSolidSphere(0.05, 10, 10);
	    gl.glPopMatrix();
	    
	}

	
}
