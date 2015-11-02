import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MakeKmlsFromFileList {
	public static void main(String[] args){
		int filePrefix = 0;
    if (args.length != 2) {
      System.out.println("Commandline argument must be a comma seperatet textfile followed");
      System.out.println("by the speed tresholdin this format <mmsi>,<name>..  Where the dots");
      System.out.println("can be any value. Only first 2 values in the file are used");
      System.out.println("speedTreshold is the speed limet where the dots change colour");
      return;
    }

    try {
      String file = args[0];
      float speedTreshold = Float.valueOf(args[1]);
      String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi = ? ORDER BY timestamp";
      String ship;
      String timestamp = "140408676099";
      FileInputStream fstream = new FileInputStream(file);
      DataInputStream fin = new DataInputStream(fstream);
      BufferedReader in = new BufferedReader(new InputStreamReader(fin));
      DB DB = new DB();
      //For each ship create kmlfiles
      while ((ship = in.readLine()) != null) {
        String[] newShip = ship.split(",");
        String mmsi = newShip[0];
        String name = newShip[1];
        DB.UpdateSQL(sql, mmsi, mmsi, mmsi);
        GenKml.makeKml(DB.rs, name, speedTreshold, 0);
      }
    } catch (Exception e) {
        System.out.println(e);
      }
 }
}
