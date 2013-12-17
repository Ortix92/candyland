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
    dbConnect("jdbc:mysql://sql.ewi.tudelft.nl:3306/candyland", "candyland",
             "snoepjes123");
}
   public void dbConnect(String db_connect_string,
            String db_userid,
            String db_password)
   {
      try {

		Class.forName("com.mysql.jdbc.Driver");
         connect = DriverManager.getConnection(db_connect_string,
                  db_userid, 
                  db_password);
      } catch (Exception e) {
          e.printStackTrace();
       }
   }
   
   public void AddHighScore(String naam, int score) {
	   
         Statement statement;
		try {
			statement = connect.createStatement();
         statement.executeUpdate("INSERT INTO  `candyland`.`Score` (`Naam` ,`Score`)VALUES ('" + naam + "',  '" + score + "');");
   
   } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
   
   public void GetHighScores(GL gl) {
	 try {
		 Statement statement = connect.createStatement();
         ResultSet rs = statement.executeQuery("SELECT Naam, Score FROM Score");
         float i = 0.9f;
         while (rs.next()) {
            display(gl, rs.getString(1),rs.getString(2), i);
            i = i - 0.1f;
         }
	 } catch (SQLException e) {         
     e.printStackTrace();
	 }
   }
   
   public void display(GL gl, String a, String b, float num) {
	 GLUT glut = new GLUT();
	   gl.glColor3f(1.0f,1.0f,1.0f);
		gl.glRasterPos2f(600/2f, 600-num*600);
		int len, i;
		String string = a + "  " + b; 
		  len = (int)string.length();
		  for (i = 0; i < len; i++) {
		    glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24,string.charAt(i));
		  }
   }
   
   public void close() {
	   try {
		connect.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
}