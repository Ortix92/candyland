import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;

import com.sun.opengl.util.Animator;


public class ScoreScreen implements GLEventListener {
	public static int gameover;
	private SQL connection;
	private GameOverScreen screen = new GameOverScreen();
	public static String naam;
	public static String letter = null;
	public GLCanvas canvas;
	private int screenWidth = MazeRunner.screenWidth;
	private int screenHeight = MazeRunner.screenHeight;
	public static UserInput input; 
	public static int score;
	public static boolean dood;
	
public ScoreScreen(){
	initJOGL();
	connection = new SQL();
	input = new UserInput(canvas);
	//gameover = i;
	naam = "";
}

public void setScore(int i) {
	score = i;
}

public void setState(int i) {
	gameover = i;
}

public int getState() {
	return gameover;
}

private void initJOGL() {
	GLCapabilities caps = new GLCapabilities();
	
	caps.setDoubleBuffered(true);
	caps.setHardwareAccelerated(true);

	canvas = new GLCanvas(caps);
	/*
	 * We need to add a GLEventListener to interpret OpenGL events for us.
	 * Since MazeRunner implements GLEventListener, this means that we add
	 * the necesary init(), display(), displayChanged() and reshape()
	 * methods to this class. These will be called when we are ready to
	 * perform the OpenGL phases of MazeRunner.
	 */
	canvas.addGLEventListener(this);
	canvas.setSize(screenWidth, screenHeight);
	/*
	 * We need to create an internal thread that instructs OpenGL to
	 * continuously repaint itself. The Animator class handles that for
	 * JOGL.
	 */
	Animator anim = new Animator(canvas);
	anim.start();
	// makes an image of 1 by 1 pixel, type:
	// Represents an image with 8-bit RGBA color components packed into
	// integer pixels.
	Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	// makes the created 1 by 1 pixel into cursor image, with default
	// location 0,0
	Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image,
			new Point(0, 0), "name");
	// sets the cursor in the canvas to the created cursor.
	canvas.setCursor(cursor);
}

public void init(GLAutoDrawable drawable) {
	drawable.setGL(new DebugGL(drawable.getGL())); // We set the OpenGL
													// pipeline to Debugging
													// mode.
	GL gl = drawable.getGL();

	gl.glClearColor(1, 0, 1, 0); // Set the background color.

	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glPushMatrix();
	gl.glLoadIdentity();
	gl.glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glPushMatrix();
	gl.glLoadIdentity();	
	
}

public void display(GLAutoDrawable drawable) {
	
	GL gl = drawable.getGL();
	
	if (gameover == 1) {
		screen.setdood(true);
		screen.display(gl, naam);
		if (letter != null) {
		naam = naam + letter;
		letter = null; }
		if (input.backspace) {
			removeLetter();
		}
	}
//	if (gameover == 1 && !dood) {
//		screen.setdood(false);
//		screen.display(gl, naam);
//		if (letter != null) {
	//	naam = naam + letter;
//		letter = null; }
//		if (input.backspace) {
//			removeLetter();
//		}
//	}
	if (gameover == 2) {
		screen.setdood(false);
		screen.display(gl, naam);
		if (letter != null) {
		naam = naam + letter;
		letter = null; }
		if (input.backspace) {
			removeLetter();
		}
		
	}
	if (gameover == 0) {
		
		connection.GetHighScores(gl);	
	}
	
	gl.glLoadIdentity();
	// Flush the OpenGL buffer.
	gl.glFlush();
}

public void AddHighScore() {
	connection.AddHighScore(naam, score);
}

public static void removeLetter(){
	 if (naam.length() > 0) {
		    naam = naam.substring(0, naam.length()-1);
		  }
}

@Override
public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	// TODO Auto-generated method stub
	
}

@Override
public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	// TODO Auto-generated method stub
	
}


}
