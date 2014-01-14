import java.awt.Color;
import java.awt.Font;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;

import com.sun.opengl.util.j2d.TextRenderer;

public class GameOverScreen {

	private boolean dood;
	private double screenWidth = MazeRunner.screenWidth;
	private double screenHeight = MazeRunner.screenHeight;

	public void setdood(boolean a) {
		dood = a;
	}

	public void display(GL gl, String naam) {
		if (dood) {
			gl.glColor4d(1, 0, .1, 1);
		}
		if (!dood) {
			gl.glColor4d(0, 0, 1, 1);
		}
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(0, screenHeight);
		gl.glVertex2d(screenWidth, screenHeight);
		gl.glVertex2d(screenWidth, 0);
		gl.glVertex2d(0, 0);
		gl.glEnd();

		gl.glColor4d(0.0, 0.0, 0.0, 0.2);
		Font f = new Font("SansSerif", Font.BOLD, 36);
		TextRenderer tr = new TextRenderer(f);
		tr.beginRendering((int) screenWidth, (int) screenHeight);
		if (dood) {
			tr.draw("JE BENT DOOD", (int) (screenWidth / 2 - 150),
					(int) (screenHeight - 200));
		}
		if (!dood) {
			tr.draw("YOU ARE WINNER", (int) (screenWidth / 2 - 150),
					(int) (screenHeight - 200));
		}
		tr.draw("Enter your name: " + naam, (int) (screenWidth / 2 - 360),
				(int) (screenHeight - 400));
		tr.draw("PRESS ENTER TO RETURN TO MAIN MENU",
				(int) (screenWidth / 2 - 400), (int) (screenHeight - 450));
		tr.endRendering();
	}

}
