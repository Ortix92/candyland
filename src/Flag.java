import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Flag extends GameObject implements VisibleObject{
	float[] Color={ (float) Math.random(), (float) Math.random(),
			(float) Math.random(), (float) Math.random() };
	Player player;
	static Boolean Flag=false;
	double i=0;
	
	public Flag(double X,double Y, double Z, Player player){
		super(X,Y,Z);
		this.player=player;
	}

	@Override
	public void display(GL gl) {
		GLUT glut=new GLUT();
		gl.glPushMatrix();
		gl.glTranslated(getLocationX(), 5, getLocationZ());
		gl.glRotated(90,1,0,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, Color, 0);
		glut.glutSolidCylinder(0.2, 15, 10, 10);
		gl.glRotated(-90,1,0,0);
		//gl.glTranslated(0, 5, 0);
		glut.glutSolidSphere(0.3, 10, 10);
		gl.glTranslated(0, -1-i, 0);
		if(Flag){
			if(i<4){
				i=i+0.01;
			}			
		}
		drawFlag(gl);
		gl.glPopMatrix();
		//System.out.println(i);
		
		
		
	}
	public void update(){
		if(Math.abs(this.getLocationX()-player.getLocationX())<10*player.getSpeed()){
			if(Math.abs(this.getLocationZ()-player.getLocationZ())<10*player.getSpeed()){
				System.out.println("Flag!");
				Flag=true;
			}
			
		}
	}
	public void drawFlag(GL gl) {
		Textureloader.Flag.enable();
		Textureloader.Flag.bind();
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0,0);
			gl.glVertex3f(0, 0, 0);
			gl.glTexCoord2f(1,0);
			gl.glVertex3f(0,1,0);
			gl.glTexCoord2f(1,1);
			gl.glVertex3f(0,0.5f,1);
			gl.glTexCoord2f(0,1);
			gl.glVertex3f(0, 0, 0);
			gl.glEnd();
			
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0,0);
			gl.glVertex3f(0, 0, 0);
			gl.glTexCoord2f(1,1);
			gl.glVertex3f(0,0.5f,1);
			gl.glTexCoord2f(1,0);
			gl.glVertex3f(0,1,0);
			gl.glTexCoord2f(0,1);
			gl.glVertex3f(0, 0, 0);
					
			gl.glEnd();
			Textureloader.Flag.disable();
		
		
	}

}
