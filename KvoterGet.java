
import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
//import java.sql.*;
import java.text.SimpleDateFormat;

public class KvoterGet {
  public static void main(String[] args) throws Exception {
    ArrayList<MarinetraficShip> shipsHtml = new ArrayList<MarinetraficShip>();
    ArrayList<MarinetraficShip> shipsUrl = new  ArrayList<MarinetraficShip>();
    ArrayList<String> errors = new ArrayList<String>();

    //if wrong argument size
    if (args.length != 2) {
      System.out.println("wrong argument specification \n Use: MarineTraficCrawler.java file");
      System.out.println("where file is a comma seperatet text file in this format <mmsi>,<name>,<html file>");
      System.out.println("html file is optional. if not provided data will be fetched from Marinetrafic.com");
    }


    try {
      String file = args[0];
      String area = args[1];
      UploadkvoterToDB DB = new UploadkvoterToDB();
      System.out.println("createt db instance");

      Kvoter htmlFile = new Kvoter(file, area);
      System.out.println("createt html instance");


      //fetch data
        htmlFile.fetchData();
        System.out.println("data fetched");

        DB.upload(htmlFile.getData());
      System.out.println("data uploaded");

    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }


  }
}
