
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

class Menu extends JFrame
	{	
				  
    public Menu() {
		  JButton button_start = 		new JButton(new ImageIcon("images/menu/button_start.png"));
		  JButton button_load = 		new JButton(new ImageIcon("images/menu/button_load.png"));
		  JButton button_level_editor = new JButton(new ImageIcon("images/menu/button_level_editor.png"));
		  JButton button_options = 		new JButton(new ImageIcon("images/menu/button_options.png"));
		  JButton button_exit = 		new JButton(new ImageIcon("images/menu/button_exit.png"));

		  JFrame frame = new JFrame("Candyland Menu");
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  JPanel panel = new JPanel();
		  panel.setLayout(null);
		  
		  JLabel background = new JLabel(new ImageIcon("images/menu/Nyan-Cat-menu.gif"));
  		  
		  background.setBounds(0, 0, 1024, 768);
		  button_start.setBounds(0, 146, 561, 57);
		  button_load.setBounds(0, 235, 561, 57);
		  button_level_editor.setBounds(0, 324, 561, 57);
		  button_options.setBounds(0, 413, 561, 57);
		  button_exit.setBounds(0, 502, 561, 57);
		  
		  panel.add(button_start);
		  panel.add(button_load);
		  panel.add(button_level_editor);
		  panel.add(button_options);
		  panel.add(button_exit);
		  panel.add(background);
		  frame.add(panel);
		  
		  frame.setSize(1024,768);
		  
		  frame.setVisible(true);
		  
		  final JButton[] knoppen = new JButton[5];
		  knoppen[0] = button_start;
		  knoppen[1] = button_load;
		  knoppen[2] = button_level_editor;
		  knoppen[3] = button_options;
		  knoppen[4] = button_exit;
		  
		  final String[] knopnamen = new String[5];
		  knopnamen[0] = "button_start";
		  knopnamen[1] = "button_load";
		  knopnamen[2] = "button_level_editor";
		  knopnamen[3] = "button_options";
		  knopnamen[4] = "button_exit";
		  
		  for(int i = 0; i < knoppen.length; i++) {
			  
			  final int j = i;
			  knoppen[i].addMouseListener(new java.awt.event.MouseAdapter() {
			  		public void mouseEntered(java.awt.event.MouseEvent evt) { 
			  			String name = knopnamen[j];
			  			String imageplace = "images/menu/"+name+"_active.png";
			  			knoppen[j].setIcon(new ImageIcon(imageplace)); 
		  			}
					public void mouseExited(java.awt.event.MouseEvent evt) {
			  			String name = knopnamen[j];
			  			String imageplace = "images/menu/"+name+".png";
						knoppen[j].setIcon(new ImageIcon(imageplace)); 
					}
				    public void mouseClicked(java.awt.event.MouseEvent evt) {
				    	Sound.play("sounds/sonic_ring.wav");
				    	
					    	switch(j) {
						    	case 0:
						    		GameRunner.StateUpdater(GameRunner.State.GAME);
						    		GameRunner.state_changed = true;
						    	case 1:
						    		break;
						    	case 2:
						    		break;
						    	case 3:
						    		break;
						    	case 4:
						    		System.exit(0);
					    	}				    	
				    }
				  });			  
		  	
		  }		  
    }
   
    public static class Sound {
        public static synchronized void play(final String fileName) 
        {
     
            new Thread(new Runnable() { 
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
                        clip.open(inputStream);
                        clip.start(); 
                    } catch (Exception e) {
                        System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                    }
                }
            }).start();
        }
    }
    
} 