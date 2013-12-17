import java.awt.Color;
import java.awt.Font;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;

import com.sun.opengl.util.j2d.TextRenderer;

public class GameOverScreen {
	
	private double screenWidth = 1024;
	private double screenHeight = 768;

	public void display(GL gl) {
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);

		
		gl.glColor4d(.1, .1, .1 , 1); // grey 
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0, screenHeight);
		gl.glVertex2d(screenWidth, screenHeight);
		gl.glVertex2d( screenWidth, 0);
		gl.glVertex2d (0, 0);
		gl.glEnd();
		
		gl.glColor4d(0.0, 0.0, 0.0, 0.2);
		Font f = new Font("SansSerif", Font.BOLD, 36);
		TextRenderer tr = new TextRenderer(f);
		tr.beginRendering((int) screenWidth, (int) screenHeight);
		tr.draw("JE BENT DOOD", (int) (screenWidth/2 - 150), (int) (screenHeight -200));
		Font f2 = new Font("SansSerif", Font.ITALIC, 20);
		tr.draw("", (int) (screenWidth/2 - 200), (int) (screenHeight -300));
		Font f3 = new Font("SansSerif", Font.ITALIC, 15);
		tr.draw("", (int) (screenWidth/2 - 360), (int) (screenHeight -400));
		Font f4 = new Font("SansSerif", Font.ITALIC, 5);
		tr.draw("PRESS ENTER TO RETURN TO MAIN MENU", (int) (screenWidth/2 - 400), (int) (screenHeight -450));
		tr.endRendering();

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
	
}
		
}
