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

public class Textureloader {
	static Texture PoptartTexture; // remembers the texture of the poptart.
	static Texture Nyanface; // remembersd the texture of the face
	static Texture Rainbow; // remembers the texture of the rainbow
	static Texture purple;
	static Texture red;
	static boolean load = false; // boolean so that images are only loaded once

	public static void load() {
		// Loads in all images and makes textures of them.
		try {
			// Poptart:

			BufferedImage image = ImageIO
					.read(new File("src/nyan zijkant.jpg")); // image loading
			PoptartTexture = TextureIO.newTexture(image, false); // making
																	// texture
																	// of it
			// Give texture parameters: (Not sure what the filters do)
			PoptartTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_LINEAR);
			PoptartTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_LINEAR);
			// Put the poptart over the whole cuboid by wrapping repeating it:
			// PoptartTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S,
			// GL.GL_REPEAT);
			// PoptartTexture.setTexParameteri( GL.GL_TEXTURE_WRAP_T,
			// GL.GL_REPEAT);

			// Nyans face:
			BufferedImage image2 = ImageIO.read(new File(
					"src/nyanfaceopzij.jpg")); // image loading
			Nyanface = TextureIO.newTexture(image2, false); // making texture of
															// it
			// give filter parameters:
			Nyanface.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			Nyanface.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

			// RainbowTrail
			BufferedImage image3 = ImageIO.read(new File(
					"src/Rainbow segment.jpg"));
			Rainbow = TextureIO.newTexture(image3, false);
			Rainbow.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			Rainbow.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// Rainbow.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			// Rainbow.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

			// Purple
			BufferedImage image4 = ImageIO.read(new File("src/Purple.jpg"));
			purple = TextureIO.newTexture(image4, false);
			purple.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			purple.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

			// Red
			BufferedImage image6 = ImageIO.read(new File("src/red.jpg"));
			red = TextureIO.newTexture(image6, false);
			red.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			red.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Trolololol");
			// Catch block. Yay.
		}
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
