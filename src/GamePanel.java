import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
 
public class GamePanel extends JPanel implements Runnable, KeyListener {
  
	private static final long serialVersionUID = -3478812483258109832L;
		public static final int WIDTH = 1024;
        public static final int HEIGHT = 768;
       
        private Thread thread;
       
        private boolean running;
       
        private GameStateManager gsm;
        
        public GamePanel() {
                super();
                setPreferredSize(new Dimension(WIDTH, HEIGHT));
                setFocusable(true);
                requestFocus();
        }
       
        public void addNotify() {
                super.addNotify();
                if(thread == null) {
                        addKeyListener(this);
                        thread = new Thread(this);
                        thread.start();
                }
        }
       
        // initializes variables
        private void init() {
               
                running = true;
                
                gsm =  new GameStateManager();
                               
        }
       
        // the "main" function
        public void run() {
               
                init();
               
                int FPS = 30;
                int targetTime = 1000 / FPS;
               
                long start;
                long elapsed;
                long wait;
               
                // simple game loop
                while(running) {
                       
                        start = System.nanoTime();
                       
                        update();
                        draw();
                        drawToScreen();
                       
                        elapsed = (System.nanoTime() - start) / 1000000;
                       
                        wait = targetTime - elapsed;
                        if(wait < 0) wait = 5;
                       
                        try {
                                Thread.sleep(wait);
                        }
                        catch(Exception e) {
                                e.printStackTrace();
                        }
                       
                }
                
               
        }
               
        // updates the game
        private void update() {
               gsm.update();
        }
       
        // draws the game onto an off-screen buffered image
        private void draw() {
               gsm.draw();
        }
       
        // draws the off-screen buffered image to the screen
        private void drawToScreen() {
                
        }
       
        public void keyTyped(KeyEvent k) {
               
        }
        
        public void keyPressed(KeyEvent k) {
             	gsm.keyPressed(k.getKeyCode());
        }
        
        public void keyReleased(KeyEvent k) {
        		gsm.keyPressed(k.getKeyCode());
        }
       
}