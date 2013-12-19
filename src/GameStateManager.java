import javax.swing.JFrame;

import leveleditor.editor;

public class GameStateManager {

	private static final long serialVersionUID = -2385197562776088374L;
	private int gameState;
	public final static int MENU_STATE = 0;
	public final static int MAZE_STATE = 1;
	public final static int LOAD_STATE = 2;
	public final static int EDITOR_STATE = 3;
	
	public MazeRunner mazerunner;
	public LevelSelector levelselector;
	public Menu menu = new Menu(this);
 
	public GameStateManager() {
		gameState = MENU_STATE;
	}

	public JFrame setGameState(int state) {
				
		if(state == MENU_STATE) {
			Game.frame.setVisible(false);
			Game.frame.getContentPane().removeAll();
			Game.frame.add(Game.gsm.menu);
			this.gameState = state;
			Game.frame.setVisible(true);
		}
		else if(state == MAZE_STATE) {
			Game.frame.getContentPane().removeAll();
			//Game.frame.remove(Game.gsm.menu);
			mazerunner = new MazeRunner();
			Game.frame.add(mazerunner.canvas); 
			this.gameState = state;	
			Textureloader.load = false;
		}		
		else if(state == LOAD_STATE) {
			//Game.frame.setVisible(false);
			Game.frame.remove(Game.gsm.menu);
			levelselector = new LevelSelector();
			Game.frame.add(levelselector);
			this.gameState = state;	
			Game.frame.setVisible(true);
		}		
		else if(state == EDITOR_STATE) {
			new editor();
			this.gameState = state;	
		}
		
		return Game.frame;
		
	}
 
	public int getGameState() {
		return gameState;
	}

}
