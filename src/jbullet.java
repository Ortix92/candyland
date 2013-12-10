import java.util.ArrayList;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.BroadphasePair;
import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.broadphase.OverlapCallback;
import com.bulletphysics.collision.broadphase.OverlappingPairCache;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

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
	private RigidBody groundbody;
	private RigidBody playar;

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

		CollisionShape boxshape = new BoxShape(
				new Vector3f(1.25f, 1.25f, 1.25f));
		Transform p = new Transform();
		p.setRotation(new Quat4f(0, 0, 0, 1));
		p.origin.set(27.5f, 5f, 27.5f);
		MotionState boxMotionState = new DefaultMotionState(p);
		float mass = 20;
		Vector3f Inertia = new Vector3f(0, 0, 0);
		boxshape.calculateLocalInertia(mass, Inertia);
		RigidBodyConstructionInfo boxRigidBodyInfo = new RigidBodyConstructionInfo(
				mass, boxMotionState, boxshape, Inertia);
		boxRigidBody = new RigidBody(boxRigidBodyInfo);
		dynamicworld.addRigidBody(boxRigidBody);

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
					RigidBodyConstructionInfo mazeinfo = new RigidBodyConstructionInfo(
							100000, mazeMotionState, mazeshape, Inertia);
					RigidBody mazebody = new RigidBody(mazeinfo);
					mazebody.setFriction(0f);
					dynamicworld.addRigidBody(mazebody);
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
		RigidBodyConstructionInfo nyaninfo = new RigidBodyConstructionInfo(
				4000, nyanstate, nyanshape, Inertia);
		RigidBody nyanbody = new RigidBody(nyaninfo);
		nyanbody.setCollisionFlags(nyanbody.getCollisionFlags()
				| CollisionFlags.KINEMATIC_OBJECT);
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
		p.origin.set(x - (float) Math.sin(Math.toRadians(horAngle)), y, z - (float)Math.cos(Math.toRadians(horAngle)));
		DefaultMotionState bulletmotion = new DefaultMotionState();
		bulletmotion.setWorldTransform(p);
		float mass = 0.002f;
		Vector3f Inertia = new Vector3f(0, 0, 0);
		bulletshape.calculateLocalInertia(mass, Inertia);
		RigidBodyConstructionInfo boxRigidBodyInfo = new RigidBodyConstructionInfo(
				mass, bulletmotion, bulletshape, Inertia);
		RigidBody bullet = new RigidBody(boxRigidBodyInfo);
		bullet.setLinearVelocity(new Vector3f(35 * (-(float) Math.sin(Math
				.toRadians(horAngle))), 35 * (float) Math.sin(Math
				.toRadians(verAngle)), 35 * (-(float) Math.cos(Math
				.toRadians(horAngle)))));
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
				2, playerstate, playershape, Inertia);
		playar = new RigidBody(playerRigidBody);
		// float stepHeight = 0.35f;
		// player = new KinematicCharacterController(ghostobject, capsule,
		// stepHeight);
		playar.setFriction(5f);
		playar.setLinearVelocity(new Vector3f(0, 0, 0));
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

	public TestBox getBox(TestBox box) {
		Transform trans = new Transform();
		boxRigidBody.getMotionState().getWorldTransform(trans);
		box.setLocationX(trans.origin.x);
		box.setLocationY(trans.origin.y);
		box.setLocationZ(trans.origin.z);
		return box;
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
		// if (playar.getLinearVelocity(new Vector3f(0,0,0)).length() <= 15) {
		if (play.getControl().getForward()) {
			playar.setLinearVelocity(new Vector3f(-(float) Math.sin(Math
					.toRadians(play.getHorAngle())) * 10f, 0, -(float) Math
					.cos(Math.toRadians(play.getHorAngle())) * 10f));
		}
		if (play.getControl().getBack()) {
			playar.setLinearVelocity(new Vector3f((float) Math.sin(Math
					.toRadians(play.getHorAngle())) * 10f, 0, (float) Math
					.cos(Math.toRadians(play.getHorAngle())) * 10f));
		}
		if (play.getControl().getLeft()) {
			playar.setLinearVelocity(new Vector3f(-(float) Math.cos(Math
					.toRadians(play.getHorAngle())) * 10f, 0, (float) Math
					.sin(Math.toRadians(play.getHorAngle())) * 10f));
		}
		if (play.getControl().getRight()) {
			playar.setLinearVelocity(new Vector3f((float) Math.cos(Math
					.toRadians(play.getHorAngle())) * 10f, 0, -(float) Math
					.sin(Math.toRadians(play.getHorAngle())) * 10f));
		}
		if (play.getControl().getForward() && play.getControl().getRight()) {
			playar.setLinearVelocity(new Vector3f(
					-(float) Math.sin(Math.toRadians(play.getHorAngle()))
							* 5f
							+ (float) Math.cos(Math.toRadians(play
									.getHorAngle())) * 5f, 0, -(float) Math
							.cos(Math.toRadians(play.getHorAngle()))
							* 5f
							- (float) Math.sin(Math.toRadians(play
									.getHorAngle())) * 5f));
		}
		if (play.getControl().getForward() && play.getControl().getLeft()) {
			playar.setLinearVelocity(new Vector3f(
					-(float) Math.sin(Math.toRadians(play.getHorAngle()))
							* 5f
							- (float) Math.cos(Math.toRadians(play
									.getHorAngle())) * 5f, 0, -(float) Math
							.cos(Math.toRadians(play.getHorAngle()))
							* 5f
							+ (float) Math.sin(Math.toRadians(play
									.getHorAngle())) * 5f));
		}
		if (play.getControl().getBack() && play.getControl().getRight()) {
			playar.setLinearVelocity(new Vector3f(
					(float) Math.sin(Math.toRadians(play.getHorAngle()))
							* 5f
							+ (float) Math.cos(Math.toRadians(play
									.getHorAngle())) * 5f, 0, (float) Math
							.cos(Math.toRadians(play.getHorAngle()))
							* 5f
							- (float) Math.sin(Math.toRadians(play
									.getHorAngle())) * 5f));
		}
		if (play.getControl().getBack() && play.getControl().getLeft()) {
			playar.setLinearVelocity(new Vector3f(
					(float) Math.sin(Math.toRadians(play.getHorAngle()))
							* 5f
							- (float) Math.cos(Math.toRadians(play
									.getHorAngle())) * 5f, 0, (float) Math
							.cos(Math.toRadians(play.getHorAngle()))
							* 5f
							+ (float) Math.sin(Math.toRadians(play
									.getHorAngle())) * 5f));
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