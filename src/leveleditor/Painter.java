package leveleditor;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;

import com.sun.opengl.util.Animator;

/**
 * A frame for us to draw on using OpenGL.
 * 
 * @author Kang
 * 
 */
public class Painter extends GLJPanel implements GLEventListener, MouseListener {
	static final long serialVersionUID = 7526471155622776147L;

	// Screen size.
	private int screenWidth = 300, screenHeight = 300;
	private float buttonSize = screenHeight / 10.0f;

	// A GLCanvas is a component that can be added to a frame. The drawing
	// happens on this component.
	private GLCanvas canvas;

	private static final byte DM_POINT = 0;
	private static final byte DM_LINE = 1;
	private static final byte DM_KOCH = 2;
	private byte drawMode = DM_POINT;

	private ArrayList<Point2D.Float> points;

	/**
	 * When instantiating, a GLCanvas is added for us to play with. An animator
	 * is created to continuously render the canvas.
	 */
	public Painter() {

		points = new ArrayList<Point2D.Float>();

		// Set the desired size and background color of the frame
		setSize(screenWidth, screenHeight);
		// setBackground(Color.white);
		setBackground(new Color(0.95f, 0.95f, 0.95f));


		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// Create a GLCanvas with the specified capabilities and add it to this
		// frame. Now, we have a canvas to draw on using JOGL.
		canvas = new GLCanvas(caps);
		add(canvas);

		// Set the canvas' GL event listener to be this class. Doing so gives
		// this class control over what is rendered on the GL canvas.
		canvas.addGLEventListener(this);

		// Also add this class as mouse listener, allowing this class to react
		// to mouse events that happen inside the GLCanvas.
		canvas.addMouseListener(this);

		// An Animator is a JOGL help class that can be used to make sure our
		// GLCanvas is continuously being re-rendered. The animator is run on a
		// separate thread from the main thread.
		Animator anim = new Animator(canvas);
		anim.start();

		// With everything set up, the frame can now be displayed to the user.
		setVisible(true);
	}

	@Override
	/**
	 * A function defined in GLEventListener. It is called once, when the frame containing the GLCanvas 
	 * becomes visible. In this assignment, there is no moving ´camera´, so the view and projection can 
	 * be set at initialization. 
	 */
	public void init(GLAutoDrawable drawable) {
		// Retrieve the OpenGL handle, this allows us to use OpenGL calls.
		GL gl = drawable.getGL();

		// Set the matrix mode to GL_PROJECTION, allowing us to manipulate the
		// projection matrix
		gl.glMatrixMode(GL.GL_PROJECTION);

		// Always reset the matrix before performing transformations, otherwise
		// those transformations will stack with previous transformations!
		gl.glLoadIdentity();

		/*
		 * glOrtho performs an "orthogonal projection" transformation on the
		 * active matrix. In this case, a simple 2D projection is performed,
		 * matching the viewing frustum to the screen size.
		 */
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);

		// Set the matrix mode to GL_MODELVIEW, allowing us to manipulate the
		// model-view matrix.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// We leave the model view matrix as the identity matrix. As a result,
		// we view the world 'looking forward' from the origin.
		gl.glLoadIdentity();

		// We have a simple 2D application, so we do not need to check for depth
		// when rendering.
		gl.glDisable(GL.GL_DEPTH_TEST);
	}

	@Override
	/**
	 * A function defined in GLEventListener. This function is called many times per second and should 
	 * contain the rendering code.
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the buttons.
		//drawButtons(gl);

		// Draw a figure based on the current draw mode and user input
		//drawFigure(gl);

		// Flush the OpenGL buffer, outputting the result to the screen.
		gl.glFlush();
	}

	/**
	 * A method that draws the top left buttons on the screen.
	 * 
	 * @param gl
	 */
	private void drawButtons(GL gl) {
		// Draw the background boxes
		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 0.0f, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 2 * buttonSize, screenHeight - buttonSize, buttonSize);

		// Draw a point on top of the first box
		gl.glPointSize(5.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		pointOnScreen(gl, buttonSize / 2.0f, screenHeight - buttonSize / 2.0f);

		// Draw a line on top of the second box.
		gl.glLineWidth(3);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		lineOnScreen(gl, buttonSize + 4.0f, screenHeight - 4.0f,
				2 * buttonSize - 4.0f, screenHeight - buttonSize + 4.0f);

		// Draw a triangle on top of the third box.
		gl.glLineWidth(2);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		triangleOnScreen(gl, buttonSize * 2.5f, screenHeight - 4.0f,
				3 * buttonSize - 4.0f, screenHeight - buttonSize + 4.0f,
				2 * buttonSize + 4.0f, screenHeight - buttonSize + 4.0f);
	}

	private void triangleOnScreen(GL gl, float x1, float y1, float x2,
			float y2, float x3, float y3) {
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glVertex2f(x3, y3);
		gl.glEnd();

	}

	/**
	 * A method that draws a figure, when the user has inputted enough points
	 * for the current draw mode.
	 * 
	 * @param gl
	 */
	private void drawFigure(GL gl) {
		// Set line and point size, and set color to black.
		gl.glLineWidth(3);
		gl.glPointSize(10.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		Point2D.Float p1, p2, p3;
		switch (drawMode) {
		case DM_POINT:
			if (points.size() >= 1) {
				// If the draw mode is "point" and the user has supplied at
				// least one point, draw that point.
				p1 = points.get(0);
				pointOnScreen(gl, p1.x, p1.y);
			}
		case DM_LINE:
			if (points.size() >= 2) {
				// If the draw mode is "line" and the user has supplied at least
				// two points, draw a line between those points
				p1 = points.get(0);
				p2 = points.get(1);
				lineOnScreen(gl, p1.x, p1.y, p2.x, p2.y);
			}
		case DM_KOCH:
			if (points.size() >= 3) {
				p1 = points.get(0);
				p2 = points.get(1);
				p3 = points.get(2);

				gl.glBegin(GL.GL_LINE_LOOP);
				gl.glVertex2d(p1.getX(), p1.getY());
				gl.glVertex2d(p2.getX(), p2.getY());
				gl.glVertex2d(p3.getX(), p3.getY());
				gl.glEnd();

				double x1 = p1.getX();
				double y1 = p1.getY();

				double x2 = p2.getX();
				double y2 = p2.getY();

				double x3 = p3.getX();
				double y3 = p3.getY();

				//triangleOnScreen(gl, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
				drawKoch(gl, x1, y1, x2, y2, 4);
				drawKoch(gl, x2, y2, x3, y3, 4);
				drawKoch(gl, x3, y3, x1, y1, 4);
			}
			break;
		}
	}

	/**
	 * Method which draws the KOCH figure after three points have been selected
	 */

	public void drawKoch(GL gl, double x1, double y1, double x2, double y2,
			int n) {
		if (n <= 0) {
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(x1, y1);
			gl.glVertex2d(x2, y2);
			gl.glEnd();
		} else {
			// The new level
			int level = n - 1;
			double u1 = (x2 - x1) / 3 + x1;
			double v1 = (y2 - y1) / 3 + y1;
			double u3 = (x2 - x1) * 2 / 3 + x1;
			double v3 = (y2 - y1) * 2 / 3 + y1;
			double u2 = (u3-u1) * Math.cos(Math.toRadians(-60)) - (v3-v1)
					* Math.sin(Math.toRadians(-60))+u1;
			double v2 = (u3-u1) * Math.sin(Math.toRadians(-60)) + (v3-v1)
					* Math.cos(Math.toRadians(-60))+v1;

			drawKoch(gl, x1, y1, u1, v1, level);
			drawKoch(gl, u1, v1, u2, v2, level);
			drawKoch(gl, u2, v2, u3, v3, level);
			drawKoch(gl, u3, v3, x2, y2, level);

		}
	}

	/**
	 * Help method that uses GL calls to draw a point.
	 */
	private void pointOnScreen(GL gl, float x, float y) {
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2f(x, y);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a line.
	 */
	private void lineOnScreen(GL gl, float x1, float y1, float x2, float y2) {
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a square
	 */
	private void boxOnScreen(GL gl, float x, float y, float size) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + size, y);
		gl.glVertex2f(x + size, y + size);
		gl.glVertex2f(x, y + size);
		gl.glEnd();
	}

	@Override
	/**
	 * A function defined in GLEventListener. This function is called when there is a change in certain 
	 * external display settings. 
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// Not needed.
	}

	@Override
	/**
	 * A function defined in GLEventListener. This function is called when the GLCanvas is resized or moved. 
	 * Since the canvas fills the frame, this event also triggers whenever the frame is resized or moved.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();

		// Set the new screen size and adjusting the viewport
		screenWidth = width;
		screenHeight = height;
		buttonSize = height / 10.0f;
		gl.glViewport(0, 0, screenWidth, screenHeight);

		// Update the projection to an orthogonal projection using the new
		// screen size
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);
	}

	@Override
	/**
	 * A function defined in MouseListener. Is called when the pointer is in the GLCanvas, and a mouse button is released.
	 */
	public void mouseReleased(MouseEvent me) {
		// Check if the coordinates correspond to any of the top left buttons
		boolean buttonPressed = false;
		if (me.getY() < buttonSize) {
			if (me.getX() < buttonSize) {
				// The first button is clicked
				points.clear();
				drawMode = DM_POINT;
				System.out.println("Draw mode: DRAW_POINT");
				buttonPressed = true;
			} else if (me.getX() < 2 * buttonSize) {
				// The second button is clicked
				points.clear();
				drawMode = DM_LINE;
				System.out.println("Draw mode: DRAW_LINE");
				buttonPressed = true;
			} else if (me.getX() < 3 * buttonSize) {
				// The Third button is clicked
				points.clear();
				drawMode = DM_KOCH;
				System.out.println("Draw mode: DRAW_KOCH");
				buttonPressed = true;
			}
		}

		// Only register a new point, if the click did not hit any button
		if (!buttonPressed) {

			if (drawMode == DM_POINT && points.size() >= 1) {
				// If we're drawing points and one point was stored, reset the
				// points list
				points.clear();
			} else if (drawMode == DM_LINE && points.size() >= 2) {
				// If we're drawing lines and two points were already stored,
				// reset the points list
				points.clear();
			} else if (drawMode == DM_KOCH && points.size() >= 3) {
				// If we're drawing lines and three points were already stored,
				// reset the points list
				points.clear();
			}

			// Add a new point to the points list.
			points.add(new Point2D.Float(me.getX(), screenHeight - me.getY()));
			System.out.println(me.getX() + " " + (screenHeight - me.getY()));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Not needed.
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Not needed.

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Not needed.

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Not needed.

	}
}