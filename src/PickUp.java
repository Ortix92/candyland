import javax.media.opengl.GL;


public class PickUp extends GameObject implements VisibleObject{
Player player;
boolean pickup=false;
int i=0;
int soort;

	public PickUp(double X, double Z,Player playr, int soort){
		super(X,2,Z);
		player=playr;
		this.soort=soort;
		// soort 1== Health+score multiplier
		// soort 2== Zoom wapen
		// soort 3== MoveBlock wapen
		System.out.println("Pick up made");
		
	}

	public boolean getPickup(){
		return pickup;
	}
	
	public int getSoort(){
		return this.soort;
	}

	@Override
	public void display(GL gl) {
			if(!pickup){
				gl.glPushMatrix();
				gl.glTranslated(this.getLocationX(),this.getLocationY(),this.getLocationZ());
				gl.glRotated(i,0,1,0);
				gl.glTranslated(-0.5, -0.5, -0.5);
				if(Textureloader.loadtextures){
					if(!Textureloader.load){
						Textureloader.load();
						Textureloader.load=true;
					}
					if(this.soort==1){
						Textureloader.PickUp.enable();
						Textureloader.PickUp.bind();
					}
					else{
						Textureloader.stuiterbal.enable();
						Textureloader.stuiterbal.bind();
					}
				}
					Textureloader.drawCube(gl, false);
					if(Textureloader.loadtextures){
						if(this.soort==1){
							Textureloader.PickUp.disable();
						}
						else{
							Textureloader.stuiterbal.disable();
							}
					
					}
				gl.glPopMatrix();
			}
		}
	
	
	
	public void update(int DeltaTime){
		i=i+1;
		if(Math.abs(player.getLocationX()-this.getLocationX())<1){
			if(Math.abs(player.getLocationZ()-this.getLocationZ())<1){
				System.out.println("Pick up!");
				pickup=true;
			}
		}
	}
}
