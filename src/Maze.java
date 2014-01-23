import java.io.FileNotFoundException;

import javax.media.opengl.GL;

import leveleditor.World;

import com.sun.opengl.util.GLUT;

/**
 * Maze represents the maze used by MazeRunner.
 * <p>
 * The Maze is defined as an array of integers, where 0 equals nothing and 1
 * equals a wall. Note that the array is square and that MAZE_SIZE contains the
 * exact length of one side. This is to perform various checks to ensure that
 * there will be no ArrayOutOfBounds exceptions and to perform the calculations
 * needed by not only the display(GL) function, but also by functions in the
 * MazeRunner class itself.<br />
 * Therefore it is of the utmost importance that MAZE_SIZE is correct.
 * <p>
 * SQUARE_SIZE is used by both MazeRunner and Maze itself for calculations of
 * the display(GL) method and other functions. The larger this value, the larger
 * the world of MazeRunner will be.
 * <p>
 * This class implements VisibleObject to force the developer to implement the
 * display(GL) method, so the Maze can be displayed.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 * 
 */
public class Maze implements VisibleObject {

	public double PLAYER_SPAWN_X = 100;
	public double PLAYER_SPAWN_Y = 3;
	public double PLAYER_SPAWN_Z = 100;
	public double MAZE_SIZE = 10;
	public double SQUARE_SIZE = 5;
	public static String mazeFile = "map_export.txt";

	private int[][] maze;

	public Maze() {
		World world = new World(Maze.mazeFile);
		try {  
			world.loadMapFromFile();
			MAZE_SIZE = world.getMap().size();
			System.out.println(MAZE_SIZE);
			maze = world.convertMapToArray();
			PLAYER_SPAWN_X = world.getSpawnX()*SQUARE_SIZE;
			PLAYER_SPAWN_Z = world.getSpawnZ()*SQUARE_SIZE;
			MazeRunner.amountofNyans = world.getAmountOfNyans();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Falling back to default map due to error");
			int[][] intMaze = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
					{ 1, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
					{ 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
					{ 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
					{ 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
					{ 1, 0, 0, 0, 1, 0, 1, 0, 0, 1 },
					{ 1, 0, 0, 0, 1, 1, 1, 0, 0, 1 },
					{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
			maze = intMaze;
		}
	}

	/**
	 * isWall(int x, int z) checks for a wall.
	 * <p>
	 * It returns whether maze[x][z] contains a 1.
	 * 
	 * @param x
	 *            the x-coordinate of the location to check
	 * @param z
	 *            the z-coordinate of the location to check
	 * @return whether there is a wall at maze[x][z]
	 */
	public boolean isWall(int x, int z) {
		if (x >= 0 && x < MAZE_SIZE && z >= 0 && z < MAZE_SIZE)
			return maze[x][z] == 1;
		else
			return false;
	}

	/**
	 * isWall(double x, double z) checks for a wall by converting the double
	 * values to integer coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the
	 * grid defined by maze[][]. Then it calls upon isWall(int, int) to check
	 * for a wall.
	 * 
	 * @param x
	 *            the x-coordinate of the location to check
	 * @param z
	 *            the z-coordinate of the location to check
	 * @return whether there is a wall at maze[x][z]
	 */
	public boolean isWall(double x, double z) {
		int gX = convertToGridX(x);
		int gZ = convertToGridZ(z);
		return isWall(gX, gZ);
	}

	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * 
	 * @param x
	 *            the double x-coordinate
	 * @return the integer x-coordinate
	 */
	private int convertToGridX(double x) {
		return (int) Math.floor(x / SQUARE_SIZE);
	}

	/**
	 * Converts the double z-coordinate to its correspondent integer coordinate.
	 * 
	 * @param z
	 *            the double z-coordinate
	 * @return the integer z-coordinate
	 */
	private int convertToGridZ(double z) {
		return (int) Math.floor(z / SQUARE_SIZE);
	}

	public void display(GL gl) {

	}


	public void update(int deltaTime) {
	}
	
	public void changeMaze(double x, double z, double oldx, double oldz) {
		int gX = convertToGridX(x);
		int gZ = convertToGridZ(z);
		maze[gX][gZ] = 1 - maze[gX][gZ];
		int oX = convertToGridX(oldx);
		int oZ = convertToGridZ(oldz);
		maze[oX][oZ] = 1 - maze[oX][oZ];
	}
	
//	public String toString() {
//		String res = "";
//		for (int i=0; i < MAZE_SIZE; i++) {
//			 res = res + "\n";
//			for (int j = 0; j < MAZE_SIZE; j++) {
//				res = res + maze[i][j] + " ";
//			}
//		}
//	return res;
//	}

	
}
