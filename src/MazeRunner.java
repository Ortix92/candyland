import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import leveleditor.editor;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * MazeRunner is the base class of the game, functioning as the view controller
 * and game logic manager. =======
 * 
 * /** MazeRunner is the base class of the game, functioning as the view
 * controller and game logic manager. >>>>>>> origin/physicscollision
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
	private Animator anim;

	public static int screenWidth = 1024;

	public static int screenHeight = 768;
	private ArrayList<VisibleObject> visibleObjects;
	private Player player;
	public static int amountofNyans = editor.amountOfNyans;
	private ArrayList<NyanCat> Nyan = new ArrayList<NyanCat>();
	private Camera camera;
	private UserInput input;
	public static Maze maze;
	private long previousTime = Calendar.getInstance().getTimeInMillis();
	private Weapon weapon;
	private PauseMenu pause;
	private jbullet phworld;
	private int score = 0;
	private SkyBox skybox;
	private ArrayList<PickUp> pickup = new ArrayList<PickUp>();
	private Floor floor;
	private Flag flag;
	private boolean NyanGeluid = false;
	private int Timer = 1000;
	private double Time0 = Calendar.getInstance().getTimeInMillis();
	private double Time3;

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

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static int getScreenWidth() {
		return screenWidth;
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
		anim = new Animator(canvas);
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
	 * <p>
	 * F Remember that every object that should be visible on the screen, should
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
		// visibleObjects.add(maze);
		floor = new Floor(maze);

		// visibleObjects.add(floor);
		phworld = new jbullet(amountofNyans);

		// visibleObjects.add(skybox);

		player = new Player(maze.PLAYER_SPAWN_X, 3, maze.PLAYER_SPAWN_Z, -120,
				-90);

		skybox = new SkyBox(player, 50);
		// visibleObjects.add(skybox);
		System.out.println("Maze size: " + maze.MAZE_SIZE);
		// initialize the NyanCat.
		for (int i = 0; i < amountofNyans; i++) {
			double X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
					+ maze.SQUARE_SIZE / 2; // x-position
			double Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
					+ maze.SQUARE_SIZE / 2; // z-position
			while (maze.isWall(X, Z)) {
				X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
						+ maze.SQUARE_SIZE / 2; // x-position
				Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE + 3
						* maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2; // z-position
			}

			System.out.println("X " + X + " Z " + Z);
			Nyan.add(new NyanCat(X, maze.SQUARE_SIZE / 4, // y-position
					Z, Math.random() * 360,
					// horizontal angle
					player));

			// visibleObjects.add(Nyan.get(i)); // make Nyan visible.

			NyanCat nyan = new NyanCat(X, maze.SQUARE_SIZE / 4, // y-position
					Z, Math.random() * 360,
					// horizontal angle
					player);
			phworld.initNyan(nyan);
		}
		double X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
				; // x-position
		double Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
				; // z-position
		while (maze.isWall(X, Z)) {
			X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
					; // x-position
			Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
					; // z-position
		}
		PickUp NewClaws = new PickUp(X, Z, player, 2);
		visibleObjects.add(NewClaws);
		pickup.add(NewClaws);
		X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
				; // x-position
		Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
				; // z-position
		while (maze.isWall(X, Z)) {
			X = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE
				; // x-position
			Z = Math.random() * maze.MAZE_SIZE * maze.SQUARE_SIZE 
					; // z-position
		}
		PickUp NewClaws2 = new PickUp(X, Z, player, 3);
		visibleObjects.add(NewClaws2);
		pickup.add(NewClaws2);

		camera = new Camera(player.getLocationX(), player.getLocationY(),
				player.getLocationZ(), player.getHorAngle(),
				player.getVerAngle());
		weapon = new Weapon(10);
		input = new UserInput(canvas);
		pause = new PauseMenu();
		player.setControl(input);
		weapon.setControl(input);
		phworld.initMaze(maze);
		phworld.initObjects();
		phworld.initPlayer((float) player.getLocationX(),
				(float) player.getLocationY(), (float) player.getLocationZ());
		System.out.println("Maze size: " + maze.MAZE_SIZE);
		Time3 = Calendar.getInstance().getTimeInMillis();
		Menu.Sound.play("sounds/nyan-cat-wav.wav");
	}

	/**
	 * Switches the view to orthogonal projection
	 */
	public void Orthoview(GL gl) {
		gl.glMatrixMode(GL.GL_PROJECTION);
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

		GLUT glut = new GLUT();
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_LIGHTING);
		String weaponString = "";

		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
		if (Weapon.getNewWeapon() == 0) {
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 20.0);
			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 + 20.0, screenHeight / 2.0);
			gl.glVertex2d(screenWidth / 2.0 - 20.0, screenHeight / 2.0);
			gl.glEnd();
			weaponString = "Default weapon.";
		}
		if (Weapon.getNewWeapon() == 2) {
			weaponString = "Sniper. Use right mouse button to zoom.";
			if (!UserInput.zoom) {
				gl.glBegin(GL.GL_LINES);
				gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 5.0);
				gl.glVertex2d(screenWidth / 2.0 + 5.0, screenHeight / 2.0 - 5.0);
				gl.glVertex2d(screenWidth / 2.0 + 5.0, screenHeight / 2.0 - 5.0);
				gl.glVertex2d(screenWidth / 2.0 - 5.0, screenHeight / 2.0 - 5.0);
				gl.glVertex2d(screenWidth / 2.0 - 5.0, screenHeight / 2.0 - 5.0);
				gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 5.0);
				gl.glEnd();

			} else {
				// ZOOM! Draw scope
				// draw Circle:
				double radius = 40;
				double segments = 100;
				double theta = (2 * Math.PI) / segments;
				double x = radius;
				double y = 0;
				double tantheta = Math.tan(theta);
				double costheta = Math.cos(theta);

				gl.glBegin(GL.GL_LINES);

				for (int i = 0; i < segments; i++) {
					gl.glVertex2d(screenWidth / 2 + x, screenHeight / 2 + y);
					double tx = -y;
					double ty = x;

					// add the tangential vector

					x += tx * tantheta;
					y += ty * tantheta;

					// correct using the radial factor

					x *= costheta;
					y *= costheta;
					gl.glVertex2d(screenWidth / 2 + x, screenHeight / 2 + y);
				}
				// draw lines on the circle:
				gl.glVertex2d(screenWidth / 2 + 50, screenHeight / 2);
				gl.glVertex2d(screenWidth / 2 + 30, screenHeight / 2);
				gl.glVertex2d(screenWidth / 2 - 50, screenHeight / 2);
				gl.glVertex2d(screenWidth / 2 - 30, screenHeight / 2);
				gl.glVertex2d(screenWidth / 2, screenHeight / 2 + 50);
				gl.glVertex2d(screenWidth / 2, screenHeight / 2 + 30);
				gl.glVertex2d(screenWidth / 2, screenHeight / 2 - 50);
				gl.glVertex2d(screenWidth / 2, screenHeight / 2 - 30);

				gl.glEnd();

			}

		}
		if (Weapon.getNewWeapon() == 3) {
			weaponString = "Wallmoving Weapon.";
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 20.0);
			gl.glVertex2d(screenWidth / 2.0 + 20.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 + 20.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 - 20.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 - 20.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 + 20.0);

			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 + 10.0, screenHeight / 2.0);
			gl.glVertex2d(screenWidth / 2.0, screenHeight / 2.0 - 20.0);
			gl.glVertex2d(screenWidth / 2.0 - 10.0, screenHeight / 2.0);
			gl.glVertex2d(screenWidth / 2.0 - 10.0, screenHeight / 2.0);
			gl.glVertex2d(screenWidth / 2.0 + 10.0, screenHeight / 2.0);

			gl.glEnd();
		}

		// health bar
		gl.glColor4d(1.0, 0.0, 0.0, 0.2);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(screenWidth / 100.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 30.0, screenHeight - 10.0 - 5
				* player.getHealth());
		gl.glVertex2d(screenWidth / 100.0,
				screenHeight - 10.0 - 5 * player.getHealth());
		gl.glEnd();

		// Energy Bar
		gl.glColor4d(0, 0, 1, 1);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(screenWidth / 100.0 + 35, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 65.0, screenHeight - 10.0);
		gl.glVertex2d(screenWidth / 100.0 + 65.0, screenHeight - 10.0 - 0.5
				* jbullet.energy);
		gl.glVertex2d(screenWidth / 100.0 + 35, screenHeight - 10.0 - 0.5
				* jbullet.energy);
		gl.glEnd();

		// gl.glColor3f(1.0f, 1.0f, 1.0f);
		// gl.glRasterPos2f(screenWidth / 2.3f, screenHeight - 0.9f *
		// screenHeight);
		// int len, i;
		

		// Timer
		double Time1 = Calendar.getInstance().getTimeInMillis();
		if (Timer > 0) {
			if (Time1 - Time0 >= 1000) {
				Timer = Timer - 1;
				Time0 = Time1;
			}
		}
		String timerString = "";
		if (Nyan.size() != 0) {
			timerString = "Timebonus: " + Timer;
		} else if (!flag.Flag) {
			timerString = "Timebonus: " + Timer;
		}
		
		Font f = new Font("SansSerif", Font.BOLD, 24);
		TextRenderer tr = new TextRenderer(f);
		tr.setColor(50, 30, 0, 1.0f);

		tr.beginRendering((int) screenWidth, (int) screenHeight);
		tr.draw("Score:  " + score, (int) (screenWidth / 2.3f),
				(int) (0.85f * screenHeight));
		tr.draw(timerString, (int) (screenWidth / 2.3f),
				(int) (0.9f * screenHeight));
		tr.draw("Amount of Nyans left: "+Nyan.size(), (int) (screenWidth / 2.3f),
				(int) (0.8f * screenHeight));
		tr.endRendering();
		
		/*gl.glRasterPos2f(screenWidth / 2.3f, screenHeight - 0.95f
				* screenHeight);
		int tim = (int) timerString.length();
		for (int k = 0; k < tim; k++) {
			glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24,
					timerString.charAt(k));*/

		if (Nyan.size() == 0) {
			Font h = new Font("Castellar", Font.PLAIN, 30);
			TextRenderer fj = new TextRenderer(h);
			fj.beginRendering((int) screenWidth, (int) screenHeight);
			// gl.glRasterPos2f(screenWidth / 3, screenHeight - 0.7f
			// * screenHeight);
			// String vlagstring = "";
			if (!Flag.Flag) {
				// vlagstring = "Loop naar de vlag om te winnen.";
				fj.draw("Loop naar de vlag om te winnen.",
						(int) (screenWidth / 4), (int) (0.7f * screenHeight));
			} else {
				fj.draw("CONGRATULATUALIAONS!!!!!", (int) (screenWidth / 3),
						(int) (0.7f * screenHeight));
			}
			fj.endRendering();
			// int j = (int) vlagstring.length();
			// for (int k = 0; k < j; k++) {
			// glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24,
			// vlagstring.charAt(k));
			// }
		}
		Font f2 = new Font("Narkisim", Font.PLAIN, 28);
		TextRenderer tr2 = new TextRenderer(f2);
		tr2.beginRendering(screenWidth, screenHeight);
		tr2.draw(weaponString, 50, (int) (0.95 * screenHeight));
		// int l = (int) weaponString.length();
		// for (int k = 0; k < l; k++) {
		// glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24,
		// weaponString.charAt(k));
		// }
		tr2.endRendering();

		if (LevelSelector.chucknorris) {
			TextRenderer tr3 = new TextRenderer(f);
			tr3.beginRendering(screenWidth, screenHeight);
			tr3.setColor(20, 0, 0, 1);
			tr3.endRendering();
		}
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
		float lightPosition[] = { (float) (maze.MAZE_SIZE / 2f), 50.0f,
				(float) (maze.MAZE_SIZE / 2f), 1.0f }; // High up in the
		// sky!
		float lightColour[] = { 1.0f, 1.0f, 1.0f, 1.0f }; // White
															// light!
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
		try {
			GL gl = drawable.getGL();
			GLU glu = new GLU();
			// Calculating time since last frame.
			Calendar now = Calendar.getInstance();
			long currentTime = now.getTimeInMillis();
			int deltaTime = (int) (currentTime - previousTime);
			long fps = (currentTime - previousTime);
			// System.out.println("FPS:" + 1000 / fps);
			previousTime = currentTime;

			// Update any movement since last frame.

			// Nyan.update(deltaTime);

			if (!UserInput.pause) {
				updateMovement(deltaTime);
				updateCamera();
				updatePhysics(deltaTime);
			}

			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();

			if (UserInput.zoom) {
				System.out.println("Zoom");
				glu.gluPerspective(30, screenWidth / screenHeight, 0.1, 200);
			} else {
				glu.gluPerspective(60, screenWidth / screenHeight, 0.1, 200);
			}
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();
			glu.gluLookAt(camera.getLocationX(), camera.getLocationY(),
					camera.getLocationZ(), camera.getVrpX(), camera.getVrpY(),
					camera.getVrpZ(), camera.getVuvX(), camera.getVuvY(),
					camera.getVuvZ());

			// Display all the visible objects of MazeRunner.
			skybox.display(gl);
			phworld.displaymaze(gl);
			// for (int i = 0; i < bullets.size(); i++) {
			phworld.display(gl);
			// }
			for (int j = 0; j < Nyan.size(); j++) {
				Nyan.get(j).display(gl);
			}
			// floor.display(gl);
			for (Iterator<VisibleObject> it = visibleObjects.iterator(); it
					.hasNext();) {
				it.next().display(gl);
			}
			floor.display(gl);

			weapon.display(gl);
			Orthoview(gl);
			DrawHud(gl);

			if (UserInput.pause) {
				pause.display(gl);
			}
			Projectview(gl);

			gl.glLoadIdentity();
			// Flush the OpenGL buffer.
			gl.glFlush();
		} catch (GLException e) {
			System.out.println("Exception: " + e.getMessage());
		}
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
		drawable.getGL();
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

	public boolean NyanSeePlayer() {
		for (int i = 0; i < Nyan.size(); i++) {
			if (Nyan.get(i).SeePlayer()) {
				if (!NyanGeluid) {
					Menu.Sound.play("sounds/nyan.wav");
				}
				NyanGeluid = true;
				return true;
			}
		}
		NyanGeluid = false;
		return false;
	}

	/**
	 * updateMovement(int) updates the position of all objects that need moving.
	 * This includes rudimentary collision checking and collision reaction.
	 */
	/**
	 * updateMovement(int) updates the position of all objects that need moving.
	 * This includes rudimentary collision checking and collision reaction.
	 */
	private void updateMovement(int deltaTime) {
		for (int j = 0; j < Nyan.size(); j++) {
			player.setHealth(player.getHealth() - Nyan.get(j).getHPoff());
		}
		if (player.getHealth() <= 0) {
			ScoreScreen.score = score;
			ScoreScreen.gameover = 1;
			Game.gsm.setGameState(GameStateManager.DEAD_STATE);
		}
		player.update(deltaTime);
		for (int i = 0; i < pickup.size(); i++) {
			if (!pickup.get(i).getPickup()) {
				pickup.get(i).update(deltaTime);
				if (pickup.get(i).getPickup()) {
					int w = Weapon.getNewWeapon();
					switch (pickup.get(i).getSoort()) {
					case 0:
						Weapon.setNewWeapon(0);
						break;
					case 1:
						double rand = Math.random();
						if (rand <= 0.5) {
							score = score + 500;
						}
						if (rand >= 0.3) {
							player.setHealth(player.getHealth() + 20);
							if (player.getHealth() > 100) {
								player.setHealth(100);
							}
						}
						// System.out.println(rand);
						break;
					case 2:
						Weapon.setNewWeapon(2);
						break;
					case 3:
						Weapon.setNewWeapon(3);
						break;
					}
					// place your old weapon in new pickup:
					if (pickup.get(i).getSoort() != 1) {
						PickUp wapen = new PickUp(
								player.getLocationX()
										+ Math.sin(Math.toRadians(player
												.getHorAngle()))
										* player.getSpeed() * 20,
								player.getLocationZ()
										+ Math.cos(Math.toRadians(player
												.getHorAngle()))
										* player.getSpeed() * 20, player, w);
						visibleObjects.add(wapen);
						pickup.add(wapen);
					}
				}
			}
		}
		for (int j = 0; j < Nyan.size(); j++) {
			Nyan.get(j).update(deltaTime);
			if (phworld.updateNyanhealth(j)) {
				PickUp pickupnieuw = new PickUp(Nyan.get(j).getLocationX(),
						Nyan.get(j).getLocationZ(), player, 1);
				visibleObjects.add(pickupnieuw);
				pickup.add(pickupnieuw);
				Nyan.remove(j);
				Menu.Sound.play("sounds/sonic_ring.wav");
				score = score + 1000;
				if (Nyan.size() == 0) {
					flag = new Flag(maze.PLAYER_SPAWN_X, 0,
							maze.PLAYER_SPAWN_Z, player);
					visibleObjects.add(flag);
				}
			}
		}
		if (Nyan.size() != 0) {
			phworld.update(player);
			player = phworld.updatePlayer(player);

		} else {
			flag.update();
			if (!flag.Flag) {
				phworld.update(player);
				player = phworld.updatePlayer(player);
			} else {
				if (flag.geti() == 0.01) {
					score = score + Timer;
				}
				if (flag.geti() >= 4) {
					ScoreScreen.score = score;
					ScoreScreen.gameover = 2;
					Game.gsm.setGameState(Game.gsm.DEAD_STATE);
				}
			}

			if (NyanSeePlayer()) {
				for (int i = 0; i < Nyan.size(); i++) {
					Nyan.get(i).goalX = player.getLocationX();
					Nyan.get(i).goalZ = player.getLocationZ();
				}

			}
		}
		if (weapon.update(deltaTime, player, camera, phworld)) {
			phworld.CreateBullet((float) camera.getLocationX(),
					(float) camera.getVrpY(), (float) camera.getLocationZ(),
					(float) player.getVerAngle(), (float) player.getHorAngle(),
					camera);
			// bullets.add(new Bullet(player.getLocationX(),
			// player.getLocationY(), player.getLocationZ(), player
			// .getHorAngle(), player.getVerAngle()));

			// }
			// for (int i = 0; i < bullets.size(); i++) {
			// bullets.get(i).update(deltaTime);
		}
		double Time2 = Calendar.getInstance().getTimeInMillis();
		if (Time2 - Time3 > 4000) {
			Time3 = Time2;
		}
		if (Time2 - Time3 == 0) {
			Menu.Sound.play("sounds/nyan-cat-wav.wav");
		}
	}

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

	public void updatePhysics(int deltaTime) {

		phworld.update(deltaTime, Nyan, player);

		// bullets = phworld.getbullets();
	}

}