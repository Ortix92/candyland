import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class MazeState extends GameState {

	public MazeState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override
	public void draw() {
		MazeRunner mazerunner = new MazeRunner();
		add(mazerunner);
	}

	@Override
	public void keyPressed(int k) {
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
	}

}
