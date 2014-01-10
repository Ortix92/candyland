package leveleditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class World {
	private String worldFile;
	private ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
	private int[][] mapArray;
	private double spawnZ;
	private double spawnX;

	public World(String absolutePath) {
		this.worldFile = absolutePath;
	}
	
	public String getWorldFile() {
		return this.worldFile;
	}

	public ArrayList<ArrayList<Integer>> getZeroesMatrix(int resolution) {

		ArrayList<ArrayList<Integer>> maze = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < resolution; i++) {
			ArrayList<Integer> row = new ArrayList<Integer>(
					Collections.nCopies(resolution, 0));
			maze.add(row);
		}

		return maze;
	}

	public void loadMapFromFile() throws FileNotFoundException {
		try {
			Scanner sc = new Scanner(new File(this.worldFile));
			sc.useDelimiter("\\s");

			// retrieve spawnpoints
			spawnX = Integer.parseInt(sc.next());
			spawnZ = Integer.parseInt(sc.next());
			sc.nextLine();

			ArrayList<Integer> row;
			while (sc.hasNextLine()) {

				String line = sc.nextLine();

				row = new ArrayList<Integer>();
				int j = 0;
				Scanner lineScanner = new Scanner(line);
				while (lineScanner.hasNextInt()) {

					row.add(lineScanner.nextInt());
					System.out.print(" " + row.get(j));
					j++;
				}
				lineScanner.close();
				map.add(row);
				System.out.println("");
			}

			// int i = 0;
			// while (sc.hasNextLine()) {
			// row = new ArrayList<Integer>();
			// int j = 0;
			// while (sc.hasNextInt()) {
			//
			// row.add(sc.nextInt());
			// System.out.print(" " + row.get(j));
			// j++;
			// }
			// map.add(row);
			// // Check if end of file
			// if (sc.hasNext()) {
			// System.out.println(sc.next());
			// }
			// i++;
			// }
			System.out.println("");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Array corrupt");
		}
	}

	public int[][] convertMapToArray() {

		mapArray = new int[map.size()][map.size()];
		// System.out.println(map.size());
		// System.out.println(map);
		for (int i = 0; i < map.size(); i++) {
			for (int j = 0; j < map.size(); j++) {
				// spawn point is just an empty block
				if (map.get(i).get(j) == 2) {
					mapArray[i][j] = 0;
				} else {
					mapArray[i][j] = map.get(i).get(j);
				}
			}
		}
		return mapArray;
	}

	public ArrayList<ArrayList<Integer>> getMap() {
		return map;
	}

	public double getSpawnZ() {
		return spawnZ;
	}

	public double getSpawnX() {
		return spawnX;
	}

//	public static void main(String[] args) {
//		World world = new World("map_export.txt");
//		try {
//			world.loadMapFromFile();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//	}

}
