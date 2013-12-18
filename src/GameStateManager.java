import javax.swing.JFrame;

public class GameStateManager {

	private static final long serialVersionUID = -2385197562776088374L;
	private int gameState;
	public final static int MENU_STATE = 0;
	public final static int MAZE_STATE = 1;
	public final static int LOAD_STATE = 2;
	public final static int EDITOR_STATE = 3;
	
	public MazeRunner mazerunner;
	public Menu menu = new Menu(this);
 
	public GameStateManager() {
		gameState = MENU_STATE;
	}

	public JFrame setGameState(int state) {
				
		if(state == MENU_STATE) {	
			Game.frame.remove(mazerunner.canvas);
			Game.frame.add(Game.gsm.menu);
			this.gameState = state;
		}
		else if(state == MAZE_STATE) {
			Game.frame.remove(Game.gsm.menu);
			mazerunner = new MazeRunner();
			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
			Textureloader.load = false;
		}		
		else if(state == LOAD_STATE) {
//			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
		}		
		else if(state == EDITOR_STATE) {
//			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
		}
		
		return Game.frame;
		
	}
 
	public int getGameState() {
		return gameState;
	}

}
