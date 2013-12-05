import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;

public class PauseMenu {
	
	private double screenWidth = 400;
	private double screenHeight = 400;

	public void display(GL gl) {
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glColor4d(1.0, 1.0, 1.0, 0.2);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(screenWidth / 100.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0 - 5
				* 100);
		gl.glVertex2d(screenWidth / 100.0,
				screenHeight - 10.0 - 5 * 100);

		gl.glEnd();

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
}
