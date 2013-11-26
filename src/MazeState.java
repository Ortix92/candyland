import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class MazeState extends GameState {
	public JButton button;

	public MazeState(GameStateManager gsm) {

		this.gsm = gsm;
	}

	@Override
	public void draw() {
		System.out.println(">> test");
		MazeRunner mazerunner = new MazeRunner();
		button = new JButton("test");
		add(button);
		add(mazerunner);

	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
	}

}
