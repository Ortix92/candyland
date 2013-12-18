import javax.media.opengl.GL;

public class Floor implements VisibleObject {
	Maze maze;


	public Floor( Maze maze) {
		this.maze = maze;

	}

	@Override
	public void display(GL gl) {
		if (Textureloader.loadtextures) {
			if (!Textureloader.load) {
				Textureloader.load();
				Textureloader.load = true;
			}
			Textureloader.CandyFloor.enable();
			Textureloader.CandyFloor.bind();
		}
		for (int i = 0; i < maze.MAZE_SIZE; i++) {
			for (int j = 0; j < maze.MAZE_SIZE; j++) {
				gl.glPushMatrix();

				gl.glTranslated(i * maze.SQUARE_SIZE, 0,
						j * maze.SQUARE_SIZE );
				
				// Textureloader.Floor(gl, maze.SQUARE_SIZE );

				//gl.glTranslated(0, maze.SQUARE_SIZE / 2, 0);
				gl.glScaled(maze.SQUARE_SIZE,maze.SQUARE_SIZE,maze.SQUARE_SIZE);
				gl.glNormal3d(0, 1, 0);
				gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3d(0, 0, 0);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3d(0, 0, 1);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3d(1, 0, 1);
				gl.glTexCoord2f(0, 0);
				gl.glVertex3d(1, 0, 0);
				gl.glEnd();
				gl.glPopMatrix();
			}
		}
		Textureloader.CandyFloor.disable();
	}
}
