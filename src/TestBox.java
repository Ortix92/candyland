import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class TestBox extends GameObject {
	private double hp;

	
	public TestBox(double x, double y, double z) {
		super(x, y, z);
		hp = 100;
	}
	
	public void setHealth(double x) {
		hp = x;
	}
	
	public double getHealth() {
		return hp;
	}
	
	public void display(GL gl) {
	if (hp >= 0) {	
		gl.glColor3f(0.8f,1,0.6f);
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		gl.glTranslated(getLocationX(), getLocationY(), getLocationZ());
		glut.glutSolidCube(1.5f);
		gl.glPopMatrix();
	}}
}
