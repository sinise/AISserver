
import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;

/**
 * Inserts a polygons in the DB from a list of kml files.
 */
public class KMLtoDB {
  public static void main(String[] args) throws Exception {
    ArrayList<String> errors = new ArrayList<String>();
    ArrayList<KmlToDBHandler> kmlFiles = new ArrayList<KmlToDBHandler>();

    //if wrong argument size
    if (args.length != 1) {
      System.out.println("wrong argument specification \n Use: KMLtoDB.java file");
      System.out.println("where file is a list of kml files containing polygons");
    }

    try {
      //get the file containing list of ships
      String file = args[0];
      FileInputStream fstream = new FileInputStream(args[0]);
      DataInputStream fin = new DataInputStream(fstream);
      BufferedReader in = new BufferedReader(new InputStreamReader(fin));
      DB DB = new DB();
      String currentKmlFile;
      //For each kml file create a KmlToDBHandler object and place it in KmlToDBHandler arraylist
      int count = 0;
      while ((currentKmlFile = in.readLine()) != null) {
          kmlFiles.add(new KmlToDBHandler(currentKmlFile));
          count++;
        }
        System.out.printf("%d kmlfiles added\n", count);

//    fetch data for each file and upload to DB
      for (int i = 0; i < kmlFiles.size(); i++) {
        kmlFiles.get(i).fetchData();
        DB.uploadArea(kmlFiles.get(i).getData());
      }
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
