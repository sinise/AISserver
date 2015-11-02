import java.io.*;
import java.util.ArrayList;
import java.sql.*;


/*
 * Upload kvoter to database
 */
public class UploadkvoterToDB
{
  public void upload(String[] line) throws Exception
  {
    PreparedStatement preparedStatement = null;
    Connection connection = null;

    try
      {
        Class.forName("com.mysql.jdbc.Driver");
      }
      catch (ClassNotFoundException e) {
        System.out.println("MySQL JDBC Driver not found !!");
        return;
      }

    System.out.println("MySQL JDBC Driver Registered!");
    int count = 0;
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost/shipplotter", "shipplotter", "shipplotter");
      for (int i = 0; i < line.length; i++)
      {
	      String[] la = line[i].split("_");
              count++;
                System.out.println(line[i]);
	      preparedStatement = connection.prepareStatement("INSERT IGNORE INTO northsea values (?, ?, ?, ? , ?, ?, ?)");
	      preparedStatement.setString(1, la[0]);
	      preparedStatement.setString(2, la[1]);
	      preparedStatement.setString(3, la[2]);
	      preparedStatement.setString(4, la[3]);
	      preparedStatement.setString(5, la[4]);
	      preparedStatement.setString(6, la[5]);
	      preparedStatement.setString(7, la[6]);
	      preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
    System.out.println("Connection Failed! Check output console");
    System.out.println(e);
    return;
    } finally {
      try {
        if(connection != null)
        connection.close();
        System.out.println("Connection closed !!");
	    }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    System.out.println("");

  }
}
