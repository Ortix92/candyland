import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.sun.opengl.util.GLUT;

public class jbullet {

	private ArrayList<BulletTimer> Bullets;
	private ArrayList<NyanCat> nyans;

	private ArrayList<Float> oldx = new ArrayList<Float>();
	private ArrayList<Float> oldz = new ArrayList<Float>();

	public DiscreteDynamicsWorld dynamicworld;
	public int maxSubSteps;
	public float timeStep, fixedTimeStep;
	private CollisionDispatcher dispatcher;
	private ConstraintSolver solver;
	private DefaultCollisionConfiguration collisionConfiguration;
	private BroadphaseInterface broadphase;

	private int amountofNyans;
	private ObjectArrayList<RigidBody> bullets;
	private static ObjectArrayList<RigidBody> mazeblocks = new ObjectArrayList<RigidBody>();
	private RigidBody groundbody;
	private RigidBody playar;
	public static int energy = 1000;

	public jbullet(int n) {
		Bullets = new ArrayList<BulletTimer>();
		bullets = new ObjectArrayList<RigidBody>();
		nyans = new ArrayList<NyanCat>();

		broadphase = new DbvtBroadphase();

		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);

		solver = new SequentialImpulseConstraintSolver();

		dynamicworld = new DiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);
		dynamicworld.setGravity(new Vector3f(0f, -10f, 0f));

	}

	/*
	 * Sets up the standard world
	 */

	public void initObjects() {
		CollisionShape groundshape = new StaticPlaneShape(
				new Vector3f(0, 1, 0), 0);
		Transform t = new Transform();
		t.setRotation(new Quat4f(0, 0, 0, 1));
		t.transform(new Vector3f(0, -1, 0));
		DefaultMotionState groundMotionState = new DefaultMotionState(t);
		RigidBodyConstructionInfo groundRigidBody = new RigidBodyConstructionInfo(
				0, groundMotionState, groundshape, new Vector3f(0, 0, 0));
		groundbody = new RigidBody(groundRigidBody);

		dynamicworld.addRigidBody(groundbody);

	}

	public void initMaze(Maze maze) {
		for (int i = 0; i < maze.MAZE_SIZE; i++) {
			for (int j = 0; j < maze.MAZE_SIZE; j++) {
				if (maze.isWall(i, j)) {
					CollisionShape mazeshape = new BoxShape(new Vector3f(2.5f,
							2.5f, 2.5f));
					Transform t = new Transform();
					t.origin.set(
							(float) (i * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2f),
							(float) (maze.SQUARE_SIZE / 2f), (float) (j
									* maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2f));
					DefaultMotionState mazeMotionState = new DefaultMotionState(
							t);
					Vector3f Inertia = new Vector3f(0, 0, 0);
					if (i == maze.MAZE_SIZE - 1 || j == maze.MAZE_SIZE - 1
							|| i == 0 || j == 0) {
						RigidBodyConstructionInfo mazeinfo = new RigidBodyConstructionInfo(
								Float.POSITIVE_INFINITY, mazeMotionState,
								mazeshape, Inertia);
						RigidBody mazebody = new RigidBody(mazeinfo);
						mazebody.setFriction(1);
						dynamicworld.addRigidBody(mazebody);
						mazeblocks.add(mazebody);
					} else {
						Inertia = new Vector3f(0, 0, 0);
						RigidBodyConstructionInfo mazeinfo = new RigidBodyConstructionInfo(
								100000, mazeMotionState, mazeshape, Inertia);
						RigidBody mazebody = new RigidBody(mazeinfo);
						mazebody.setFriction(1);
						dynamicworld.addRigidBody(mazebody);
						mazeblocks.add(mazebody);
					}
				}
			}
		}
		for (int i = 0; i < mazeblocks.size(); i++) {
			Transform trans = new Transform();
			mazeblocks.get(i).getWorldTransform(trans);
			System.out.println(trans.origin);
			oldx.add(trans.origin.x);
			oldz.add(trans.origin.z);
		}

	}

	public void initNyan(NyanCat nyancat) {
		nyans.add(nyancat);
	}

	public void CreateBullet(float x, float y, float z, float verAngle,
			float horAngle, Camera camera) {
		System.out.println("Shoot");
		CollisionShape bulletshape = new SphereShape(0.2f);
		Transform p = new Transform();
		p.setRotation(new Quat4f(horAngle, verAngle, 0, 1));
		p.origin.set(x - (float) Math.sin(Math.toRadians(horAngle)), y, z
				- (float) Math.cos(Math.toRadians(horAngle)));
		DefaultMotionState bulletmotion = new DefaultMotionState();
		bulletmotion.setWorldTransform(p);
		float mass = 0.002f;
		if (Weapon.getNewWeapon() == 3) {
			mass = 3000000 * mass;
		}
		Vector3f Inertia = new Vector3f(0, 0, 0);
		bulletshape.calculateLocalInertia(mass, Inertia);
		RigidBodyConstructionInfo boxRigidBodyInfo = new RigidBodyConstructionInfo(
				mass, bulletmotion, bulletshape, Inertia);
		RigidBody bullet = new RigidBody(boxRigidBodyInfo);

		int velocityScalar = 200;
		if (Weapon.getNewWeapon() == 2) {
			velocityScalar = velocityScalar * 2;
		}
		Vector3f velocityVector = new Vector3f(
				velocityScalar
						* (-(float) Math.sin(Math.toRadians(horAngle)) * (float) Math.cos(Math
								.toRadians(verAngle))),
				velocityScalar * (float) Math.sin(Math.toRadians(verAngle)),
				velocityScalar
						* (-(float) Math.cos(Math.toRadians(horAngle)) * (float) Math
								.cos(Math.toRadians(verAngle))));

		bullet.setLinearVelocity(velocityVector);
		bullet.setFriction(20);
		dynamicworld.addRigidBody(bullet);
		bullets.add(bullet);
		BulletTimer b = new BulletTimer();
		Bullets.add(b);
	}

	public void initPlayer(float x, float y, float z) {
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(x, y, z);

		DefaultMotionState playerstate = new DefaultMotionState();
		CollisionShape playershape = new BoxShape(new Vector3f(1f, 2.5f, 1f));
		playerstate.setWorldTransform(startTransform);

		Vector3f Inertia = new Vector3f(0, 0, 0);
		RigidBodyConstructionInfo playerRigidBody = new RigidBodyConstructionInfo(
				20, playerstate, playershape, Inertia);
		playar = new RigidBody(playerRigidBody);

		playar.setFriction(5f);
		playar.applyCentralImpulse(new Vector3f(0, 0, 0));
		playar.setActivationState(CollisionObject.DISABLE_DEACTIVATION);

		dynamicworld.addRigidBody(playar);
	}

	public void displaymaze(GL gl) {
		float wallColour[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
		for (int i = 0; i < mazeblocks.size(); i++) {
			gl.glPushMatrix();
			Transform trans = new Transform();
			mazeblocks.get(i).getMotionState().getWorldTransform(trans);
			float x = trans.origin.x;
			float y = trans.origin.y;
			float z = trans.origin.z;

			gl.glTranslatef(x, y, z);
			gl.glTranslatef(-5 / 2f, -5 / 2f, -5 / 2f); // Textured walls worden
														// vanuit hoekpunt
														// getekend ipv origin
														// blok.
			gl.glScaled(5, 5, 5); // Grootte van muur blok.
			Textureloader.Wall(gl);
			// glut.glutSolidCube(5f);
			gl.glPopMatrix();
			// Textureloader.Floor(gl, 5, maze);
		}
	}

	public static boolean isNewWall(double X, double Z) {
		for (int i = 0; i < mazeblocks.size(); i++) {
			Transform trans = new Transform();
			mazeblocks.get(i).getMotionState().getWorldTransform(trans);

			if (Math.abs(X - trans.origin.x) < 2.5
					|| Math.abs(Z - trans.origin.z) < 2.5) {
				return true;
			}
			// System.out.println("new: " + trans.origin.x + ", " +
			// trans.origin.z);
			// System.out.println("old: " + X + ", " + Z);
		}
		return false;
	}

	public void display(GL gl) {
		for (int i = 0; i < bullets.size(); i++) {
			GLUT glut = new GLUT();
			float wallColour[] = { 30.0f, 10.0f, 30.0f, 1.0f };
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);

			Transform trans = new Transform();
			bullets.get(i).getMotionState().getWorldTransform(trans);
			float x = trans.origin.x;
			float y = trans.origin.y;
			float z = trans.origin.z;

			gl.glPushMatrix();
			gl.glTranslatef(x, y, z);
			// glut.glutSolidCube(0.2f);
			Textureloader.Stuiterbal(gl, 0.4f, 10);

			gl.glPopMatrix();
		}
	}

	public void removeBullet(int j) {
		dynamicworld.removeRigidBody(bullets.get(j));
		bullets.remove(j);
		Bullets.remove(j);
	}

	public void updateBullets() {
		for (int j = 0; j < Bullets.size(); j++) {
			Bullets.get(j).update();
			if (!Bullets.get(j).getBulletState()) {
				removeBullet(j);
			}
		}
	}

	public Player updatePlayer(Player play) {
		Transform transform = new Transform();
		transform.setIdentity();
		playar.getWorldTransform(transform);
		play.setLocationX(transform.origin.x);
		play.setLocationY(transform.origin.y);
		play.setLocationZ(transform.origin.z);

		return play;
	}

	public void update(int deltaTime, ArrayList<NyanCat> Nyans, Player play) {

		dynamicworld.stepSimulation(1 / 62f, 0, 1);
		updateBullets();
		CollisionCheck(Nyans);
		updatePlayer(play);
		updateMaze();
	}

	private void updateMaze() {
		for (int i = 0; i < mazeblocks.size(); i++) {
			Transform trans = new Transform();
			mazeblocks.get(i).getMotionState().getWorldTransform(trans);

			if (!MazeRunner.maze.isWall((double) trans.origin.x,
					(double) trans.origin.z)) {
				MazeRunner.maze.changeMaze((double) trans.origin.x,
						(double) trans.origin.z, (double) oldx.get(i),
						(double) oldz.get(i));
			}
			oldx.set(i, trans.origin.x);
			oldz.set(i, trans.origin.z);
		}
	}

	public void CollisionCheck(ArrayList<NyanCat> Nyans) {
		for (int i = 0; i < Nyans.size(); i++) {
			for (int j = 0; j < bullets.size(); j++) {
				Transform trans = new Transform();
				trans = bullets.get(j).getWorldTransform(trans);
				if (trans.origin.x > Nyans.get(i).getLocationX() - 2f
						&& trans.origin.x < Nyans.get(i).getLocationX() + 2f
						&& trans.origin.z > Nyans.get(i).getLocationZ() - 2f
						&& trans.origin.z < Nyans.get(i).getLocationZ() + 2f
						&& trans.origin.y > Nyans.get(i).getLocationY() - 1f
						&& trans.origin.y < Nyans.get(i).getLocationY() + 10f) {
						if (Weapon.getNewWeapon() == 2) {
							nyans.get(i).setHP(nyans.get(i).getHP() - 100);
							removeBullet(j); 
						} else {
							nyans.get(i).setHP(nyans.get(i).getHP() - 50);
							removeBullet(j); 
						}
				}
			}
		}
	}

	public boolean updateNyanhealth(int i) {
		if (nyans.get(i).getHP() <= 0) {
			nyans.remove(i);
			amountofNyans = amountofNyans - 1;
			return true;
		}
		return false;
	}

	public void update(Player play) {
		if (!UserInput.zoom) {
			// updates movement
			boolean net_gebukt = play.getControl().getNet_gebukt();
			boolean duck = play.getControl().getDuck();
			boolean jump = play.getControl().getJump();
			boolean forward = play.getControl().getForward();
			boolean back = play.getControl().getBack();
			boolean left = play.getControl().getLeft();
			boolean right = play.getControl().getRight();
			boolean sprint = play.getControl().getSprint();
			boolean floor = false;
			Transform trans = playar.getWorldTransform(new Transform());
			if (trans.origin.y <= 2.6 && trans.origin.y >= 2.4
					|| trans.origin.y <= 7.6 && trans.origin.y >= 7.4) {
				floor = true;
			} else {
				floor = false;
			}

			// bukken
			if (trans.origin.y <= 2.6 && trans.origin.y >= 1) {
				if (duck) {
					playar.setCollisionShape(new BoxShape(new Vector3f(1f, 1f,
							1f)));
					Transform transform = new Transform();
					transform.origin.set(new Vector3f((float) play
							.getLocationX(), 1f, (float) play.getLocationZ()));
					playar.setCenterOfMassTransform(transform);
					// playar.translate(new Vector3f(0,-0.05f,0));
				}
			} else if (net_gebukt) {
				System.out.println("ik heb net gebukt");
				playar.translate(new Vector3f(0, 1.5f, 0));
				playar.setCollisionShape(new BoxShape(
						new Vector3f(1f, 2.5f, 1f)));
				play.getControl().setNet_gebukt(false);
			}

			float speed = 10f;

			if (sprint && energy >= 0) {
				if (energy > 0) {
					energy -= 3;
					speed = 20f;
				}
			}
			if (energy < 1000) {
				energy++;
			}

			float jumpheight = 8f;

			if (floor) { // je mag pas een actie doen als je op de vloer staat
				if (jump && energy >= 100) { // Druk je de spring toets in
												// tijdens het lopen?
					if (forward && left) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2,
								jumpheight, -(float) Math.cos(Math
										.toRadians(play.getHorAngle()))
										* speed
										/ 2
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (forward && right) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2,
								jumpheight, -(float) Math.cos(Math
										.toRadians(play.getHorAngle()))
										* speed
										/ 2
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (forward) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed, jumpheight, -(float) Math.cos(Math
								.toRadians(play.getHorAngle())) * speed));
					} else if (back && left) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2,
								jumpheight, (float) Math.cos(Math
										.toRadians(play.getHorAngle()))
										* speed
										/ 2
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (back && right) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2,
								jumpheight, (float) Math.cos(Math
										.toRadians(play.getHorAngle()))
										* speed
										/ 2
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (back) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed, jumpheight, (float) Math.cos(Math
								.toRadians(play.getHorAngle())) * speed));
					} else if (left) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* speed, jumpheight, (float) Math.sin(Math
								.toRadians(play.getHorAngle())) * speed));
					} else if (right) {
						energy -= 100;
						playar.setLinearVelocity(new Vector3f((float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* speed, jumpheight, -(float) Math.sin(Math
								.toRadians(play.getHorAngle())) * speed));
					} else { // Nee ik sta stil en wil gewoon even springen, ja!
						energy -= 100;
						playar.applyCentralImpulse(new Vector3f(0, 80f, 0));
					}
				} else { // Fuck dat springen, ik wil gewoon lopen.
					if (forward && left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2, 0,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* speed
										/ 2
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (forward && right) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2, 0,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* speed
										/ 2
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (forward) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed, 0f, -(float) Math.cos(Math
								.toRadians(play.getHorAngle())) * speed));
					} else if (back && left) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2, 0,
								(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* speed
										/ 2
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (back && right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed
								/ 2
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * speed / 2, 0,
								(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* speed
										/ 2
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * speed / 2));
					} else if (back) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* speed / 2, 0, (float) Math.cos(Math
								.toRadians(play.getHorAngle())) * speed / 2));
					} else if (left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* speed / 2, 0, (float) Math.sin(Math
								.toRadians(play.getHorAngle())) * speed / 2));
					} else if (right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* speed / 2, 0, -(float) Math.sin(Math
								.toRadians(play.getHorAngle())) * speed / 2));
					}

				}

			}
		}

	}

	/**
	 * A simple timer for the bullets, so they get removed from the game after a
	 * certain amount of time
	 * 
	 * @author Michiel
	 * 
	 */
	public class BulletTimer {

		private int time;
		private boolean bullet;

		public BulletTimer() {
			bullet = true;
			time = 80;
		}

		public void update() {
			time = time - 1;
			if (time <= 0) {
				BulletStop();
			}
		}

		public void BulletStop() {
			bullet = false;
		}

		public boolean getBulletState() {
			return bullet;
		}

	}

}