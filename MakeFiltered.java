import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MakeFiltered {
  public int id;
  public String name;
  public String dest;
  public float lat;
  public float lng;
  public String type;
  public long timestamp;

  public static void main(String[] args){
    if (args.length != 3) {
      System.out.println("\nCommandline argument must be a file with ships followed by the speedTreshold and the time i ms. of the fall out\n");
      System.out.println("    use java MakeFilered <ships-file> <speed> <fallout in s>");
      System.out.println("    Ex. java MakeFilered ships.csv 4.5 86400\n");
      return;
    }
    try {
    String file = args[0];
    float speedTreshold = Float.valueOf(args[1]);
    int fallOut = Integer.parseInt(args[2]);
    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and timestamp < ? ORDER BY timestamp";

    //only export positions up to this timestamp. set it high if you want all positions
    String timestamp = "1404086760999";
    String ship;
    FileInputStream fstream = new FileInputStream(file);
    DataInputStream fin = new DataInputStream(fstream);
    BufferedReader in = new BufferedReader(new InputStreamReader(fin));

      //For each ship create kmlfiles
    while ((ship = in.readLine()) != null) {
      int filePrefix = 0;
      String[] newShip = ship.split(",");
      String mmsi = newShip[0];
      String name = newShip[1];
      Filter myFilter = new Filter(sql, mmsi, timestamp, fallOut);
      System.out.println("createt filter");

      for(int i = 0; i < myFilter.results.size(); i++) {

        String filname = name + "-" + mmsi + "-" + filePrefix;
        GenKml.makeKml(myFilter.results.get(i), (myFilter.resultsTimeSpan.get(i)/100) + "-" + filname, speedTreshold, myFilter.resultsTimeFallout.get(i));


        filePrefix++;

      }
    }
    } catch (Exception e) {
        System.out.println(e);
      }
  }
}
