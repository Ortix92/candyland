import static org.junit.Assert.*;

import org.junit.Test;


public class Test1 {

	@Test
	public void test() {
	jbullet jbul = new jbullet(2);
	jbul.initMaze(new Maze());
	jbul.initObjects();
	jbul.initPlayer(35, 35, 35);
	}

	@Test
	public void testcollision() {
		jbullet jbul = new jbullet(2);
		NyanCat nyancat = new NyanCat(10, 2, 10, 0, null, null);
		jbul.initNyan(nyancat);
		jbul.CreateBullet(10f, 2, 10, 0, 0, null);
		
		//jbul.CollisionCheck();
		
		assertTrue(nyancat.getHP() == 50);
	}
}
