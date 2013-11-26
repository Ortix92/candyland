import java.util.ArrayList;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int MAZESTATE = 1;
	
	
	public GameStateManager() {
		
		gameStates = new ArrayList<GameState>();
		
		gameStates.add(new MenuState(this));
		gameStates.add(new MazeState(this));
		
		currentState = MENUSTATE;
		
	}
	
	public void setCurrentState(int i) {
		currentState = i;
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw() {
	  gameStates.get(currentState).draw();
	}
	
	public void keyPressed(int k) {
		System.out.println("GameStateManager.keyPressed");
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}

	
}