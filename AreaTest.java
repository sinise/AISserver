import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class AreaTest {
    ArrayList<Polygon2D> legal = new ArrayList<Polygon2D>();
    ArrayList<Polygon2D> illegal = new ArrayList<Polygon2D>();


  /**
   *  Constructor
   */
  public AreaTest(){
    try {

      //get the arreas
      String sql = "SELECT * FROM areas WHERE id > ? and id > ? and id > ? ORDER BY id";
      DB DB = new DB();
      String next;
      DB.UpdateSQL(sql, "0", "0", "0");

      while(DB.rs.next()){
        int typeCode = DB.rs.getInt("typeCode");
        String[] points =  DB.rs.getString("area").split(",");
        int nPoints = points.length;
        float[] xCords = new float[nPoints];
        float[] yCords = new float[nPoints];

        for(int i = 0; i < nPoints; i++){
          String[] point = points[i].split(" ");
          xCords[i] = Float.parseFloat(point[0]);
          yCords[i] = Float.parseFloat(point[1]) ;
        }
        if(typeCode == 0){
          legal.add(new Polygon2D(xCords, yCords, nPoints));
        }
        if(typeCode == 1){
          illegal.add(new Polygon2D(xCords, yCords, nPoints));
        }
//        System.out.println(points);
      }
//      nPoints = points.length;
    } catch (Exception e) {
        System.out.println(e);
      }
 }

  /*
   * 
   */
  public boolean test(double xPoint, double yPoint){
    boolean rState = false;
    for(int i = 0; i < legal.size(); i++){
      if((legal.get(i).contains(xPoint, yPoint))){
        rState = true;
      }
    }
    for(int i = 0; i < illegal.size(); i++){
      if(illegal.get(i).contains(xPoint, yPoint)){
        rState = false;
      }
    }

    return rState;
  }
}
