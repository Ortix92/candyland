package leveleditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class World {
	private String worldFile;
	private ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();

	public World(String absolutePath) {
		this.worldFile = absolutePath;
	}

	public void loadMapFromFile() {
		try {
			Scanner sc = new Scanner(new File(this.worldFile));
			sc.useDelimiter("\\s");

			ArrayList<Integer> row;
			int i = 0;
			while (sc.hasNextLine()) {
				row = new ArrayList<Integer>();
				int j = 0;
				while (sc.hasNextInt()) {

					row.add(sc.nextInt());
					System.out.print(" " + row.get(j));
					j++;
				}

				// Add row to array
				// Only if all vectors are same size (equal to first)
//				if (map.get(0).size() == map.get(i).size()) {
//				System.out.println(map.get(0).size());
					map.add(row);
//				} else {
//					// TODO Catch the exception
//					throw new IllegalArgumentException(
//							"Array not square! File incompatible");
//				}

				// Check if end of file
				if (sc.hasNext()) {
					System.out.println(sc.next());
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			//TODO afhandelen!
			e.printStackTrace();
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Array corrupt");

		}
	}

	public static void main(String[] args) {
		World world = new World("map.txt");
		world.loadMapFromFile();

	}

}
