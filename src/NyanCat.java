import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class NyanCat extends GameObject implements VisibleObject {

	private double horAngle; // sets the direction in which Nyan looks
	// private Control Nyancontrol=null; // the code to tell Nyan what to do
	private double speed = 0.05; // the speed of Nyan
	private int i = 0; // hulp integer for floating up and down
	private int j = 0; // hulp integer for floating up and down
	private int traillength = 30; // amount of rainbow blocks following Nyan
	private double[] lastX = new double[traillength]; // for saving the last
														// locations of Nyan
	private double[] lastZ = new double[traillength];
	private double[] lastY = new double[traillength];
	float NyanColor[] = { (float) Math.random(), (float) Math.random(),
			(float) Math.random(), (float) Math.random() }; // color Nyan,
															// randomized for
															// funz.
	private int HP = 100; // HealthPoints :D
	private boolean goal = false; // has Nyan reached its goal yet?
	public double goalX = 0; // where Nyan wants to go
	public double goalZ = 0;
	boolean dead = false; // whether Nyan is dead or not.
	private Player player;
	private ArrayList<RainbowBlock> rainbows = new ArrayList<RainbowBlock>();
	private Maze maze;
	private TimerTask timertask;
	private Timer timer = new Timer();
	private double HPoff = 0;

	// makes a NyanCat on the location x, y , z, looking in direction h
	public NyanCat(double x, double y, double z, double h, Player play, Maze m) {
		super(x, y, z);
		horAngle = h;
		player = play;
		maze = m;
		// init the last locations:
		for (int i = 0; i < traillength; i++) {
			lastX[i] = getLocationX();
			lastZ[i] = getLocationZ();
			lastY[i] = getLocationY();
		}
	}

	public int getHP() {
		return HP; // healthpoints
	}

	// tells Nyan to what he should listen
	/*
	 * public void setControl(Control control) { this.Nyancontrol = control; }
	 * 
	 * /** Gets the Control object currently controlling the NyanCat
	 * 
	 * @return
	 */
	/*
	 * public Control getControl() { return Nyancontrol; }
	 * 
	 * /** Returns the horizontal angle of the orientation.
	 * 
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle + 90; // plus 90, omdat Nyancat scheef is gebouwd.
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * 
	 * @param horAngle
	 *            the horAngle to set
	 */
	public void setHorAngle(double horangle) {
		this.horAngle = horangle;
	}

	/**
	 * Returns the speed.
	 * 
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 * 
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Updates the physical location and orientation of the NyanCat.
	 * 
	 * @param deltaTime
	 *            The time in milliseconds since the last update.
	 */
	public void update(int deltaTime) {
		// update Nyans movement.
		// this.Nyancontrol.update();// does not do anything yet.
		// Floating animation:
		// Fly up 70 times:

		boolean up = false;
		if ((i >= 0) && (i < 70)) {
			setLocationY(getLocationY() + 0.5 * getSpeed());
			i = i + 1;
			up = true;
		}
		// fly down 70 times:
		if ((i >= 70) && (!up)) {
			setLocationY(getLocationY() - 0.5 * getSpeed());
			j = j + 1;
			if ((j > 69)) {
				i = 0;
				j = 0;
			}
		}
		if (goal) {
			goal(Math.random() * 100, Math.random() * 100);
		}
		if (SeePlayer()) { // checks if player is seen
			goal(player.getLocationX(), player.getLocationZ()); // if player is
																// seen, set
																// goal to
																// players
																// location
		}
		if (!goal) {
			moveTo(goalX, goalZ); // move to goalX,goalZ
		}
		// when goal is reached choose randomly a new goal:
		if ((Math.abs(getLocationX() - goalX) < speed)
				&& (Math.abs(getLocationZ() - goalZ) < speed)) {
			goal = true;
			if ((Math.abs(getLocationX() - goalX) < speed)
					&& (Math.abs(getLocationZ() - goalZ) < speed)) {
				goal = true;
			}
		}

		fireRainbow(10);
		for (int i = 0; i < rainbows.size(); i++) {
			rainbows.get(i).update();
			if (rainbows.get(i).CollisionCheck(player)) {
				HPoff = HPoff + 5;
				rainbows.remove(i);
			}
		}
	}

	private void fireRainbow(int rof) {

		if (SeePlayer()) { // when player is seen, shoot rainbowblocks.
			if (timertask != null)
				return;

			timertask = new TimerTask() {
				@Override
				public void run() {
					RainbowBlock Rainbow = new RainbowBlock(
							NyanCat.this.getLocationX(),
							NyanCat.this.getLocationY(),
							NyanCat.this.getLocationZ(), goalX,
							NyanCat.this.getLocationY(), goalZ, maze);
					rainbows.add(Rainbow);
				}
			};

			timer.scheduleAtFixedRate(timertask, 0,
					1000 + Math.round((Math.random() * 100)));
		}
	}

	public void goal(double X, double Y) {
		goalX = X;
		goalZ = Y;
		goal = false;
	}

	public boolean SeePlayer() {
		// Checks whether the Nyan can see the player.
		// It does this by checking whether the angle the line from player to
		// Nyan makes with the Z-axis
		// and whether this angle is between Nyans looking angle +-70 degrees.
		double hoekPlayer = Math.toDegrees(Math.atan2(player.getLocationX()
				- this.getLocationX(),
				player.getLocationZ() - this.getLocationZ()));
		if (hoekPlayer < 0) {
			hoekPlayer = hoekPlayer + 360; // for better comparison
		}
		double HorAngle = this.getHorAngle();
		if (this.getHorAngle() < 0) {
			HorAngle = HorAngle + 360; // for better comparison
		}
		double deltaX = (player.getLocationX() - this.getLocationX())
				/ Math.sqrt(Math.pow(player.getLocationX() - getLocationX(), 2)
						+ Math.pow(player.getLocationZ() - getLocationZ(), 2));
		double deltaZ = (player.getLocationZ() - getLocationZ())
				/ Math.sqrt(Math.pow(player.getLocationX() - getLocationX(), 2)
						+ Math.pow(player.getLocationZ() - getLocationZ(), 2));

		if (HorAngle - 70 <= hoekPlayer) {
			if (HorAngle + 70 >= hoekPlayer) {
				double x = player.getLocationX() - this.getLocationX();
				double z = player.getLocationZ() - this.getLocationZ();
//				double length = Math.sqrt(x*x + z*z);
//				double hoev = Math.floor(length / 2.5);
//				double hoek = Math.tan(x / z);
//			for (int i = 0; i < hoev; i++) {	
//				if (jbullet.isNewWall(this.getLocationX() + Math.sqrt(12.5) * Math.asin(hoek) ,
//						this.getLocationZ() + Math.sqrt(12.5)*Math.acos(hoek))) {
				if (Math.abs(x) > Math.abs(z)) {
					for (double i = 0; i < x; i = i + 10) {
						if (jbullet.isNewWall(this.getLocationX() + i,
								this.getLocationZ() + (z / x) * i )) {
							return false;
						}
					}
				}
				if (Math.abs(z) > Math.abs(x)) {
					for (double i = 0; i < z; i = i + 10) {
						if (jbullet.isNewWall(this.getLocationX()  + (x / z) * i,
								this.getLocationZ() - 2.5 + i )) {
							return false;
						}
					}
				}
				
//				for (double i = 0; i < Math.abs(player.getLocationX()
//						- this.getLocationX()); i = i + maze.SQUARE_SIZE / 2) {
//					for (double j = 0; j < Math.abs(player.getLocationZ()
//							- this.getLocationZ()); j = j + maze.SQUARE_SIZE
//							/ 2) {
//						if (jbullet.isNewWall(this.getLocationX() + i * deltaX,
//								this.getLocationZ() + j * deltaZ)) {
//							return false;
//						}
					
		
				return true;
			}
		}
		return false;
	}

	public void moveTo(double X, double Z) {

		// genormaliseerde richtingsvector:
		double deltaX = (X - getLocationX())
				/ Math.sqrt(Math.pow(X - getLocationX(), 2)
						+ Math.pow(Z - getLocationZ(), 2));
		double deltaZ = (Z - getLocationZ())
				/ Math.sqrt(Math.pow(X - getLocationX(), 2)
						+ Math.pow(Z - getLocationZ(), 2));

		if (jbullet.isNewWall(this.getLocationX() + deltaX * speed,
				this.getLocationZ() + deltaZ * speed)) {
			if (!jbullet.isNewWall(this.getLocationX() - deltaX * speed,
					this.getLocationZ() + deltaZ * speed)) {
				moveTo(this.getLocationX() - deltaX * speed,
						this.getLocationZ() + deltaZ * speed);
			} else if (!jbullet.isNewWall(this.getLocationX() + deltaX * speed,
					this.getLocationZ() - deltaZ * speed)) {
				moveTo(this.getLocationX() + deltaX * speed,
						this.getLocationZ() - deltaZ * speed);

			} else if (!jbullet.isNewWall(this.getLocationX() - deltaX * speed,
					this.getLocationZ() - deltaZ * speed)) {
				moveTo(this.getLocationX() - deltaX * speed,
						this.getLocationZ() - deltaZ * speed);
			}
		}

		if (!goal) {
			this.setLocationX(getLocationX() + deltaX * speed);
			this.setLocationZ(getLocationZ() + deltaZ * speed);
			double hoek = Math
					.atan(Math.abs(((this.getLocationX() - this.lastX[1]) / (this
							.getLocationZ() - this.lastZ[1]))));
			hoek = (hoek / Math.PI) * 180; // van rad naar degrees.

			// 4 kwadranten afzonderlijk bekijken vanwege oscillaties rond 0, en
			// range atan:
			if ((-this.lastX[1] + this.getLocationX() >= 0)
					&& (-this.lastZ[1] + this.getLocationZ() >= 0)) {
				// hoek=hoek;
			}
			if ((-this.lastX[1] + this.getLocationX() < 0)
					&& (-this.lastZ[1] + this.getLocationZ() >= 0)) {
				hoek = -hoek;
			}
			if ((-this.lastX[1] + this.getLocationX() >= 0)
					&& (-this.lastZ[1] + this.getLocationZ() < 0)) {
				hoek = 180 - hoek;
			}
			if ((-this.lastX[1] + this.getLocationX() < 0)
					&& (-this.lastZ[1] + this.getLocationZ() < 0)) {
				hoek = 180 + hoek;
			}
			hoek = hoek - 90; // offset vanwege rare assendefinities.
			this.setHorAngle(hoek);

		}
	}

	@Override
	public void display(GL gl) {
		// When Nyan is not dead, display him:
		if (!dead) {
			GLUT glut = new GLUT();
			// Coords for NyanLocation:
			double X = this.getLocationX();
			double Y = this.getLocationY() + 5 / 4; // Nyan is build around this
													// point, it is the center
			double Z = this.getLocationZ();
			gl.glPushMatrix(); // slaat het coordinatenstelsel van nu op.
			// display the Nyan (its poptart) on its new location:
			gl.glTranslated(X, Y, Z); // X,Y,Z are the coordinates of the
										// middlepoint of the poptart of Nyan
			gl.glRotated(this.horAngle, 0, 1, 0); // rotate around Y-axis for
													// the right orientation,
			// System.out.println(horAngle);
			// make Nyan look to the right direction.

			// the appearance of Nyan:

			int roundness = 10; // how round are the spheres and cones? Less
								// round-> faster rendering.
			float size = 1; // measure for the size of Nyan
			float scalefactor = 2; // measure for scaling of the poptart

			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, NyanColor, 0); // Nyan
																		// material.

			// POPTART:
			gl.glScaled(scalefactor, scalefactor, 1); // making the poptart cube
														// into a cuboid,
			// make a textured poptart of size size:
			Textureloader.poptart(gl, size); // texturized cube

			// HEAD:
			gl.glScaled((float) 1 / (scalefactor), (float) 1 / scalefactor, 1); // unscale
																				// the
																				// head
			gl.glScaled(1, 1, (float) 1.5); // scale the head
			// location of head with respect to the poptart:
			gl.glTranslated((0.7 * scalefactor), -1 / scalefactor * size, 0);
			Textureloader.head(gl, glut, size, roundness);

			// EARS:
			gl.glScaled(1, 1, (float) 1 / 1.5); // unscale ears.
			gl.glTranslated(0, 0.25 * size, 0.25 * size); // location of ear 1
															// with respect to
															// head
			gl.glRotated(-90, 1, 0, 0);// point upwards.
			glut.glutSolidCone(0.25 * size, 0.5 * size, roundness, roundness); // ear
																				// 1

			gl.glRotated(90, 1, 0, 0); // rotate back
			gl.glTranslated(0, 0, -0.5 * size); // location of ear 2 with
												// respect to ear 1
			gl.glRotated(-90, 1, 0, 0); // point upwards.
			glut.glutSolidCone(0.25 * size, 0.5 * size, roundness, roundness); // ear
																				// 2
			gl.glRotated(90, 1, 0, 0); // rotate back

			// PAWS:
			// translate back to middle of poptart:
			gl.glTranslated(0, 0, 0.5 * size);
			gl.glTranslated(0, -0.25 * size, -0.25 * size);
			gl.glTranslated(-(0.7 * scalefactor), 1 / scalefactor * size, 0);
			// making of the paws:
			// pawlocation with respect to middle of poptart:
			gl.glTranslated(0.4 * scalefactor, -0.5 * scalefactor, 0.2 * size);
			glut.glutSolidCube((float) (0.3 * size)); // paw 1
			// paw location with respect to paw 1:
			gl.glTranslated(-0.8 * scalefactor, 0, 0);
			glut.glutSolidCube((float) (0.3 * size)); // paw 2
			// Paw location with respect to paw 2:
			gl.glTranslated(0, 0, -0.5 * size);
			glut.glutSolidCube((float) (0.3 * size)); // paw 3
			// Paw location with respect to paw 3:
			gl.glTranslated(0.8 * scalefactor, 0, 0);
			glut.glutSolidCube((float) (0.3 * size)); // paw 4

			// TAIL:
			// Tail movement:
			double ytail = 0; // the translation in up direction of the next
								// tail segment with respect to the last.
			boolean up = false;
			if ((i >= 0) && (i < 35)) {
				ytail = i * (0.3 * size / 70);
				up = true;
			}
			if ((i >= 35) && (i < 70)) {
				ytail = (70 - i) * (0.3 * size / 70);
			}
			if ((i >= 70) && (!up)) {
				if ((j >= 0) && (j < 35)) {
					ytail = -j * (0.3 * size) / 70;
				}
				if ((j >= 35) && (j < 70)) {
					ytail = (j - 70) * (0.3 * size / 70);
				}
			}
			gl.glTranslated(-0.9 * size * scalefactor, 0.7 * size, 0.3 * size); // translation
																				// with
																				// respect
																				// to
																				// paw
																				// 4.
			glut.glutSolidCube((float) (0.3 * size)); // tail part 1.
			gl.glTranslated(-0.3 * size, ytail, 0); // translation with respect
													// to tail part 1
			glut.glutSolidCube((float) (0.3 * size));// tail part 2.
			gl.glTranslated(-0.3 * size, ytail, 0); // translation with respect
													// to tail part 2.
			glut.glutSolidCube((float) (0.3 * size)); // tail part 3.

			// TODO: if Nyan x and/or z location changes make paws and head
			// move. No Priority.

			// Nyans rainbow trail:

			gl.glPopMatrix();// reset alle coordinaten tot waar
								// gl.glPushMatrix() werd aangeroepen.
			// make rainbowtrail:
			for (int i = 0; i < traillength; i++) { // of length traillength
				gl.glPushMatrix(); // save all coords
				gl.glTranslated(0, 3 / 4 + 0.5 * size, 0); // translate a bit up
															// so that the
															// rainbowtrail
															// origins from the
															// middle
				gl.glTranslated((lastX[i]), lastY[i], (lastZ[i])); // translate
																	// to saved
																	// last
																	// coords
				Textureloader.Rainbow(gl, 0.5 * size); // make the actual
														// rainbowblock
				gl.glPopMatrix(); // reset all coords
			}

			// shift all coords in the last positions one position so that first
			// position in array becomes
			// free and last position is deleted
			// only once in 3 times for saving memory
			if ((i % 3 == 0) || ((j % 3 == 0) && (i > 69))) {
				for (int k = traillength - 1; k > 0; k = k - 1) {
					lastX[k] = lastX[k - 1];
					lastY[k] = lastY[k - 1];
					lastZ[k] = lastZ[k - 1];
				}
				// put the new location in the arrays.
				lastX[0] = getLocationX();
				lastY[0] = getLocationY();
				lastZ[0] = getLocationZ();
			}
			// gl.glPopMatrix();
			if (this.getHP() < 0) {
				dead = true;
			}
			// make projectiles visible:
			for (int i = 0; i < rainbows.size(); i++) {
				rainbows.get(i).display(gl);

			}
		}

	}

	public double getHPoff() {
		double res = HPoff;
		HPoff = 0;
		return res;
	}

	public void setHP(int x) {
		HP = x;
	}

}
