import javax.swing.JFrame;

public class GameStateManager {

	private static final long serialVersionUID = -2385197562776088374L;
	private int gameState;
	public final static int MENU_STATE = 0;	//ESCAPE
	public final static int MAZE_STATE = 1;	//ENTER
	public MazeRunner mazerunner = new MazeRunner();
	public Menu menu = new Menu(this);
	
	
	public GameStateManager() {
		gameState = MENU_STATE;

	}

	public JFrame setGameState(int keynumber, JFrame frame) {
		
			
		switch(gameState) {
		
			case MENU_STATE:
				if(keynumber == 10) { 
					this.gameState = MAZE_STATE;
					frame.add(mazerunner.canvas);
				}
				
			case MAZE_STATE:
				if(keynumber == 27) { 
					this.gameState = MENU_STATE;
					System.out.println(menu);
					frame.removeAll();
					frame.add(menu);
					frame.setVisible(true);
				}
		
		}
	
		
		return frame;
		
	}

	public int getGameState() {
		return gameState;
	}

}
