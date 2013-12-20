import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
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

	private ArrayList<Bullet> Bullets;
	private ArrayList<NyanCat> nyans;

	public DiscreteDynamicsWorld dynamicworld;
	public int maxSubSteps;
	public float timeStep, fixedTimeStep;
	private CollisionDispatcher dispatcher;
	private ConstraintSolver solver;
	private DefaultCollisionConfiguration collisionConfiguration;
	private BroadphaseInterface broadphase;

	private ObjectArrayList<RigidBody> nyanies;
	private int amountofNyans;
	private RigidBody boxRigidBody;
	private ObjectArrayList<RigidBody> bullets;
	private ObjectArrayList<RigidBody> mazeblocks = new ObjectArrayList<RigidBody>();
	private RigidBody groundbody;
	private RigidBody playar;
	private Maze maze;

	public jbullet(int n) {
		Bullets = new ArrayList<Bullet>();
		nyanies = new ObjectArrayList<RigidBody>();
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
		this.maze=maze;
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
					RigidBodyConstructionInfo mazeinfo = new RigidBodyConstructionInfo(
							100000, mazeMotionState, mazeshape, Inertia);
					RigidBody mazebody = new RigidBody(mazeinfo);
					dynamicworld.addRigidBody(mazebody);
					mazeblocks.add(mazebody);
				}
			}
		}
	}

	public void initNyan(NyanCat nyancat) {
		CollisionShape nyanshape = new BoxShape(new Vector3f(1f, 2f, 3f));
		Transform nyan = new Transform();
		nyan.setRotation(new Quat4f((float) nyancat.getHorAngle(), 0f, 0f, 1f));
		nyan.origin.set((float) nyancat.getLocationX(),
				(float) nyancat.getLocationY(), (float) nyancat.getLocationZ());
		KinematicMotionState nyanstate = new KinematicMotionState();
		nyanstate.setWorldTransform(nyan);
		Vector3f Inertia = new Vector3f(0, 0, 0);
		RigidBodyConstructionInfo nyaninfo = new RigidBodyConstructionInfo(5,
				nyanstate, nyanshape, Inertia);
		RigidBody nyanbody = new RigidBody(nyaninfo);
		// nyanbody.setCollisionFlags(nyanbody.getCollisionFlags()
		// | CollisionFlags.KINEMATIC_OBJECT);
		nyanbody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		dynamicworld.addRigidBody(nyanbody);
		nyanies.add(nyanbody);
		nyans.add(nyancat);
	}

	public void CreateBullet(float x, float y, float z, float verAngle,
			float horAngle, Camera camera) {
		CollisionShape bulletshape = new SphereShape(0.5f);
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

		int velocityScalar = 50;
		if(Weapon.getNewWeapon()==2){
			velocityScalar=velocityScalar*10;
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
		dynamicworld.addRigidBody(bullet);
		bullets.add(bullet);
	}

	public void initPlayer(float x, float y, float z) {
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(x, y, z);

		// Vector3f worldMin = new Vector3f(-1000f,-1000f,-1000f);
		// Vector3f worldMax = new Vector3f(1000f,1000f,1000f);
		// AxisSweep3 sweepBP = new AxisSweep3(worldMin, worldMax);

		// PairCachingGhostObject ghostObject = new PairCachingGhostObject();
		DefaultMotionState playerstate = new DefaultMotionState();
		CollisionShape playershape = new BoxShape(new Vector3f(1f, 2.5f, 1f));
		playerstate.setWorldTransform(startTransform);
		// sweepBP.getOverlappingPairCache().setInternalGhostPairCallback(new
		// GhostPairCallback());
		// ConvexShape capsule = new CapsuleShape(characterWidth,
		// characterHeight);
		// ghostobject.setCollisionShape(capsule);
		// ghostobject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		Vector3f Inertia = new Vector3f(0, 0, 0);
		RigidBodyConstructionInfo playerRigidBody = new RigidBodyConstructionInfo(
				20, playerstate, playershape, Inertia);
		playar = new RigidBody(playerRigidBody);
		// float stepHeight = 0.35f;
		// player = new KinematicCharacterController(ghostobject, capsule,
		// stepHeight);
		playar.setFriction(5f);
		playar.applyCentralImpulse(new Vector3f(0, 0, 0));
		playar.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		// dynamicworld.addCollisionObject(ghostobject,
		// CollisionFilterGroups.CHARACTER_FILTER,
		// (short)(CollisionFilterGroups.STATIC_FILTER |
		// CollisionFilterGroups.DEFAULT_FILTER));
		dynamicworld.addRigidBody(playar);
		// dynamicworld.addAction(player);

	}

	public void updateNyanpos(int i, NyanCat newnyan) {
		RigidBody nyan = nyanies.get(i);
		Transform trans = new Transform();
		trans.setRotation(new Quat4f((float) newnyan.getHorAngle(), 0, 0, 1));
		trans.origin.set((float) newnyan.getLocationX(),
				(float) newnyan.getLocationY(), (float) newnyan.getLocationZ());
		nyan.setWorldTransform(trans);
		dynamicworld.addRigidBody(nyan);
		dynamicworld.removeRigidBody(nyanies.get(i));
		nyanies.remove(i);
		nyanies.add(nyan);

	}

	public void displaymaze(GL gl) {
		GLUT glut = new GLUT();
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
			gl.glTranslatef(-5/2f,-5/2f,-5/2f); // Textured walls worden vanuit hoekpunt getekend ipv origin blok. 
			gl.glScaled(5,5,5); // Grootte van muur blok. 
			Textureloader.Wall(gl);
			//glut.glutSolidCube(5f);
			gl.glPopMatrix();
			//Textureloader.Floor(gl, 5, maze);
		}
	}

	public void display(GL gl, int i) {
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
		glut.glutSolidCube(0.25f);
		gl.glPopMatrix();
	}

	public void removeBullet(int j) {
		dynamicworld.removeRigidBody(bullets.get(j));
		bullets.remove(j);
		Bullets.remove(j);
	}

	public void updateBullets() {
		for (int j = 0; j < Bullets.size(); j++) {
			if (Bullets.get(j).getBulletState()) {
				Transform trans = new Transform();
				bullets.get(j).getMotionState().getWorldTransform(trans);
				Bullets.get(j).setLocationX(trans.origin.x);
				Bullets.get(j).setLocationY(trans.origin.y);
				Bullets.get(j).setLocationZ(trans.origin.z);
			} else {
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

	public void update(int deltaTime, TestBox box, ArrayList<NyanCat> Nyans,
			Player play) {

		dynamicworld.stepSimulation(1f);
		for (int i = 0; i < nyans.size(); i++) {

			// updateNyanpos(0,Nyans[0]);
			RigidBody nyan = nyanies.get(i);
			dynamicworld.removeRigidBody(nyan);
			Transform trans = new Transform();
			trans.setRotation(new Quat4f((float) Nyans.get(i).getHorAngle(), 0,
					0, 1));
			trans.origin
					.set((float) Nyans.get(i).getLocationX(), (float) Nyans
							.get(i).getLocationY(), (float) Nyans.get(i)
							.getLocationZ());
			nyan.setWorldTransform(trans);
			dynamicworld.addRigidBody(nyan);
		}
		updateBullets();
		CollisionCheck();
		updatePlayer(play);
	}

	public ArrayList<Bullet> getbullets() {
		return Bullets;
	}


	public void CollisionCheck() {
		for (int j = 0; j < bullets.size(); j++) {
			for (int i = 0; i < nyanies.size(); i++) {
				Transform trans = new Transform();
				Transform trans2 = new Transform();
				trans = bullets.get(j).getWorldTransform(trans);
				trans2 = nyanies.get(i).getWorldTransform(trans2);
				if (trans.origin.x > trans2.origin.x - 2f
						&& trans.origin.x < trans2.origin.x + 2f
						&& trans.origin.z > trans2.origin.z - 2f
						&& trans.origin.z < trans2.origin.z + 2f) {
					nyans.get(i).setHP(nyans.get(i).getHP() - 50);
					Bullets.get(j).BulletStop();
				}
			}
		}
	}

	public ArrayList<NyanCat> getNyan() {
		return nyans;
	}

	public boolean updateNyanhealth(int i) {
		// if(Nyan[i].getHP()>-1){
		// System.out.println(nyans.get(i).getHP());
		if (nyans.get(i).getHP() <= 0) {
			System.out.println(nyans.get(i).getLocationX());
			nyans.remove(i);
			dynamicworld.removeRigidBody(nyanies.get(i));
			nyanies.remove(i);
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
			boolean floor = false;
			Transform trans = playar.getWorldTransform(new Transform());
			if (trans.origin.y <= 2.6 && trans.origin.y >= 2.4) {
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

			if (floor) { // je mag pas een actie doen als je op de vloer staat
				if (jump) { // Druk je de spring toets in tijdens het lopen?
					if (forward && left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 10,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 05f));
					} else if (forward && right) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 10,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 05f));
					} else if (forward) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle())) * 10f,
								10f, -(float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 10f));
					} else if (back && left) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 10,
								(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 5f));
					} else if (back && right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 10f,
								(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 05f));
					} else if (back) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle())) * 10f,
								10f, (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 0.f));
					} else if (left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.cos(Math.toRadians(play.getHorAngle())) * 10f,
								10, (float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 0.f));
					} else if (right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.cos(Math.toRadians(play.getHorAngle())) * 10f,
								10, -(float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 10f));
					} else { // Nee ik sta stil en wil gewoon even springen, ja!
						playar.applyCentralImpulse(new Vector3f(0, 80f, 0));
					}
				} else { // Fuck dat springen, ik wil gewoon lopen.
					if (forward && left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 0,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										+ (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 05f));
					} else if (forward && right) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 0,
								-(float) Math.cos(Math.toRadians(play
										.getHorAngle()))
										* 05f
										- (float) Math.sin(Math.toRadians(play
												.getHorAngle())) * 05f));
					} else if (forward) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.sin(Math.toRadians(play.getHorAngle())) * 10f,
								0f, -(float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 10f));
					} else if (back && left) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								- (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 0, (float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* 05f
								+ (float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 5f));
					} else if (back && right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle()))
								* 05f
								+ (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 05f, 0, (float) Math
								.cos(Math.toRadians(play.getHorAngle()))
								* 05f
								- (float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 05f));
					} else if (back) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.sin(Math.toRadians(play.getHorAngle())) * 10f,
								0, (float) Math.cos(Math.toRadians(play
										.getHorAngle())) * 10f));
					} else if (left) {
						playar.setLinearVelocity(new Vector3f(-(float) Math
								.cos(Math.toRadians(play.getHorAngle())) * 10f,
								0, (float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 10f));
					} else if (right) {
						playar.setLinearVelocity(new Vector3f((float) Math
								.cos(Math.toRadians(play.getHorAngle())) * 10f,
								0, -(float) Math.sin(Math.toRadians(play
										.getHorAngle())) * 10f));
					}

				}

			}
		}

	}

	public class KinematicMotionState extends MotionState {
		private Transform worldTransform;

		public KinematicMotionState() {
			worldTransform = new Transform();
			worldTransform.setIdentity();
		}

		@Override
		public Transform getWorldTransform(Transform out) {
			out.set(worldTransform);
			return out;
		}

		public void setWorldTransform(Transform worldTrans) {
			worldTransform.set(worldTrans);
		}
	}

}