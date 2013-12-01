import java.util.ArrayList;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
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

import javax.media.opengl.GL;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class jbullet {

	public DiscreteDynamicsWorld dynamicworld;
	  public int maxSubSteps;
	  public float timeStep, fixedTimeStep;
		private CollisionDispatcher dispatcher;
		private ConstraintSolver solver;
		private DefaultCollisionConfiguration collisionConfiguration;		
		
		private RigidBody boxRigidBody;
		private ObjectArrayList<RigidBody> bullets = new ObjectArrayList<RigidBody>();
	    private RigidBody groundbody;


public jbullet() {
	BroadphaseInterface broadphase = new DbvtBroadphase();

	collisionConfiguration = new DefaultCollisionConfiguration();
	dispatcher = new CollisionDispatcher(collisionConfiguration);

	solver = new SequentialImpulseConstraintSolver();

	dynamicworld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
	dynamicworld.setGravity(new Vector3f(0f,-10f,0f));
}

public void initObjects() {
	CollisionShape groundshape = new StaticPlaneShape(new Vector3f(0,1,0),0);
	Transform t = new Transform();
	t.setRotation(new Quat4f(0,0,0,1));
	t.transform(new Vector3f(0,-1,0));
	DefaultMotionState groundMotionState = new DefaultMotionState(t);
	RigidBodyConstructionInfo groundRigidBody = new RigidBodyConstructionInfo(0, groundMotionState, groundshape, new Vector3f(0,0,0));
	groundbody = new RigidBody(groundRigidBody);
	
	dynamicworld.addRigidBody(groundbody);
	
	CollisionShape boxshape = new BoxShape(new Vector3f(1.25f,1.25f,1.25f));
	Transform p = new Transform();
	p.setRotation(new Quat4f(0,0,0,1));
	p.origin.set(27.5f, 5f, 27.5f);
	MotionState boxMotionState = new DefaultMotionState(p);
	float mass = 20;
    Vector3f Inertia = new Vector3f(0,0,0); 
    boxshape.calculateLocalInertia(mass, Inertia);
    RigidBodyConstructionInfo boxRigidBodyInfo = new RigidBodyConstructionInfo(mass, boxMotionState , boxshape , Inertia);
	boxRigidBody = new RigidBody(boxRigidBodyInfo);
	dynamicworld.addRigidBody(boxRigidBody);
    
	
	
}

public void initMaze(Maze maze) {
	for( int i = 0; i < 10; i++ )
	{
       for( int j = 0; j < 10; j++ )
		{
          if (maze.isWall(i, j)) {
			CollisionShape mazeshape = new BoxShape(new Vector3f(2.5f,2.5f,2.5f));
        	Transform t = new Transform();
        	t.origin.set((float)(i*maze.SQUARE_SIZE + maze.SQUARE_SIZE/2f),(float)(maze.SQUARE_SIZE/2f),(float)(j*maze.SQUARE_SIZE + maze.SQUARE_SIZE/2f));
			DefaultMotionState mazeMotionState = new DefaultMotionState(t);
			Vector3f Inertia = new Vector3f(0,0,0);
			RigidBodyConstructionInfo mazeinfo = new RigidBodyConstructionInfo(0, mazeMotionState, mazeshape, Inertia);
			RigidBody mazebody = new RigidBody(mazeinfo);
			mazebody.setFriction(0f);
			dynamicworld.addRigidBody(mazebody);
			}
		}
	}
}	

public void CreateBullet(float x, float y, float z, float verAngle, float horAngle, Camera camera) {
	CollisionShape bulletshape = new SphereShape(0.5f);
	Transform p = new Transform();
	p.setRotation(new Quat4f(horAngle,verAngle,0,1));
	p.origin.set(x, y, z);
	DefaultMotionState bulletmotion = new DefaultMotionState();
	bulletmotion.setWorldTransform(p);
	float mass = 1;
    Vector3f Inertia = new Vector3f(0,0,0); 
    bulletshape.calculateLocalInertia(mass, Inertia);
    RigidBodyConstructionInfo boxRigidBodyInfo = new RigidBodyConstructionInfo(mass, bulletmotion , bulletshape , Inertia);
	RigidBody bullet = new RigidBody(boxRigidBodyInfo);
	bullet.setLinearVelocity(
			new Vector3f(50*(-(float)Math.sin(Math.toRadians(horAngle))),(float)Math.sin(Math.toRadians(verAngle)), 50*(-(float)Math.cos(Math.toRadians(horAngle)))));	
 //   bullet.applyForce(new Vector3f(-10,0,0), new Vector3f((float)camera.getVrpX(),(float)camera.getVrpY(),(float)camera.getVrpZ()));
 //   bullet.applyForce(new Vector3f(-10,0,0), new Vector3f(27.5f , 2.5f, 27.5f));
 //	bullet.setCollisionFlags(bullet.getCollisionFlags() | CollisionFlags.KINEMATIC_OBJECT);
//	bullet.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
	dynamicworld.addRigidBody(bullet);
	bullets.add(bullet);
}

public void updateBulletpos(Transform trans, RigidBody bullet) {
	if (bullet.getBroadphaseHandle() != null) {
	bullet.setWorldTransform(trans);
	}
}

public void DestroyBullet(int i) {
	dynamicworld.removeRigidBody(bullets.get(i));
}

public void display(GL gl, int i) {
	GLUT glut = new GLUT();
    float wallColour[] = { 0.5f, 10.0f, 30.0f, 1.0f };			
    gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
    
    Transform trans = new Transform();
	 bullets.get(i).getMotionState().getWorldTransform(trans);
	 float x = trans.origin.x;
	 float y = trans.origin.y;
	 float z = trans.origin.z;
	 
gl.glPushMatrix();
   gl.glTranslatef(x, y, z);

    glut.glutSolidSphere(0.05, 10, 10);
    gl.glPopMatrix();
}


//public ArrayList<Bullet> update(ArrayList<Bullet> bullet) {
//	 dynamicworld.stepSimulation(1/120f,10);
//	 ArrayList<Bullet> bul = new ArrayList<Bullet>();
//   if (bullet.size() != 0) {
//	 Transform trans = new Transform();
//	 bullets.get(0).getMotionState().getWorldTransform(trans);
//	 bullet.get(0).setLocationX(trans.origin.x);
//	 bullet.get(0).setLocationY(trans.origin.y);
//	 bullet.get(0).setLocationZ(trans.origin.z);
//	 bul.add(bullet.get(0));
 //  }
//	 return bul;

		
// }
	
public TestBox update(TestBox box) {
	 dynamicworld.stepSimulation(1/120f,10);
	 boxRigidBody.applyImpulse(new Vector3f(0,0,0), new Vector3f(1,0,0));
	 Transform trans = new Transform();
	 boxRigidBody.getMotionState().getWorldTransform(trans);
	 box.setLocationX(trans.origin.x);
	 box.setLocationY(trans.origin.y);
	 box.setLocationZ(trans.origin.z);

return box;
}

public void rayHit() {
	Vector3f rayStart = new Vector3f(27.5f, 2.5f, 25f);
	Vector3f rayEnd = new Vector3f(27.5f, 2.5f, 50f);
	ClosestRayResultCallback raycallback = new ClosestRayResultCallback(rayStart, rayEnd);
	dynamicworld.rayTest(rayStart, rayEnd, raycallback);
	
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