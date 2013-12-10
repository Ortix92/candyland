import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import loader.Model;
import loader.OBJLoader;

/**
 * MazeRunner is the base class of the game, functioning as the view controller
 * and game logic manager.
 * <p>
 * Functioning as the window containing everything, it initializes both JOGL,
 * the game objects and the game logic needed for MazeRunner.
 * <p>
 * For more information on JOGL, visit <a
 * href="http://jogamp.org/wiki/index.php/Main_Page">this page</a> for general
 * information, and <a
 * href="https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/">this
 * page</a> for the specification of the API.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 * 
 */
public class MazeRunner implements GLEventListener {
	static final long serialVersionUID = 7526471155622776147L;

	/*
	 * **********************************************
	 * * Local variables * **********************************************
	 */
	public GLCanvas canvas;

	private int screenWidth = 1024, screenHeight = 768; // Screen size.
	private ArrayList<VisibleObject> visibleObjects; // A list of objects that
														// will be displayed on
														// screen.
	private Player player; // The player object.
	private int amountofNyans = 1; // The amount of NyanCats.
	private NyanCat[] Nyan = new NyanCat[amountofNyans]; // The NyanCat object
															// array.
	private Camera camera; // The camera object.
	private UserInput input; // The user input object that controls the player.
	private NyanCatInput NyanInput; // the input that controls Nyan (yet to be
									// determined)
	private Maze maze; // The maze.
	private long previousTime = Calendar.getInstance().getTimeInMillis(); // Used
																			// to
																			// calculate
																			// elapsed
																			// time.
	private Model teapot;
	private Guy guy;
	private Weapon weapon;
	private TestBox box;
	private PauseMenu pause;
	private jbullet phworld;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Control control = null;

	/*
	 * **********************************************
	 * * Initialization methods * **********************************************
	 */
	/**
	 * Initializes the complete MazeRunner game.
	 * <p>
	 * MazeRunner extends Java AWT Frame, to function as the window. It creats a
	 * canvas on itself where JOGL will be able to paint the OpenGL graphics. It
	 * then initializes all game components and initializes JOGL, giving it the
	 * proper settings to accurately display MazeRunner. Finally, it adds itself
	 * as the OpenGL event listener, to be able to function as the view
	 * controller.
	 */
	public MazeRunner() {

		initJOGL(); // Initialize JOGL.
		initObjects(); // Initialize all the objects!
		
	}

	/**
	 * initJOGL() sets up JOGL to work properly.
	 * <p>
	 * It sets the capabilities we want for MazeRunner, and uses these to create
	 * the GLCanvas upon which MazeRunner will actually display our screen. To
	 * indicate to OpenGL that is has to enter a continuous loop, it uses an
	 * Animator, which is part of the JOGL api.
	 */
	private void initJOGL() {
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double
		// buffering.
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// Now we add the canvas, where OpenGL will actually draw for us. We'll
		// use settings we've just defined.
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
	}

	/**
	 * initializeObjects() creates all the objects needed for the game to start
	 * normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li>the default Maze
	 * <li>the Player
	 * <li>the Camera
	 * <li>the User input
	 * </ul>
	 * <p>F
	 * Remember that every object that should be visible on the screen, should
	 * be added to the visualObjects list of MazeRunner through the add method,
	 * so it will be displayed automagically.
	 */
	private void initObjects() {
		// We define an ArrayList of VisibleObjects to store all the objects
		// that need to be
		// displayed by MazeRunner.
		visibleObjects = new ArrayList<VisibleObject>();
		// Add the maze that we will be using.
		maze = new Maze();
		visibleObjects.add(maze);

		NyanInput = new NyanCatInput(canvas);
		// Zorgt dat Nyan beweegt volgens de input in NyanCatInput
		// Initialize the player.
		player = new Player(6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, // x-position
				// 100*Math.tan(35*Math.PI/180),

				maze.SQUARE_SIZE / 2, // y-position
				// 100* 1/Math.tan(35*Math.PI/180),

				3 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, // z-position

				0, 0); // horizontal and vertical angle

		// initialize the NyanCat.
		for (int i = 0; i < amountofNyans; i++) {
			double X = Math.random() * 100 + 6 * maze.SQUARE_SIZE
					+ maze.SQUARE_SIZE / 2; // x-position
			double Z = Math.random() * 100 + 3 * maze.SQUARE_SIZE
					+ maze.SQUARE_SIZE / 2; // z-position
			while (maze.isWall(X, Z)) {
				X = Math.random() * 100 + 6 * maze.SQUARE_SIZE
						+ maze.SQUARE_SIZE / 2; // x-position
				Z = Math.random() * 100 + 3 * maze.SQUARE_SIZE
						+ maze.SQUARE_SIZE / 2; // z-position
			}
			Nyan[i] = new NyanCat(X, maze.SQUARE_SIZE / 4, // y-position
					Z, Math.random() * 360,
					// horizontal angle
					player, maze);

			visibleObjects.add(Nyan[i]); // make Nyan visible.
			// Nyan[i].setControl(NyanInput);
		}
		camera = new Camera(player.getLocationX(), player.getLocationY(),
				player.getLocationZ(), player.getHorAngle(),
				player.getVerAngle());
		guy = new Guy();
		weapon = new Weapon(10);
		phworld = new jbullet();
		input = new UserInput(canvas);
		player.setControl(input);
		weapon.setControl(input);
		guy.setControl(input);
		box = new TestBox(27.5, 2.5, 27.5);
		pause = new PauseMenu();
		phworld.initMaze(maze);
		phworld.initObjects();
		
//		try {
//			teapot = OBJLoader.loadModel(new File("src/assets/Halo_3_SPARTAN.obj"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(teapot);

	}

	/**
	 * Switches the view to orthogonal projection
	 */
	public void Orthoview(GL gl) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
	}

	/**
	 * Switches the view to projectionview
	 */
	public void Projectview(GL gl) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
	}
	
	public void drawPause(GL gl) {
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 20.0);
		gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 - 20.0);
		gl.glVertex2d(screenWidth / 2.0 + 20.0, screenHeight / 2.0);
		gl.glVertex2d(screenWidth / 2.0 - 20.0, screenHeight / 2.0);
		gl.glEnd();

		gl.glColor4d(1.0, 0.0, 0.0, 0.2);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(screenWidth / 100.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0 - 5
				* player.getHealth());
		gl.glVertex2d(screenWidth / 100.0,
				screenHeight - 10.0 - 5 * player.getHealth());

		gl.glEnd();

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	/**
	 * Draws the Hud on screen.
	 */
	public void DrawHud(GL gl) {

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 20.0);
		gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 - 20.0);
		gl.glVertex2d(screenWidth / 2.0 + 20.0, screenHeight / 2.0);
		gl.glVertex2d(screenWidth / 2.0 - 20.0, screenHeight / 2.0);
		gl.glEnd();

		gl.glColor4d(1.0, 0.0, 0.0, 0.2);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(screenWidth / 100.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0 - 5
				* player.getHealth());
		gl.glVertex2d(screenWidth / 100.0,
				screenHeight - 10.0 - 5 * player.getHealth());

		gl.glEnd();

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	/*
	 * **********************************************
	 * * OpenGL event handlers * **********************************************
	 */

	/**
	 * init(GLAutodrawable) is called to initialize the OpenGL context, giving
	 * it the proper parameters for viewing.
	 * <p>
	 * Implemented through GLEventListener. It sets up most of the OpenGL
	 * settings for the viewing, as well as the general lighting.
	 * <p>
	 * It is <b>very important</b> to realize that there should be no drawing at
	 * all in this method.
	 */
	public void init(GLAutoDrawable drawable) {
		drawable.setGL(new DebugGL(drawable.getGL())); // We set the OpenGL
														// pipeline to Debugging
														// mode.
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClearColor(0, 0, 0, 0); // Set the background color.

		// Now we set up our viewpoint.
		gl.glMatrixMode(GL.GL_PROJECTION); // We'll use orthogonal projection.
		gl.glLoadIdentity(); // Reset the current matrix.
		glu.gluPerspective(60, screenWidth, screenHeight, 200); // Set up the
																// parameters
																// for
																// perspective
																// viewing.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// Enable back-face culling.
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_CULL_FACE);

		// Enable Z-buffering.
		gl.glEnable(GL.GL_DEPTH_TEST);

		// Set and enable the lighting.
		float lightPosition[] = { 0.0f, 50.0f, 0.0f, 1.0f }; // High up in the
																// sky!
		float lightColour[] = { 1.0f, 1.0f, 1.0f, 0.0f }; // White light!
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0); // Note
																		// that
																		// we're
																		// setting
																		// Light0.
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glShadeModel(GL.GL_SMOOTH);

	}

	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a
	 * new frame and handles all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. In order to draw everything needed,
	 * it iterates through MazeRunners' list of visibleObjects. For each
	 * visibleObject, this method calls the object's display(GL) function, which
	 * specifies how that object should be drawn. The object is passed a
	 * reference of the GL context, so it knows where to draw.
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		// Calculating time since last frame.
		Calendar now = Calendar.getInstance();
		long currentTime = now.getTimeInMillis();
		int deltaTime = (int) (currentTime - previousTime);
		previousTime = currentTime;

		// Update any movement since last frame.
		
		// Nyan.update(deltaTime);
		
		if(!input.pause) {
			updateMovement(deltaTime);
			updateCamera();
			updatePhysics();
		}
	
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(camera.getLocationX(), camera.getLocationY(),
				camera.getLocationZ(), camera.getVrpX(), camera.getVrpY(),
				camera.getVrpZ(), camera.getVuvX(), camera.getVuvY(),
				camera.getVuvZ());

		// Display all the visible objects of MazeRunner.
		for (Iterator<VisibleObject> it = visibleObjects.iterator(); it
				.hasNext();) {
			it.next().display(gl);
		}

		//for (int i = 0; i < bullets.size(); i++) {
		//	phworld.display(gl, i);
		

		//box.display(gl);
		// Has to be displayed after everything else.
		// guy.display(gl, player);
		//weapon.display(gl);
		//teapot.display(gl);
		Orthoview(gl);
		DrawHud(gl);
		 
		if(input.pause) {
		pause.display(gl);
		}
		Projectview(gl);
		
		gl.glLoadIdentity();
		// Flush the OpenGL buffer.
		gl.glFlush();
	}

	/**
	 * displayChanged(GLAutoDrawable, boolean, boolean) is called upon whenever
	 * the display mode changes.
	 * <p>
	 * Implemented through GLEventListener. Seeing as this does not happen very
	 * often, we leave this unimplemented.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// GL gl = drawable.getGL();
	}

	/**
	 * reshape(GLAutoDrawable, int, int, int, int, int) is called upon whenever
	 * the viewport changes shape, to update the viewport setting accordingly.
	 * <p>
	 * Implemented through GLEventListener. This mainly happens when the window
	 * changes size, thus changin the canvas (and the viewport that OpenGL
	 * associates with it). It adjust the projection matrix to accomodate the
	 * new shape.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		gl.glViewport(0, 0, screenWidth, screenHeight);

		// Set the new projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, screenWidth / screenHeight, .1, 200);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	/*
	 * **********************************************
	 * * Methods * **********************************************
	 */

	/**
	 * updateMovement(int) updates the position of all objects that need moving.
	 * This includes rudimentary collision checking and collision reaction.
	 */
	private void updateMovement(int deltaTime) {
		player.update(deltaTime);
		for (int i = 0; i < amountofNyans; i++) {
			if (Nyan[i].getHP() > -1) {
				Nyan[i].update(deltaTime);
			}
		}

		Bullet bullet = weapon.update(deltaTime, player, camera, phworld);
		if (bullet != null) {
			bullets.add(bullet);
			phworld.CreateBullet((float) camera.getLocationX(), 2.5f,
					(float) camera.getLocationZ(),
					(float) player.getVerAngle(), (float) player.getHorAngle(),
					camera);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update(deltaTime);
			Bullet bb = bullets.get(i);
			// if (maze.isWall(bb.getLocationX(), bb.getLocationZ()));
			// bullets.remove(bullets.get(i)); }
			// phworld.DestroyBullet(i);
			// for (int c = 0; c < enemies.aantal; c++) {
			// if (enemies.isNyan(bb.getLocationX(), bb.getLocationZ())) {
			// enemies.get(i).setHealth(enemies.get(i).getHealth() - 10);
			if (bb.getBulletState() == false) {
				bullets.remove(bullets.get(i));
				phworld.DestroyBullet(i);
			}
		}

		// TODO: don't let Nyan walk through walls. However: walls will
		// disappear thus this might not be
		// necessary.
		/*
		 * 
		 * if (maze.isWall( player.getLocationX() - Math.sin(Math.PI *
		 * player.getHorAngle() / 180) player.getSpeed(), player.getLocationZ()
		 * - Math.cos(Math.PI * player.getHorAngle() / 180) player.getSpeed()))
		 * {
		 * 
		 * player.update(-deltaTime); }
		 * 
		 * if (maze.isWall( player.getLocationX() + Math.sin(Math.PI *
		 * player.getHorAngle() / 180) player.getSpeed() * deltaTime,
		 * player.getLocationZ() + Math.cos(Math.PI * player.getHorAngle() /
		 * 180) player.getSpeed() * deltaTime)) {
		 * 
		 * player.update(-deltaTime); }
		 * 
		 * if (maze.isWall( player.getLocationX() - Math.sin(Math.PI *
		 * player.getHorAngle() / 180) player.getSpeed() * deltaTime,
		 * player.getLocationZ() + Math.cos(Math.PI * player.getHorAngle() /
		 * 180) player.getSpeed() * deltaTime)) { player.update(-deltaTime); }
		 * 
		 * if (maze.isWall( player.getLocationX() + Math.sin(Math.PI *
		 * player.getHorAngle() / 180) player.getSpeed(), player.getLocationZ()
		 * - Math.cos(Math.PI * player.getHorAngle() / 180) player.getSpeed()))
		 * {
		 * 
		 * player.update(-deltaTime); }
		 */
	}

	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * This is done by copying the locations from the Player, since MazeRunner
	 * runs on a first person view.
	 */
	private void updateCamera() {
		camera.setLocationX(player.getLocationX());
		camera.setLocationY(player.getLocationY());
		camera.setLocationZ(player.getLocationZ());
		camera.setHorAngle(player.getHorAngle());
		camera.setVerAngle(player.getVerAngle());
		camera.calculateVRP();
	}

	/**
	 * Updates the physics world simulation.
	 */

	public void updatePhysics() {
		// bullets = phworld.update(bullets);
		box = phworld.update(box);
	}

	/*
	 * ****************************************
	 * * Main * **********************************************
	 */
	/**
	 * Program entry point
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		// Create and run MazeRunner.
//		new MazeRunner();
//	}
}