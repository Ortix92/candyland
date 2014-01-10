package leveleditor;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.FileNotFoundException;

import org.junit.Test;

/**
 * Test class for the Painter class. Just to clarify, this class does not test
 * the World class. In other words, we are not testing how well the World class
 * imports the files
 * 
 * @author Nick Tsutsunava
 * 
 */

public class PainterTest {

	@Test
	public void testSetResolution() {
		Painter painter = new Painter(800, 600);
		try {
			painter.setResolution(10);
			assertEquals(painter.getResolution(), 10);
		} catch (IllegalArgumentException e) {
			fail("Resolution not divisible by 5");
		}

		try {
			painter.setResolution(7);
			assertNotEquals(painter.getResolution(), 7);
		} catch (IllegalArgumentException e) {

			assertTrue("not divisible by 5", true);
		}
	}

	@Test
	public void testSet_and_GetMaze() {
		Painter painter = new Painter(800, 600);
		World world = new World("map_export_test.txt");
		try {
			world.loadMapFromFile();
			painter.setMaze(world.getMap());

			assertEquals(painter.getResolution(), 20);
			assertEquals(painter.getMaze().size(), 20);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetSpawnPoint() {
		Painter painter = new Painter(800, 600);
		World world = new World("map_export_test.txt");
		try {
			world.loadMapFromFile();

			assertEquals((int) world.getSpawnX(), 11);
			assertEquals((int) world.getSpawnZ(), 9);

			painter.setSpawnPoint(world.getSpawnX(), world.getSpawnZ());
			Point spawn = new Point(11, 9);
			assertTrue(painter.getSpawnPoint().equals(spawn));
			spawn.x = 10;
			assertFalse(painter.getSpawnPoint().equals(spawn));

		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testFillEdges() {
		Painter painter = new Painter(800, 600);
		World world = new World("map_export_test.txt");
		World world2 = new World("map_export_test_fill_edges.txt");
		try {
			world.loadMapFromFile();
			world2.loadMapFromFile();

			painter.setMaze(world.getMap());
			painter.setDrawMapListener(true);
			painter.fillEdges();

			assertTrue(painter.getMaze().equals(world2.getMap()));

		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

}
