import java.awt.event.*;

public class PlayState extends GameState {

	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public void update() {
		
	}	
	
	public void draw() {

        Game.window.setTitle("CandyLand Nycan Slayer - Playing");
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE) {
			gsm.setCurrentState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyReleased(int k) {
	
	}
	
}
