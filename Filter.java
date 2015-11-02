import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.String;
public class Filter {

  public long timestamp;
  public ArrayList<ResultSet> results = new ArrayList<ResultSet>();
  public ArrayList<Integer> resultsTimeSpan = new ArrayList<Integer>();
  public ArrayList<Integer> resultsTimeFallout = new ArrayList<Integer>();

//  public ArrayList<Integer> resultsDiff = new ArrayList<Integer>();
  /**
  * Update filter reultset rs object with sql statement and filtered values
  * @sql the sqlstring as prepared statement with 3 values
  * @mmsi the mmsi value
  * @timestamp get positions no later than the given timestamp.
  * @fallOut min fallOut time for the trip to be included in the results 
  */
	public Filter(String sql, String mmsi, String timestamp, int fallOut) {
    try {
      DB DB = new DB();
      DB.UpdateSQL(sql, mmsi, mmsi, timestamp);
      int count = 0;
      int lastTime = -1;

      //while there is more positions
      while(DB.rs.next()){

        int diff = DB.rs.getInt("timestamp") - lastTime;

        //if there is a fall out then log the route consisting of 
        // points before and after the fall out
        if ((diff > fallOut) && (lastTime > 0)) {
          // we only consider it a fall out if the fall out happend
          // in our area of interest
          if (validPosition(DB.rs.getFloat("lat"), DB.rs.getFloat("lon"))) {
            DB thisDB = new DB();
            thisDB.UpdateSQL("SELECT * FROM shipplotter WHERE mmsi = ? and timestamp > ? and timestamp < ? ORDER BY timestamp",
                              mmsi, Long.toString(lastTime - 18000), "" + (lastTime + 18000));
            results.add(thisDB.rs);
            resultsTimeSpan.add(diff);
            resultsTimeFallout.add(lastTime);
            System.out.printf("added one resultset. resultets = %d\n", results.size());
          }
        }
        lastTime = DB.rs.getInt("timestamp");
      }
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  //Check if position is within the area of interest
  public boolean validPosition(float lat, float lon) {
    //check if position is valid for area kategat
    if(lat > 55.71 && lat < 56.09 && lon < 12.68 & lon > 12.51) {
      return true;
    }
    //Check if position is valid for area Køge bugt
    if(lat > 55.4 && lat < 55.54 && lon < 12.69 & lon > 12.21) {
      return true;
    }
    else {
      System.out.println("Position outside area, skiping this file");

      return false;}

  }
}
