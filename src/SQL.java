import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class SQL {

	private Connection connect;

	public SQL() {
		dbConnect("jdbc:mysql://sql.ewi.tudelft.nl:3306/candyland",
				"candyland", "snoepjes123");
	}

	public void dbConnect(String db_connect_string, String db_userid,
			String db_password) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(db_connect_string, db_userid,
					db_password);
		} catch (Exception e) {
			System.out.println("Failed to connect to the database with following error: ");
			System.out.println(e.getMessage());
		}
	}

	public void AddHighScore(String naam, int score) {

		Statement statement;
		try {
			statement = connect.createStatement();
			statement
					.executeUpdate("INSERT INTO  `candyland`.`Score` (`Naam` ,`Score`)VALUES ('"
							+ naam + "',  '" + score + "');");

		} catch (SQLException e) {
			System.out.println("Failed to insert into the database");
		}
	}

	public void GetHighScores(GL gl) {
		try {
			Statement statement = connect.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT Naam, Score FROM Score ORDER BY Score DESC");
			int plaats = 1;
			while (rs.next()) {
				display(gl, rs.getString(1), rs.getString(2), plaats);
				plaats = plaats + 1;
			}
		} catch (SQLException e) {
			System.out.println("Failed to get Highscores from DB");
		}
	}

	public void display(GL gl, String a, String b, int num) {
		GLUT glut = new GLUT();
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glRasterPos2f(1024 / 2f - 100, 50 + num * 26);
		int len, i;
		String string = num + ": " + a + "  " + b;
		len = (int) string.length();
		for (i = 0; i < len; i++) {
			glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24,
					string.charAt(i));
		}
	}

	public void wipe() {
		try {
			Statement statement = connect.createStatement();
			statement.executeQuery("TRUNCATE TABLE  `Score`");
		} catch (SQLException e) {
			System.out.println("MY SCORES REMAIN!");
		}
	}

	public void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			System.out.println("Failed to close the connection");
		}
	}
	/**
	 * EASY WAY TO WIPE ALL DATA FROM THE SQL SERVERS, WILL WIPE EVERYTHING,
	 * CHANGE QUERY TO NOT WIPE
	 * 
	 * @param args
	 */
	// public static void main(String[] args) {
	// SQL connection = new SQL();
	// connection.wipe();
	// }

}
