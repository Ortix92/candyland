import javax.media.opengl.GL;


public class RainbowBlock extends GameObject implements VisibleObject {
        double X2;
        double Y2;
        double Z2;
        double speed=1;
        Maze maze;

        public RainbowBlock(double X, double Y, double Z, double x2, double y2, double z2, Maze m){
                super(X,Y,Z);
                X2=x2;
                Y2=y2;
                Z2=z2;
                maze=m;
                //System.out.println("haaai");
        }
        
        
        public void update(){
                if(!UserInput.pause) {
                        // genormaliseerde richtingsvector:
                        double deltaX=(X2-getLocationX())/Math.sqrt(Math.pow(X2-getLocationX(),2)+Math.pow(Z2-getLocationZ(), 2));
                        double deltaZ=(Z2-getLocationZ())/Math.sqrt(Math.pow(X2-getLocationX(),2)+Math.pow(Z2-getLocationZ(), 2));
                        
                        if(maze.isWall(this.getLocationX()+deltaX,this.getLocationZ()+deltaZ)){
                                X2=this.getLocationX()+deltaX;
                                Z2=this.getLocationZ()+deltaZ;
                        }
                        else{
                                this.setLocationX(getLocationX()+deltaX*speed);
                                this.setLocationZ(getLocationZ()+deltaZ*speed);
                                //System.out.println("Updated");
                        }
                }
                
        }
        
        @Override
        public void display(GL gl) {
                // TODO Auto-generated method stub
                
                if(!((Math.abs(this.getLocationX()-X2)<speed)&&(Math.abs(this.getLocationY()-Y2)<=speed)&&(Math.abs(this.getLocationZ()-Z2)<=speed))){
                        gl.glPushMatrix();
                        gl.glTranslated(this.getLocationX(), this.getLocationY(), this.getLocationZ());
                        Textureloader.Rainbow(gl, 1);
                        gl.glPopMatrix();
                }
                
        }


        public void update(int deltaTime) {
                System.out.println("doe iets");
                // TODO Auto-generated method stub
                if(!UserInput.pause) {
                        System.out.println("doe iets");
                        // genormaliseerde richtingsvector:
                        double deltaX=(X2-getLocationX())/Math.sqrt(Math.pow(X2-getLocationX(),2)+Math.pow(Z2-getLocationZ(), 2));
                        double deltaZ=(Z2-getLocationZ())/Math.sqrt(Math.pow(X2-getLocationX(),2)+Math.pow(Z2-getLocationZ(), 2));
                        
                        if(maze.isWall(this.getLocationX()+deltaX,this.getLocationZ()+deltaZ)){
                                X2=this.getLocationX()+deltaX;
                                Z2=this.getLocationZ()+deltaZ;
                        }
                        else{
                                this.setLocationX(getLocationX()+deltaX*speed);
                                this.setLocationZ(getLocationZ()+deltaZ*speed);
                                //System.out.println("Updated");
                        }
                }
                
        }


		public boolean CollisionCheck(Player player) {
			if (getLocationX() <= player.getLocationX() + 1 && getLocationX() >= player.getLocationX() - 1 &&
					getLocationZ() <= player.getLocationZ() + 1 && getLocationZ() >= player.getLocationZ() - 1)	{
				return true; 
				}
		return false;
			
		}
        
        

}