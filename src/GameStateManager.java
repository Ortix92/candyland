import java.awt.Component;

import javax.swing.JFrame;

public class GameStateManager {

	private static final long serialVersionUID = -2385197562776088374L;
	private int gameState;
	private int oldState;
	public final static int MENU_STATE = 0;
	public final static int MAZE_STATE = 1;
	public final static int LOAD_STATE = 2;
	public final static int EDITOR_STATE = 3;
	public final static int SCORE_STATE = 4;
	public final static int DEAD_STATE  = 5;
	
	public MazeRunner mazerunner; //= new MazeRunner();
	public Menu menu = new Menu(this);
	public ScoreScreen scores;
 
	public GameStateManager() {
		gameState = MENU_STATE;
	}

	public JFrame setGameState(int state) {
				
		if(state == MENU_STATE) {	
			Game.frame.remove(scores.canvas);
			if (mazerunner != null) {
			Game.frame.remove(mazerunner.canvas);
			}
			Game.frame.add(Game.gsm.menu);
			this.gameState = state;
		}
		else if(state == MAZE_STATE) {
		if (scores != null) {	
			Game.frame.remove(scores.canvas);
			scores = null;
		}
			Game.frame.remove(Game.gsm.menu);
			mazerunner = new MazeRunner();
			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
		}		
		else if(state == LOAD_STATE) {
			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
		}		
		else if(state == EDITOR_STATE) {
			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
		}
		else if(state == SCORE_STATE) {
			if (scores != null) {
			Game.frame.remove(scores.canvas);
			}
			scores = new ScoreScreen(0);
			Game.frame.remove(Game.gsm.menu);
			Game.frame.add(scores.canvas);
			this.gameState = state;
		}
		else if(state == DEAD_STATE) {
			Game.frame.remove(mazerunner.canvas);
			scores = new ScoreScreen(1);
			Game.frame.add(scores.canvas);
			this.gameState = state;
		}
		return Game.frame;
		
	}
 
	public int getGameState() {
		return gameState;
	}

}
