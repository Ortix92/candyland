import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class NyanCat extends GameObject implements VisibleObject{
	
	private double horAngle; // sets the direction in which Nyan looks
	private Control Nyancontrol=null; // the code to tell Nyan what to do
	private double speed=0.01; // the speed of Nyan
	private int i=0; // hulp integer for floating up and down
	private int j=0; // hulp integer for floating up and down
	private double lastX=getLocationX();
	private double lastZ=getLocationZ();

	
	// makes a NyanCat on the location x, y , z, looking in direction h
	public NyanCat(double x, double y, double z, double h){
		super(x,y,z);
		horAngle=h+90;


	}
	
	
	// tells Nyan to what he should listen
	public void setControl(Control control) {
		this.Nyancontrol = control;
	}
	
	/**
	 * Gets the Control object currently controlling the NyanCat
	 * 
	 * @return
	 */
	public Control getControl() {
		return Nyancontrol;
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * 
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * 
	 * @param horAngle
	 *            the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
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
			//update whether Nyan should move according to NyanCatInput:
		//Nyancontrol.update(); does not do anything yet.
		
		//Floating animation:
		//Fly up 70 times:
		boolean up=false;
		//System.out.println("Update");
				if((i>=0)&&(i<70)){
					setLocationY(getLocationY()+0.5*getSpeed());
					i=i+1;	
					//System.out.println("Up! i="+i);
					up=true;
				}
		// fly down 70 times:
			if((i>=70)&&(!up)){
				setLocationY(getLocationY()-0.5*getSpeed());
				j=j+1;
				//System.out.println("Down! j="+j);
				if((j>69)){
					i=0;
					j=0;
				}
			}
			
			
			// Run Nyan Run!:
			if((i>=0)&&(i<35)){
				setLocationX(getLocationX()+5*getSpeed());
			}
			if((i>=35)&&(j==0)){
				setLocationZ(getLocationZ()+5*getSpeed());
			}
			if((j<=35)&&(i==70)){
				setLocationX(getLocationX()-5*getSpeed());
			}
			if((j>=35)&&(i==70)){
				setLocationZ(getLocationZ()-5*getSpeed());
			}
			
			

			
			//TODO: Put something in that Nyan has to do with certain inputs given by NyanCatInput					
	}
	@Override
	public  void display(GL gl) {
			GLUT glut = new GLUT();
			boolean move=false;
			boolean moveagain=false;
			// Coords for NyanLocation:
			double X=this.getLocationX()+5/2; // The 5/2 part is because the "body" of Nyan
			double Y=this.getLocationY()+5/2; // is build around this point, it is the center
			double Z=this.getLocationZ()+5/2; 
			if((lastX!=getLocationX()||(lastZ!=getLocationZ()))){
				if(move){
					moveagain=true;
					move=true;
					
				}
			}
			
		//display the Nyan (its poptart) on its new location:
			gl.glTranslated(X ,Y , Z); // X,Y,Z are the coordinates of the middlepoint of the poptart of Nyan
			gl.glRotated(this.horAngle, 0, 1, 0); // rotate around Y-axis for the right orientation, 
			// make Nyan look to the right direction.
		
			// the appearance of Nyan:

			int roundness=10; // how round are the spheres and cones? Less round-> faster rendering. 
			float size=1; // measure for the size of Nyan
			float scalefactor=2; // measure for scaling of the poptart
			float NyanColor[] = { 0.8f, 0.8f, 0.8f, 0f }; // white Nyan
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, NyanColor, 0); // Nyan material. 
			
			//POPTART:
			gl.glScaled(scalefactor,scalefactor,1);	 // making the poptart cube into a cuboid,
			//make a textured poptart of size size:
			Textureloader.poptart(gl,size); // texturized cube

			// HEAD:
			gl.glScaled((float)1/(scalefactor), (float)1/scalefactor, 1); // unscale the head
			gl.glScaled(1,1,(float) 1.5); // scale the head
			// location of head with respect to the poptart:
			gl.glTranslated((0.7*scalefactor), -1/scalefactor*size,
					0);  
			Textureloader.head( gl,glut,size,roundness);
			
			//EARS:
			gl.glScaled(1, 1, (float)1/1.5); // unscale ears.
			gl.glTranslated(0,0.25*size, 0.25*size); // location of ear 1 with respect to head
			gl.glRotated(-90, 1, 0, 0);// point upwards. 
			glut.glutSolidCone(0.25*size, 0.5*size, roundness, roundness); // ear 1
			
			gl.glRotated(90, 1, 0, 0); // rotate back
			gl.glTranslated(0,0, -0.5*size); // location of ear 2 with respect to ear 1
			gl.glRotated(-90, 1, 0, 0); // point upwards. 
			glut.glutSolidCone(0.25*size, 0.5*size, roundness, roundness); // ear 2
			gl.glRotated(90, 1, 0, 0); // rotate back
			
			// PAWS:
			//translate back to middle of poptart:
			gl.glTranslated(0, 0, 0.5*size); 
			gl.glTranslated(0,-0.25*size, -0.25*size);
			gl.glTranslated(-(0.7*scalefactor), 1/scalefactor*size,
					0);  
			// making of the paws:
			// pawlocation with respect to middle of poptart:
			gl.glTranslated(0.4*scalefactor, -0.5*scalefactor, 0.2*size);  
			if(move){
				gl.glTranslated(0,0,0.2*size);
				move=false;
				if(moveagain){
					gl.glTranslated(0,0,-2*0.2*size);
				}
			}
			glut.glutSolidCube((float) (0.3*size)); // paw 1
			// paw location with respect to paw 1:
			gl.glTranslated(-0.8*scalefactor,0,0);
			glut.glutSolidCube((float) (0.3*size)); // paw 2
			//Paw location with respect to paw 2:
			gl.glTranslated(0, 0,- 0.5*size);
			glut.glutSolidCube((float) (0.3*size)); // paw 3
			// Paw location with respect to paw 3:
			gl.glTranslated(0.8*scalefactor, 0,0);
			glut.glutSolidCube((float) (0.3*size)); // paw 4
			
			// TAIL:
			// Tail movement:
			double ytail=0; // the translation in up direction of the next tail segment with respect to the last. 
			boolean up=false;
			if((i>=0)&&(i<35)){
				ytail= i*(0.3*size/70);
				up=true;
			}
			if((i>=35)&&(i<70)){
				ytail=(70-i)*(0.3*size/70);
			}
			if((i>=70)&&(!up)){
				if((j>=0)&&(j<35)){
					ytail= -j*(0.3*size)/70;
				}
				if((j>=35)&&(j<70)){
					ytail=(j-70)*(0.3*size/70);
				}
			}
			gl.glTranslated(-0.9*size*scalefactor, 0.7*size, 0.3*size); // translation with respect to paw 4. 
			glut.glutSolidCube((float) (0.3*size)); // tail part 1.
			gl.glTranslated(-0.3*size, ytail, 0); // translation with respect to tail part 1
			glut.glutSolidCube((float) (0.3*size));// tail part 2.
			gl.glTranslated(-0.3*size,ytail,0); // translation with respect to tail part 2. 
			glut.glutSolidCube((float) (0.3*size)); // tail part 3. 
			
			// TODO: if Nyan x and/or z location changes make paws and  head move
			// TODO: Nyan should leave a rainbow trail

			
	}

}
