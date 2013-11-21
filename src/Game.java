import javax.swing.JFrame;
 
public class Game {
 
		public static JFrame window;
	
        public static void main(String[] args) {
               
                window = new JFrame();
                window.setTitle("CandyLand Nycan Slayer");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
                window.setContentPane(new GamePanel());
               
                window.setResizable(true);
                window.pack();
                window.setVisible(true);
               
        }
       
}