

public abstract class GameState {

	protected GameStateManager gsm;
	
	public abstract void update();
	public abstract void draw();
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
