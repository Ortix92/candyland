import java.awt.Font;

import javax.media.opengl.GL;
import com.sun.opengl.util.j2d.TextRenderer;

public class PauseMenu {
	
	private double screenWidth = 1024;
	private double screenHeight = 768;

	public void display(GL gl) {
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glColor4d(1, 0, 0, 1); // red 
				gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 80, screenHeight -80);
		gl.glVertex2d(screenWidth - 80, screenHeight -80);
		gl.glVertex2d( screenWidth - 80 , 0 + 80);
		gl.glVertex2d (0 + 80 , 0 + 80);
		gl.glEnd();
		
		gl.glColor4d(1, 0.55, 0.0, 1); // orange
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 85, screenHeight -85);
		gl.glVertex2d(screenWidth - 85, screenHeight -85);
		gl.glVertex2d( screenWidth - 85 , 0 + 85);
		gl.glVertex2d (0 + 85 , 0 + 85);
		gl.glEnd();
		
		gl.glColor4d(1 ,1, 0, 1); // yellow
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 90, screenHeight -90);
		gl.glVertex2d(screenWidth - 90, screenHeight -90);
		gl.glVertex2d( screenWidth - 90 , 0 + 90);
		gl.glVertex2d (0 + 90 , 0 + 90);
		gl.glEnd();
		
		gl.glColor4d(0.5, 1, 0, 1); // green
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 95, screenHeight -95);
		gl.glVertex2d(screenWidth - 95, screenHeight -95);
		gl.glVertex2d( screenWidth - 95 , 0 + 95);
		gl.glVertex2d (0 + 95 , 0 + 95);
		gl.glEnd();
		
		gl.glColor4d(0, 0.75, 1, 1); // blue
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 100, screenHeight -100);
		gl.glVertex2d(screenWidth - 100, screenHeight -100);
		gl.glVertex2d( screenWidth - 100 , 0 + 100);
		gl.glVertex2d (0 + 100 , 0 + 100);
		gl.glEnd();
		
		gl.glColor4d(0.6, 0.2, 0.8, 1); // purple
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 105, screenHeight -105);
		gl.glVertex2d(screenWidth - 105, screenHeight -105);
		gl.glVertex2d( screenWidth - 105 , 0 + 105);
		gl.glVertex2d (0 + 105 , 0 + 105);
		gl.glEnd();	
		
		gl.glColor4d(.1, .1, .1 , 1); // grey 
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0 + 110, screenHeight -110);
		gl.glVertex2d(screenWidth - 110, screenHeight -110);
		gl.glVertex2d( screenWidth - 110 , 0 + 110);
		gl.glVertex2d (0 + 110 , 0 + 110);
		gl.glEnd();
		
		gl.glColor4d(0.0, 0.0, 0.0, 0.2);
		Font f = new Font("SansSerif", Font.BOLD, 36);
		TextRenderer tr = new TextRenderer(f);
		tr.beginRendering((int) screenWidth, (int) screenHeight);
		tr.draw("Pauze", (int) (screenWidth/2 - 50), (int) (screenHeight -200));
		Font f2 = new Font("SansSerif", Font.ITALIC, 20);
		tr.draw("Press ESC to continue", (int) (screenWidth/2 - 200), (int) (screenHeight -300));
		Font f3 = new Font("SansSerif", Font.ITALIC, 15);
		tr.draw("Press ENTER to go back to the Main Menu", (int) (screenWidth/2 - 360), (int) (screenHeight -400));
		Font f4 = new Font("SansSerif", Font.ITALIC, 5);
		tr.draw("current game will be lost", (int) (screenWidth/2 - 220), (int) (screenHeight -450));
		tr.endRendering();

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
}
