import java.util.ArrayList;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	
	
	public GameStateManager() {
		
		gameStates = new ArrayList<GameState>();
		
		gameStates.add(new MenuState(this));
		gameStates.add(new PlayState(this));
		
		currentState = MENUSTATE;
		
	}
	
	public void setCurrentState(int i) {
		currentState = i;
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw() {
	  gameStates.get(currentState).draw();
	}
	
	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}
	
}