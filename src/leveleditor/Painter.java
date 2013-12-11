package leveleditor;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.swing.JPanel;

import com.sun.opengl.util.Animator;

/**
 * A frame for us to draw on using OpenGL.
 * 
 * @author Kang
 * 
 */
public class Painter extends JPanel implements GLEventListener, MouseListener,
		MouseMotionListener {
	static final long serialVersionUID = 7526471155622776147L;

	// Screen size.
	private int screenWidth, screenHeight;
	private float buttonSize = screenHeight / 10.0f;
	// A GLCanvas is a component that can be added to a frame. The drawing
	// happens on this component.
	private GLCanvas canvas;

	private static final byte DM_POINT = 0;
	private static final byte DM_LINE = 1;
	private static final byte DM_KOCH = 2;
	private byte drawMode = DM_POINT;
	private ArrayList<Point2D.Float> points;

	private int resolution;

	private boolean drawMap;

	private ArrayList<ArrayList<Integer>> maze;

	private int boxSize;

	private boolean drawGrid;

	/**
	 * When instantiating, a GLCanvas is added for us to play with. An animator
	 * is created to continuously render the canvas.
	 */
	public Painter(int width, int height) {
		screenWidth = width;
		screenHeight = height;
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

		// Manually set size
		canvas.setSize(screenWidth, screenHeight);
		add(canvas);

		// Set the canvas' GL event listener to be this class. Doing so gives
		// this class control over what is rendered on the GL canvas.
		canvas.addGLEventListener(this);

		// Also add this class as mouse listener, allowing this class to react
		// to mouse events that happen inside the GLCanvas.
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

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
	 * becomes visible. In this assignment, there is no moving �camera�, so the view and projection can 
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
		gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the grid
		drawGrid(gl);

		// Draw the Map
		drawMap(gl);

		// Flush the OpenGL buffer, outputting the result to the screen.
		gl.glFlush();
	}

	/**
	 * Set maze resolution
	 * 
	 * @param resolution
	 *            simple integer which sets the resolution
	 */
	public void setResolution(int resolution) throws IllegalArgumentException {
		if (resolution % 5 == 0) {
			this.resolution = resolution;
		} else {
			throw new IllegalArgumentException(
					"Maze needs resolution of multiplicity 5");
		}
	}

	/**
	 * Set the maze array
	 * 
	 * @param maze
	 *            the maze is a 2x2 ArrayList of Integers
	 */
	public void setMaze(ArrayList<ArrayList<Integer>> maze) {
		this.maze = maze;
		this.setResolution(this.maze.size());
	}

	public ArrayList<ArrayList<Integer>> getMaze() {
		return this.maze;
	}

	/**
	 * Draw the grid based on the resolution of the matrix (can be set by user)
	 * 
	 * @param gl
	 */
	private void drawGrid(GL gl) {
		// size of the grid (how many vertical and horizontal lines)
		gl.glLineWidth(1);
		if (this.drawMap || this.drawGrid) {
			float x1 = this.screenWidth / this.resolution;
			float y1 = this.screenHeight / this.resolution;
			for (int i = 0; i < resolution; i++) {
				this.lineOnScreen(gl, 0, y1, this.screenWidth, y1);
				y1 = y1 + (this.screenHeight / this.resolution);
				this.lineOnScreen(gl, x1, 0, x1, this.screenHeight);
				x1 = x1 + (this.screenWidth / this.resolution);
			}
		}
	}

	/**
	 * Draw the map in case the drawMap bool value has been set to true
	 * 
	 * @param gl
	 *            the OpenGL object
	 */
	private void drawMap(GL gl) {
		if (this.drawMap) {
			boxSize = this.screenWidth / this.resolution;
			for (int i = 0; i < maze.size(); i++) {
				for (int j = 0; j < maze.size(); j++) {
					if (this.maze.get(i).get(j) != 0) {
						boxOnScreen(gl, getXLocationBlock(j),
								getYLocationBlock(i), boxSize);
					}
				}
			}
		}

	}

	/**
	 * @param i
	 *            iterator
	 * @return the Y location of the maze tile
	 */
	private float getYLocationBlock(int i) {
		i++; // add 1 to the iterator to shift down by a single row
		return this.screenHeight - i * this.screenHeight / this.resolution;
	}

	/**
	 * 
	 * @param i
	 *            iterator
	 * @return the X location of the maze tile
	 */
	private float getXLocationBlock(int i) {
		return i * this.screenWidth / this.resolution;
	}

	/**
	 * Toggle the value of the tile
	 * 
	 * @param points2
	 */

	private void changeTile(boolean isDragged) {
		if (this.drawMap) {

			int i = 0;
			// Look for the x Coordinate of the tile
			boolean tileXFound = false;
			int tileX = 0;
			while (!tileXFound) {
				if (points.get(0).x > i * boxSize
						&& points.get(0).x <= (i + 1) * boxSize) {
					tileX = i;
					tileXFound = true;
				} else if (i > resolution) {
					System.out.println("Error processing tile change in X");
					break;
				} else {
					i++;
				}
			}

			// Look for the y Coordinate of the tile
			boolean tileYFound = false;
			int tileY = 0;
			i = 0;
			while (!tileYFound) {
				if (points.get(0).y > i * boxSize
						&& points.get(0).y <= (i + 1) * boxSize) {
					tileY = i;
					tileYFound = true;
				} else if (i > resolution) {
					System.out.println("Error processing tile change in Y");
					break;
				} else {
					i++;
				}
			}
			System.out.println((this.maze.size() - 1 - tileY) + " " + tileX);
			this.maze.get(this.maze.size() - 1 - tileY).set(tileX,
					1 - this.maze.get(this.maze.size() - 1 - tileY).get(tileX));
		}

	}

	/**
	 * Sets the map listener so the map starts drawing
	 * 
	 * @param draw
	 *            true will allow the map to be drawn, false will prevent that
	 *            from happening
	 */
	public void setDrawMapListener(boolean draw) {
		this.drawMap = draw;
	}

	/**
	 * Sets the listener of the drawGrid method to either start or stop drawing
	 * it
	 * 
	 * @param draw
	 *            true will draw the grid. False will stop drawing it
	 */
	public void setDrawGridListener(boolean draw) {
		this.drawGrid = draw;
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

		// Add a new point to the points list.
		points.add(new Point2D.Float(me.getX(), screenHeight - me.getY()));
		this.changeTile(false);

		// Clear points after tile has been set
		points.clear();

		// System.out.println(me.getX() + " " + (screenHeight - me.getY()));

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

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		points.add(new Point2D.Float(event.getX(), screenHeight - event.getY()));
		this.changeTile(true);

		// Clear points after tile has been set
		points.clear();
	}
}