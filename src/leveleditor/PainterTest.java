package leveleditor;

import static org.junit.Assert.*;

import org.junit.Test;

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
	public void testSetMaze() {
		World world = new World("map_export");
		Painter painter = new Painter(800,600);
		painter.setMaze(world.getMap());
	}

	@Test
	public void testGetMaze() {
	}

	@Test
	public void testGetSpawnPoint() {
	}

	@Test
	public void testFillEdges() {
	}

	@Test
	public void testClearMap() {
	}

	@Test
	public void testSetDrawMapListener() {
	}

	@Test
	public void testSetDrawGridListener() {
	}

}
