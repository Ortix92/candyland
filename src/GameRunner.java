import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameRunner extends JFrame {

	public enum State { GAME, MAIN_MENU; }
	public static State state = State.MAIN_MENU;
	public static boolean state_changed = true;
	
	public static void main(String[] args) {
		 
		while(true) {
			
			System.out.print("");
		
			if(state_changed) {
				switch(state) {
					case MAIN_MENU:
						new Menu();
						break;
					case GAME:
						new MazeRunner();
						break;
				}
				
			state_changed = false;
			
			}
		
		}
		
	}
	
	public static void StateUpdater(State newState) {
		state = newState;
 	}
	
}
