import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Bullet {

	private int time;
	private boolean bullet;
	
	public Bullet() {
	bullet = true;
	time = 80;
	}
	
	
	public void update() {
    time = time - 1;
    if (time <= 0){
    	BulletStop();
    }
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
	//    gl.glTranslated(getLocationX(), getLocationY(), getLocationZ());
		
	    glut.glutSolidSphere(0.05, 10, 10);
	    gl.glPopMatrix();
	    
	}

	
}
