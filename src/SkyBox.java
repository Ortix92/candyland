
import javax.media.opengl.GL;



public class SkyBox implements VisibleObject {
        // puts a SkyBox around the player.
        Player player;
        double size;

        SkyBox(Player pl,double size){
                player=pl;
                this.size=size;
        }
        @Override
        public void display(GL gl) {
                gl.glTranslated(player.getLocationX(), player.getLocationY()-5, player.getLocationZ() );
                gl.glTranslated(-size/2,0,-size/2);
                Textureloader.SkyBox(gl,size);
                gl.glTranslated(-player.getLocationX(),-player.getLocationY()+5, -player.getLocationZ());
                gl.glTranslated(size/2,0,size/2);
        }
        

}