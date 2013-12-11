import javax.media.opengl.GL;


public class PickUp extends GameObject implements VisibleObject{
Player player;
boolean pickup=false;
int i=0;

	public PickUp(double X, double Z,Player playr){
		super(X,2,Z);
		player=playr;
		
	}

	public boolean getPickup(){
		return pickup;
	}

	@Override
	public void display(GL gl) {
			if(!pickup){
				gl.glPushMatrix();
				
				gl.glTranslated(this.getLocationX(),this.getLocationY(),this.getLocationZ());
				gl.glRotated(i,0,1,0);
				gl.glTranslated(-0.5, -0.5, -0.5);
				if(!Textureloader.load){
					Textureloader.load();
					Textureloader.load=true;
				}
				Textureloader.PickUp.enable();
				Textureloader.PickUp.bind();
				Textureloader.drawCube(gl, false);
				Textureloader.PickUp.disable();
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
