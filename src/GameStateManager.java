import java.util.ArrayList;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;

	public static final int MENUSTATE = 0;
	public static final int MAZESTATE = 1;
	private boolean onBoot = false;

	public GameStateManager() {
		System.out.println("gamestate manager");
		gameStates = new ArrayList<GameState>();
		if (onBoot == false) {
			gameStates.add(new MenuState(this));
			gameStates.add(new MazeState(this));
			onBoot = true;
		}

		currentState = MENUSTATE;

	}

	public void setCurrentState(int i) {
		switch (i) {
		case 10: // Enter
			currentState = MAZESTATE; // MazeState
			break;
		case 27: // Escape
			currentState = MENUSTATE; // MenuState
			break;

		}
	}

	public GameState getCurrentState() {
		return gameStates.get(currentState);
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