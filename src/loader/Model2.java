package loader;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
public class Model2 {
	public ArrayList<Vector3d> vertices = new ArrayList<Vector3d>();
	public ArrayList<Vector3d> normals = new ArrayList<Vector3d>();
	public ArrayList<Vector2d> textureCoordinates = new ArrayList<Vector2d>();
	public ArrayList<Face> faces = new ArrayList<Face>();

	public Model2() {
	}

	public void display(GL gl) {
//		float pferdkleur[] = { 0.5f, 0.0f, 0.7f, 1.0f }; 
//		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, pferdkleur, 0);

		gl.glBegin(GL.GL_TRIANGLES);
		for (Face face : this.getFaces()) {
			System.out.println(this.getNormals());
			Vector3d n1 = this.getNormals().get(face.getNormalIndices()[0] - 1);
			gl.glNormal3d(n1.x, n1.y, n1.z);
			if (face.hasTextureCoordinates()) {
				Vector2d t1 = this.getTextureCoordinates().get(
						face.getTextureCoordinateIndices()[0] - 1);
				gl.glTexCoord2d(t1.x, t1.y);
			}
			Vector3d v1 = this.getVertices()
					.get(face.getVertexIndices()[0] - 1);
			gl.glVertex3d(v1.x, v1.y, v1.z);
			Vector3d n2 = this.getNormals().get(face.getNormalIndices()[1] - 1);
			gl.glNormal3d(n2.x, n2.y, n2.z);
			if (face.hasTextureCoordinates()) {
				Vector2d t2 = this.getTextureCoordinates().get(
						face.getTextureCoordinateIndices()[1] - 1);
				gl.glTexCoord2d(t2.x, t2.y);
			}
			Vector3d v2 = this.getVertices()
					.get(face.getVertexIndices()[1] - 1);
			gl.glVertex3d(v2.x, v2.y, v2.z);
			Vector3d n3 = this.getNormals().get(face.getNormalIndices()[2] - 1);
			gl.glNormal3d(n3.x, n3.y, n3.z);
			if (face.hasTextureCoordinates()) {
				Vector2d t3 = this.getTextureCoordinates().get(
						face.getTextureCoordinateIndices()[2] - 1);
				gl.glTexCoord2d(t3.x, t3.y);
			}
			Vector3d v3 = this.getVertices()
					.get(face.getVertexIndices()[2] - 1);
			gl.glVertex3d(v3.x, v3.y, v3.z);
		}
		gl.glEnd();
	}

	public boolean hasNormals() {
		return getNormals().size() > 0;
	}

	public boolean hasTextureCoordinates() {
		return getTextureCoordinates().size() > 0;
	}

	public ArrayList<Face> getFaces() {
		return faces;
	}

	public ArrayList<Vector3d> getNormals() {
		return normals;
	}

	public ArrayList<Vector3d> getVertices() {
		return vertices;
	}

	public ArrayList<Vector2d> getTextureCoordinates() {
		return textureCoordinates;
	}

	public static class Face {

		private final int[] vertexIndices = { -1, -1, -1 };
		private final int[] normalIndices = { -1, -1, -1 };
		private final int[] textureCoordinateIndices = { -1, -1, -1 };

		public boolean hasNormals() {
			return normalIndices[0] != -1;
		}

		public boolean hasTextureCoordinates() {
			return textureCoordinateIndices[0] != -1;
		}

		public int[] getVertexIndices() {
			return vertexIndices;
		}

		public int[] getTextureCoordinateIndices() {
			return textureCoordinateIndices;
		}

		public int[] getNormalIndices() {
			return normalIndices;
		}

		public Face(int[] vertexIndices) {
			this.vertexIndices[0] = vertexIndices[0];
			this.vertexIndices[1] = vertexIndices[1];
			this.vertexIndices[2] = vertexIndices[2];
		}

		public Face(int[] vertexIndices, int[] normalIndices) {
			this.vertexIndices[0] = vertexIndices[0];
			this.vertexIndices[1] = vertexIndices[1];
			this.vertexIndices[2] = vertexIndices[2];
			this.normalIndices[0] = normalIndices[0];
			this.normalIndices[1] = normalIndices[1];
			this.normalIndices[2] = normalIndices[2];
		}

		public Face(int[] vertexIndices, int[] normalIndices,
				int[] textureCoordinateIndices) {
			this.vertexIndices[0] = vertexIndices[0];
			this.vertexIndices[1] = vertexIndices[1];
			this.vertexIndices[2] = vertexIndices[2];
			this.normalIndices[0] = normalIndices[0];
			this.normalIndices[1] = normalIndices[1];
			this.normalIndices[2] = normalIndices[2];
			this.textureCoordinateIndices[0] = textureCoordinateIndices[0];
			this.textureCoordinateIndices[1] = textureCoordinateIndices[1];
			this.textureCoordinateIndices[2] = textureCoordinateIndices[2];
		}
	}
}
