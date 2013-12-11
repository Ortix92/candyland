import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Weapon extends GameObject {

	private Control control;
	private boolean shooting = false;
	private int counter = 0;
	private float Yoffset = 0;
	private float Xoffset = 0;
	private boolean Left = false;
	private boolean Right = false;
	private boolean Up = false;
	private boolean Down = false;
	private int rateoffire;
	private int firecounter = 0;

	public Weapon(int rateoffire) {
		this.rateoffire = 0;
	}

	public void setshooting(boolean state) {
		shooting = state;
	}

	public void setControl(Control control) {
		this.control = control;
	}
	
	public boolean Canshoot() {
		if (control.getShoot()) {
			firecounter = firecounter + 1;
			if (firecounter >= rateoffire) {
				firecounter = 0;
				return true;
			}
		}
		return false;
	}

	public void Weaponsway() {
		if (control.getForward()) {
			counter = counter + 1;
			if (counter > 0 && counter <= 20) {
				Right = true;
				Up = true; }
			if (counter > 20 && counter <= 40) {
				Right = false;
				Up = false;
				Left = true;
				Down = true;
			}
			if (counter > 40 && counter <= 60) {
				Down = false;
				Up = true;
			}
			if (counter > 60 && counter <= 80) {
				Down = true;
				Up = false;
				Left = false;
				Right = true;
			}
			if (counter > 80) {
				Down = false;
				Up = false;
				Left = false;
				Right = false;
				counter = 0;
			} }
			else if (control.getBack()) {
				counter = counter + 1;
				if (counter > 0 && counter <= 20) {
					Left = true;
					Up = true; }
				if (counter > 20 && counter <= 40) {
					Left = false;
					Up = false;
					Right = true;
					Down = true;
				}
				if (counter > 40 && counter <= 60) {
					Down = false;
					Up = true;
				}
				if (counter > 60 && counter <= 80) {
					Down = true;
					Up = false;
					Right = false;
					Left = true;
				}
				if (counter > 80) {
					Down = false;
					Up = false;
					Right = false;
					Left = false;
					counter = 0;
				}
		}	else {
				Down = false;
				Up = false;
				Left = false;
				Right = false;
				counter = 0;
			}

	}

	public void Calcoffset() {
		if (Right) {
			Xoffset = Xoffset + 0.0005f;
		}
		if (Left) {
			Xoffset = Xoffset - 0.0005f;
		}
		if (Up) {
			Yoffset = Yoffset - 0.0006f;
		}
		if (Down) {
			Yoffset = Yoffset + 0.0006f;
		} else {
			Xoffset = 0;
			Yoffset = 0;
		}
	}

	public void display(GL gl) {
		if (control.getShoot()) {
			GLUT glut = new GLUT();
			float CColour[] = { 34.0f, 0.1f, 0.2f, 1.0f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, CColour, 0);

			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glTranslatef(0.1f, -0.25f, -0.2f);
			glut.glutSolidSphere(0.05, 50, 40);
			gl.glLoadIdentity();
			gl.glTranslatef(-0.1f, -0.25f, -0.2f);
			glut.glutSolidSphere(0.05, 50, 40);

			float CColour2[] = { 0.4f, 22.1f, 16.2f, 1.0f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, CColour2, 0);
			gl.glLoadIdentity();
			gl.glTranslatef(0.13f, -0.22f, -0.5f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);
			gl.glTranslatef(-0.14f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.005, 1.0, 10, 10);

			gl.glPopMatrix();
			gl.glEnable(GL.GL_DEPTH_TEST);

		} else {
			GLUT glut = new GLUT();
			float CColour[] = { 34.0f, 0.1f, 0.2f, 1.0f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, CColour, 0);
			Weaponsway();
			Calcoffset();
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			if (control.getRight()) {
				gl.glRotated(-5,0 ,0 , 1);
			}
			if (control.getLeft()) {
				gl.glRotated(5,0 ,0 , 1);
			}
			gl.glTranslatef(0.1f + Xoffset, -0.25f + Yoffset, -0.4f);
			glut.glutSolidSphere(0.05, 50, 40);
			gl.glTranslatef(-0.2f + Xoffset, 0.0f, 0.0f);
			glut.glutSolidSphere(0.05, 50, 40);

			float CColour2[] = { 0.4f, 22.1f, 16.2f, 1.0f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, CColour2, 0);
			gl.glLoadIdentity();
			if (control.getRight()) {
				gl.glRotated(-5,0 ,0 , 1);
			}
			if (control.getLeft()) {
				gl.glRotated(5,0 ,0 , 1);
			}
			gl.glTranslatef(0.13f + Xoffset, -0.22f + Yoffset, -0.7f);
			//gl.glRotatef(-2,0,1,0);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);
			//gl.glRotatef(2,0,1,0);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);
			//gl.glRotatef(2,0,1,0);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);
			//gl.glRotatef(-4,0,1,0);
			gl.glTranslatef(-0.14f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);
			//gl.glRotatef(2,0,1,0);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);
			//gl.glRotatef(2,0,1,0);
			gl.glTranslatef(-0.03f, 0.0f, 0.0f);
			glut.glutSolidCylinder(0.007, 1.0, 10, 10);

			gl.glPopMatrix();
			gl.glEnable(GL.GL_DEPTH_TEST);
		}
	}

	public boolean update(int deltaTime, Player player, Camera camera, jbullet phworld) {
		if (control != null) {
			control.update();

			Weaponsway();

			if (control.getShoot()) {
				
				if (Canshoot()) {						
				return true;
					}
				
			}
			
		}
		return false;
	}
}
