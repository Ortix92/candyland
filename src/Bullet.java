import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Bullet extends GameObject {

	private double startx;
	private double startz;
	private double starty;
	private double speed;
	private double horAngle;
	private double verAngle;
	private boolean bullet;
	
	public Bullet(double x, double y, double z, double HorAngle, double VerAngle) {
	super( x,y,z );
	startx = x;
	starty = y;
	startz = z;
	speed = 0.05;
	horAngle = HorAngle;
	verAngle = VerAngle;
	bullet = true;
	}
	
	
	public void update(int deltaTime) {
//		if (getLocationX() <= startx + 15.0 && getLocationY() <= starty + 15.0 && getLocationZ() <= startz + 15.0
//				&& getLocationX() >= startx - 15.0 && getLocationY() >= starty - 15.0 && getLocationZ() >= startz - 15.0) {
//			setLocationX(getLocationX() - Math.sin(Math.toRadians(horAngle))*speed);
//			setLocationZ(getLocationZ() - Math.cos(Math.toRadians(horAngle))*speed);
//			setLocationY(getLocationY() + 2*Math.sin(Math.toRadians(verAngle))*speed); 	
//		} else {
//		BulletStop();
//			
//		}
	}
	
	public void BulletStop() {
		bullet = false;
	}
	
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
