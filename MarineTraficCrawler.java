
import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
//import java.sql.*;
import java.text.SimpleDateFormat;

public class MarineTraficCrawler {
  public static void main(String[] args) throws Exception {
    ArrayList<MarinetraficShip> shipsHtml = new ArrayList<MarinetraficShip>();
    ArrayList<MarinetraficShip> shipsUrl = new  ArrayList<MarinetraficShip>();
    ArrayList<MarinetraficShip> shipsUrlLogin = new  ArrayList<MarinetraficShip>();
    ArrayList<String> errors = new ArrayList<String>();

    //if wrong argument size
    if (args.length != 1) {
      System.out.println("wrong argument specification \n Use: MarineTraficCrawler.java file");
      System.out.println("where file is a comma seperatet text file in this format <mmsi>,<name>,typecode,<html file>,cookiefile");
      System.out.println("html and cookie files are optional. if not provided data will be fetched from Marinetrafic.com. typecode is the ais code specifying ship type");
      System.out.println("If cookie file is used the name of the given html file is not used. but some random tekst need to be there as the way to tell if it should use login is to count the number of arguments");
    }


    try {
      //get the file containing list of ships
      String file = args[0];
      String ship;
      FileInputStream fstream = new FileInputStream(args[0]);
      DataInputStream fin = new DataInputStream(fstream);
      BufferedReader in = new BufferedReader(new InputStreamReader(fin));
      DB DB = new DB();
      //For each ship create a MarinetraficShip object and place it in the apporpriate array list 
      // for either html or url source
      while ((ship = in.readLine()) != null) {
        String[] newShip = ship.split(",");
        System.out.println("line has so many arguments" + newShip.length);
        if (newShip.length == 5) {
          shipsUrlLogin.add(new MarinetraficShip(newShip[0], newShip[1], newShip[2], newShip[3], newShip[4]));
        }
        if (newShip.length == 4) {
          shipsHtml.add(new MarinetraficShip(newShip[0], newShip[1], newShip[2], newShip[3]));
        }
        if (newShip.length == 3) {
          shipsUrl.add(new MarinetraficShip(newShip[0], newShip[1], newShip[2]));
        }
        if (newShip.length != 3 && newShip.length != 4 && newShip.length != 5) {
          errors.add(ship);
        }
        System.out.printf("There was %d errors, when fetching ship ½s from file", errors.size(), newShip[1]);
        System.out.printf("  Url ships = %d      htmlShips = %d\n", shipsUrl.size(), shipsHtml.size());

      }

      //fetch data for each ship with html source using login cookie
      for (int i = 0; i < shipsUrlLogin.size(); i++) {
        shipsUrlLogin.get(i).fetchData();
        DB.upload(shipsUrlLogin.get(i).getData());
      }
      //fetch data for each ship with html source
      for (int i = 0; i < shipsHtml.size(); i++) {
        shipsHtml.get(i).fetchData();
        DB.upload(shipsHtml.get(i).getData());
      }

      //fetch data for each ship in url source
      for (int i = 0; i < shipsUrl.size(); i++) {
        shipsUrl.get(i).fetchData();
        DB.upload(shipsUrl.get(i).getData());
      }
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }


  }
}
