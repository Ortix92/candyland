import javax.media.opengl.GL;


public class SkyBox implements VisibleObject {
        // puts a SkyBox around the player.
        Player player;

        SkyBox(Player pl){
                player=pl;
        }
        @Override
        public void display(GL gl) {
                gl.glTranslated(player.getLocationX()-50, -3, player.getLocationZ()-50 );
                Textureloader.SkyBox(gl);
                gl.glTranslated(-player.getLocationX()+50, +3, -player.getLocationZ() +50);
        }
        

}