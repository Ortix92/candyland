import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import loader.Model;
import loader.ModelPart;
import loader.OBJLoader;

import com.sun.opengl.util.texture.Texture;

public class Monster extends GameObject implements VisibleObject {

	private Model monster;
	private IntBuffer vboPointer = IntBuffer.allocate(10);

	public Monster(double x, double y, double z) {
		super(x, y, z);

		try {
			String currentdir = System.getProperty("user.dir");
			String filename = currentdir + "\\src\\assets\\teapot.obj";
			monster = OBJLoader.loadTexturedModel(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createVBO(GL gl) {
		vboPointer = OBJLoader.createVBO(monster, gl);
	}

	@Override
	public void display(GL gl) {
		gl.glPushMatrix();

		// Draw nothing if the vboHandle are not loaded
		if (vboPointer.get(0) <= 0 || vboPointer.get(1) <= 0) {
			System.out.println("test");
		}

		else {
			// Reset the color to white
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			// Initialize counters
			int vertexSize = 0;
			int vertexCount = 0;

			// Initialize the texture
			Texture tempTexture = null;
			for (int i = 0; i < monster.getModelParts().size(); i++) {
				ModelPart p = monster.getModelParts().get(i);
				ModelPart.Face face = p.getFaces().get(0);

				// the modelPart has textureCoordinates so the material should
				// be used
				if (face.hasMaterial()) {
					gl.glMaterialfv(
							GL.GL_FRONT,
							GL.GL_DIFFUSE,
							new float[] { 1f,
									face.getMaterial().diffuseColour[0],
									face.getMaterial().diffuseColour[1],
									face.getMaterial().diffuseColour[2] }, 1);
					gl.glMaterialfv(
							GL.GL_FRONT,
							GL.GL_AMBIENT,
							new float[] { 1f,
									face.getMaterial().ambientColour[0],
									face.getMaterial().ambientColour[1],
									face.getMaterial().ambientColour[2] }, 1);
					gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS,
							face.getMaterial().specularCoefficient);
				}

				// Use default material
				else {
					gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {
							1.0f, 1.0f, 1.0f, 1.0f }, 1);
					gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {
							1.0f, 1.0f, 1.0f, 1.0f }, 1);
					gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120f);
				}

				// Enable the texture if the modelPart has a texture
				if (face.hasTexture()) {
					gl.glActiveTexture(GL.GL_TEXTURE0);
					gl.glEnable(GL.GL_TEXTURE_2D);
					tempTexture = face.getTexture();
					tempTexture.bind();
				}

				// Bind the vbo buffers for the normal and vertex arrays
				vertexSize = p.getFaces().size() * 3;
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120f);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboPointer.get(0));
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboPointer.get(1));
				gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);

				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboPointer.get(2));
				gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0L);

				// Enable the Array draw Mode for vertex,normals and textures
				gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
				gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);

				// Draw the arrays
				gl.glDrawArrays(GL.GL_TRIANGLES, vertexCount, vertexSize);

				// Enable the Array draw Mode for vertex,normals and textures
				gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

				// Keep track of the size of the modelParts. This
				vertexCount += vertexSize;

				// Disable the texture i
				if (face.hasTexture()) {
					tempTexture.disable();
					gl.glDisable(GL.GL_TEXTURE_2D);
				}
			}
		}


		gl.glPopMatrix();
	}

	@Override
	public void update(int deltaTime) {
		// TODO Auto-generated method stub

	}

}
