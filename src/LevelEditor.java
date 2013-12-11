import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LevelEditor extends JPanel implements ActionListener,
		WindowListener, ChangeListener {
	private int screenWidth = 600, screenHeight = 600; // Screen size.
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15; // initial frames per second

	public LevelEditor() {
		// Create and set up the window.
		JFrame frame = new JFrame("Nyan Level Editor");
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon("src/nyan-cat.png").getImage());
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
//		frame.pack();

		JPanel panel = new JPanel();
		panel.setBackground(Color.lightGray);
		
		JButton button1 = new JButton("Test");
		JLabel label = new JLabel("Test Label");
		
		panel.add(button1);
		panel.add(label);
		
		frame.add(panel);
		


	}

	public static void main(String[] args) {
		new LevelEditor();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
