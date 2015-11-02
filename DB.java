import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
public class DB
{
//  public Statement stmt;
  public ResultSet rs;
  Connection con;

  /**
   * Constructor for DB
   */
  public DB() {
  }

  /**
   * Update DB this DB object with sql statement
   * @param sql the sql statement as prepared statement whit 1 ?. Must start with SELECT
   * @param value the value of ?
   */
  public void UpdateSQL(String sql, String value1, String value2, String value3)
  {
    PreparedStatement preparedStatement = null;
    rs = null;
    Connection con = null;

    try {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/shipplotter";
        con = DriverManager.getConnection(url, "shipplotter", "shipplotter");
    }
    catch (Exception e) {
      System.out.println("Connection Failed! Check output console" + e);
      return;
    }
    System.out.println("MySQL JDBC Driver Registered!");

    try{
      System.out.printf("trying sql statement %s \n", sql);
       String selectSql = sql;
      preparedStatement = con.prepareStatement(selectSql);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);

//      preparedStatement.setInt(0, 1001);
      rs = preparedStatement.executeQuery();
    }
    catch (Exception e){
      System.out.println("failed to execute statement");
      System.err.println(e.getMessage());
    }
  }

  public void close() {
    try {
        if(con != null)
        con.close();
        System.out.println("Connection closed !!");
	    }
      catch (SQLException e) {
        e.printStackTrace();
      }
    System.out.println("");

  }

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
      for (int i = 49; i < line.length; i++)
      {
	      String[] la = line[i].split(",");
              count++;
	      if ((count % 10) == 0) {

              }
	      preparedStatement = connection.prepareStatement("INSERT IGNORE INTO shipplotter values (?, ?, ?, ? , ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)");
	      preparedStatement.setString(1, la[0]);
	      preparedStatement.setString(2, la[1]);
	      preparedStatement.setString(3, la[2]);
	      preparedStatement.setString(4, la[3]);
	      preparedStatement.setString(5, la[4]);
	      preparedStatement.setString(6, la[5]);
	      preparedStatement.setString(7, la[6]);
	      preparedStatement.setString(8, la[7]);
	      preparedStatement.setString(9, la[8]);
	      preparedStatement.setString(10, la[9]);
	      preparedStatement.setString(11, la[10]);
	      preparedStatement.setString(12, la[11]);
	      preparedStatement.setString(13, la[12]);
	      preparedStatement.setString(14, la[13]);
	      preparedStatement.setString(15, la[14]);
	      preparedStatement.setString(16, la[15]);
	      preparedStatement.setString(17, la[16]);
	      preparedStatement.setString(18, la[17]);
	      preparedStatement.setString(19, la[18]);
	      preparedStatement.setString(20, la[19]);
	      preparedStatement.setString(21, la[20]);
	      preparedStatement.setString(22, la[21]);
	      preparedStatement.executeUpdate();
      }
                System.out.println(line[0]);
                System.out.println(line[50]);
                System.out.println(line[1]);
                System.out.println(line[51]);

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

  public void uploadArea(String[] line) throws Exception
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
	      String[] la = line[i].split(":");
        count++;
        String[] poly = la[2].split(",0 ");
        String polyStatement = "";

        for(int j = 0; j < poly.length; j++){
          polyStatement = polyStatement + poly[j].trim().replace(",", " ");
          if(j + 1 < poly.length){
            polyStatement = polyStatement + ",";
          }
        }
          System.out.println(la[0].replace("\"", " "));
          System.out.println(la[1].replace("\"", " "));

          System.out.println("print statement");
          System.out.println(polyStatement);


	      preparedStatement = connection.prepareStatement("INSERT IGNORE INTO areas (type, name, area) values (?, ?, ?)");
	      preparedStatement.setString(1, la[0].replace("\"", " "));
	      preparedStatement.setString(2, la[1].replace("\"", " "));
	      preparedStatement.setString(3, polyStatement);
	      preparedStatement.executeUpdate();

        Scanner in2 = new Scanner(System.in);
        System.out.println("Enter for next");
        String s = in2.nextLine();
          System.out.println("");
          System.out.println("");
 
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
