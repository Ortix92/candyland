public class GameStateManager {

	private int gameState;
	public final static int MENU_STATE = 0;
	public final static int MAZE_STATE = 1;

	public GameStateManager() {
		gameState = MENU_STATE;
	}

	public void setGameState(int state) {
		this.gameState = state;
	}

	public int getGameState() {
		return gameState;
	}

}
