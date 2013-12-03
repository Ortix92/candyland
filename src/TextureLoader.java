import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class TextureLoader {
	static Texture PoptartTexture; // remembers the texture of the poptart.
	static Texture Nyanface; // remembers the texture of the face
	static Texture Rainbow; // remembers the texture of the rainbow
	static Texture purple;
	static Texture red;
	static Texture SkyBox1; // reserve memory for skybox
	static Texture SkyBox2; // reserve memory for skybox
	static Texture SkyBox3; // reserve memory for skybox
	static Texture SkyBox4; // reserve memory for skybox
	static Texture SkyBox5; // reserve memory for skybox
	static Texture SkyBox6; // reserve memory for skybox
	static Texture CandyWall;
	static Texture CandyFloor;
	static boolean load = false; // boolean so that images are only loaded once

	public static void load() {
		// Loads in all images and makes textures of them.
		try {

			// Skybox
			BufferedImage skyboxImage1 = ImageIO.read(new File(
					"src/skybox_02.png"));
			SkyBox1 = TextureIO.newTexture(skyboxImage1, false);
			SkyBox1.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox1.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
		      SkyBox1.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox1.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 

			BufferedImage skyboxImage2 = ImageIO.read(new File(
					"src/skybox_05.png"));
			SkyBox2 = TextureIO.newTexture(skyboxImage2, false);
			SkyBox2.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox2.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
			SkyBox2.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox2.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 

			BufferedImage skyboxImage3 = ImageIO.read(new File(
					"src/skybox_07.png"));
			SkyBox3 = TextureIO.newTexture(skyboxImage3, false);
			SkyBox3.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox3.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
			SkyBox3.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox3.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 

			BufferedImage skyboxImage4 = ImageIO.read(new File(
					"src/skybox_09.png"));
			SkyBox4 = TextureIO.newTexture(skyboxImage4, false);
			SkyBox4.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox4.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
			SkyBox4.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox4.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 

			BufferedImage skyboxImage5 = ImageIO.read(new File(
					"src/skybox_04.png"));
			SkyBox5 = TextureIO.newTexture(skyboxImage5, false);
			SkyBox5.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox5.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
			SkyBox5.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox5.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 
			BufferedImage skyboxImage6 = ImageIO.read(new File(
					"src/skybox_06.png"));
			SkyBox6 = TextureIO.newTexture(skyboxImage6, false);
			SkyBox6.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			SkyBox6.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// make edges less visible:
			SkyBox6.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		      SkyBox6.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE); 
		      
		      BufferedImage CandyWallImage=ImageIO.read(new File("src/CandyWall.png"));
		      CandyWall=TextureIO.newTexture(CandyWallImage,true);
				CandyWall.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
				CandyWall.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		      
				BufferedImage CandyFloorImage=ImageIO.read(new File("src/CandyFloor3.png"));
				CandyFloor=TextureIO.newTexture(CandyFloorImage,false);
				CandyFloor.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
				CandyFloor.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		      

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Trolololol");
			// Catch block. Yay.
		}
	}

	public static void Floor(GL gl, double size){
		if (!load) {
			load();
			load = true;
		}
		
		CandyFloor.enable();
		CandyFloor.bind();
	    gl.glNormal3d(0, 1, 0);
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(1,0);
		        gl.glVertex3d(0, 0, 0);
		        gl.glTexCoord2f(1,1);
		        gl.glVertex3d(0, 0, size);
		        gl.glTexCoord2f(0,1);
		        gl.glVertex3d(size, 0, size);
		        gl.glTexCoord2f(0,0);
		        gl.glVertex3d(size, 0, 0);		
			gl.glEnd();	
		CandyFloor.disable();
	}
	
	public static void Wall(GL gl){
		if (!load) {
			load();
			load = true;
		}
		CandyWall.enable();
		CandyWall.bind();
		drawCube(gl,false);
		CandyWall.disable();
	}
	
	
	public static void SkyBox(GL gl) {

		if (!load) {
			load();
			load = true;
		}
		gl.glDepthMask(false);
		// draws a cube with texture coordinates.
		// Set size:
		gl.glScaled(100, 100, 100);
		// Square 1:
		SkyBox5.enable();
		SkyBox5.bind();
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 1, 0);

		gl.glEnd();
		SkyBox5.disable();

		// Square 2:
		SkyBox3.enable();
		SkyBox3.bind();
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 1, 1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 0, 1);

		gl.glEnd();
		SkyBox3.disable();
		// Square 3 BOVENKANT:
		SkyBox1.enable();
		SkyBox1.bind();

		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 1, 1);

		gl.glEnd();

		SkyBox1.disable();

		// Square 4:
		SkyBox2.enable();
		SkyBox2.bind();
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 1);

		gl.glEnd();
		SkyBox2.disable();

		// Square 5 ONDERKANT:
		SkyBox4.enable();
		SkyBox4.bind();
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 0, 1);

		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 1);

		gl.glEnd();
		SkyBox4.disable();

		// Square 6:
		SkyBox6.enable();
		SkyBox6.bind();
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 1, 1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 0, 1);

		gl.glEnd();
		SkyBox6.disable();

		gl.glScaled(1 / 100, 1 / 100, 1 / 100);
		gl.glDepthMask(true);

	}

	public static void head(GL gl, GLUT glut, float size, int roundness) {
		// makes the head.
		if (!load) { // checks if loading is needed
			load(); // loads textures
			load = true; // next time loading is not needed anymore.
		}
		Nyanface.enable(); // enable the texture so that it can be used.
		Nyanface.bind(); // bind texture to the following object
		GLU glu = new GLU(); // needed to make a sphere with texture coordinates
		GLUquadric head = glu.gluNewQuadric(); // makes a quadric object
		glu.gluQuadricTexture(head, true); // Makes the quadric object head
											// capable of having textures.
		glu.gluSphere(head, 0.5 * size, roundness, roundness); // make the
																// object into a
																// sphere
		Nyanface.disable(); // disable texture so that it will not be put on
							// every next object.

	}

	public static void poptart(GL gl, float size) {
		// Makes a poptart.
		if (!load) { // checks if loading is needed
			load(); // loads textures
			load = true; // next time loading is not needed anymore.
		}

		PoptartTexture.enable(); // enable texture so that it can be used.
		PoptartTexture.bind(); // bind texture to following object.
		gl.glTranslated(-size / 2, -size / 2, -size / 2); // put the poptart in
															// the right
															// position
		gl.glScaled(size, size, size); // make the rib lengths: size.
		drawCube(gl, false); // make a cube with ribs length 1 and with texture
								// coords.
		gl.glScaled(1 / size, 1 / size, 1 / size); // unscale so that following
													// objects will not be
													// scaled.
		gl.glTranslated(size / 2, size / 2, size / 2); // translate back.
		PoptartTexture.disable(); // disable texture so that next objects won't
									// have it.
	}

	public static void Rainbow(GL gl, double size) {
		if (!load) {
			load();
			load = true;
		}

		// set de size van de rainbowblokjes:
		gl.glScaled(size, size, size);

		// Plak rainbow texture op de zijden:
		Rainbow.enable();
		Rainbow.bind();
		// gl.glScaled(2,2,1);
		gl.glTranslated(-size / 2, -size / 2, -size / 2); // put the rainbowcube
															// in the right
															// position
		drawCube(gl, true);

		Rainbow.disable();

		// Plak paarse texture op de onderkant:
		purple.enable();
		purple.bind();

		// Square 5:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 0, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glEnd();
		purple.disable();

		// plak rode texture op de bovenkant:
		red.enable();
		red.bind();
		// Square 3:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 1, 1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 1, 0);
		gl.glEnd();
		red.disable();
		gl.glScaled(1 / size, 1 / size, 1 / size); // unscale so that following
													// objects will not be
													// scaled.

		gl.glTranslated(size / 2, size / 2, size / 2); // translate back.
		// gl.glScaled(1/2,1/2,1);
	}

	public static void drawCube(GL gl, boolean rainbow) {
		// draws a cube with texture coordinates.
		// The length of the ribs are 1.
		// Square 1:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glEnd();

		// Square 2:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(0, 0, 1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(0, 1, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glEnd();

		if (!rainbow) { // Omdat rainbow blokjes geen boven en onderkant moeten
						// hebben met dezelfde textuur.
			// Square 3:
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(1, 0);
			gl.glVertex3f(0, 1, 1);
			gl.glTexCoord2f(1, 1);
			gl.glVertex3f(1, 1, 1);
			gl.glTexCoord2f(0, 1);
			gl.glVertex3f(1, 1, 0);
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(0, 1, 0);
			gl.glEnd();
		}

		// Square 4:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 0);
		gl.glEnd();

		if (!rainbow) {
			// Omdat rainbow blokjes geen boven en onderkant moeten hebben met
			// dezelfde textuur.
			// Square 5:
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(1, 0);
			gl.glVertex3f(1, 0, 1);
			gl.glTexCoord2f(1, 1);
			gl.glVertex3f(0, 0, 1);
			gl.glTexCoord2f(0, 1);
			gl.glVertex3f(0, 0, 0);
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(1, 0, 0);
			gl.glEnd();
		}

		// Square 6:
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(0, 0, 1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(0, 1, 1);
		gl.glEnd();

	}

}