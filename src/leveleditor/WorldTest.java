package leveleditor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This test class is intended to test the implementation of the World Class.
 * Here we are checking how well the importing of the map from a text file works.
 * 
 * @author Nick Tsutsunava
 */
import org.junit.Test;

public class WorldTest {

	@Test
	public void testWorld() {
		String filename = "map_export.txt";
		World world = new World(filename);
		assertEquals(world.getWorldFile(), filename);
	}

	@Test
	public void testGetZeroesMatrix() {
		World world = new World(null);
		ArrayList<ArrayList<Integer>> maze = new ArrayList<ArrayList<Integer>>();
		maze = world.getZeroesMatrix(10);

		// is it square?
		assertEquals(maze.size(), maze.get(0).size());

		for (int i = 0; i < maze.size(); i++) {
			for (int j = 0; j < maze.size(); j++) {
				assertEquals((int) maze.get(i).get(j), (int) 0);
			}
		}

	}

	// also tests spawn points
	@Test
	public void testLoadMapFromFile() {
		String filename = "map_export_test.txt";
		World world = new World(filename);

		try {
			world.loadMapFromFile();

			// did we get the spawn points correctly?
			assertEquals((int) world.getSpawnX(), (int) 11);
			assertEquals((int) world.getSpawnZ(), (int) 9);
			assertEquals(2, (int) world.getMap().get(0).get(0));

			// is it square?
			assertEquals(world.getMap().size(), world.getMap().get(0).size());

		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testConvertMapToArray() {
		String filename = "map_export_test.txt";
		World world = new World(filename);

		try {
			world.loadMapFromFile();
			int[][] mapArray = world.convertMapToArray();

			for (int i = 0; i < world.getMap().size(); i++) {
				for (int j = 0; j < world.getMap().size(); j++) {

					// The converted array does not contain the spawn point
					// Let's remove it here
					if (world.getMap().get(i).get(j) == 2) {
						world.getMap().get(i).set(j, 0);
					}
					assertEquals((int) mapArray[i][j], (int) world.getMap()
							.get(i).get(j));
				}
			}

		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

}
