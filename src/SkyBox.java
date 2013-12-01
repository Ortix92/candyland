import javax.media.opengl.GL;


public class SkyBox implements VisibleObject{

	@Override
	public void display(GL gl) {
		TextureLoader.SkyBox(gl);
	}
	

}
