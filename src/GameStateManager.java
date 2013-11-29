import javax.swing.JFrame;

public class GameStateManager {

	private static final long serialVersionUID = -2385197562776088374L;
	private int gameState;
	public final static int MENU_STATE = 0;
	public final static int MAZE_STATE = 1;
	public MazeRunner mazerunner = new MazeRunner();
	public Menu menu = new Menu(this);

	public GameStateManager() {
		gameState = MENU_STATE;
	}

	public JFrame setGameState(int state, JFrame frame) {
		this.gameState = state;
		frame.add(mazerunner.canvas);
		return frame;
		
	}

	public int getGameState() {
		return gameState;
	}

}
