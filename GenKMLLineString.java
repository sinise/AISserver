import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenKMLLineString
{
  public float lat;
  public float lng;
  public static void main(String[] args)
  {
    Statement stmt;
    ResultSet rs;
    GenKMLLineString KML = new GenKMLLineString();
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://localhost/shipplotter";
      Connection con = DriverManager.getConnection(url, "shipplotter", "shipplotter");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      TransformerFactory tranFactory = TransformerFactory.newInstance();
      Transformer aTransformer = tranFactory.newTransformer();
      Document doc = builder.newDocument();
      Element root = doc.createElement("kml");
      root.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
      doc.appendChild(root);
      Element fnode = doc.createElement("Folder");
      root.appendChild(fnode);
      Element placemark = doc.createElement("Placemark");
      fnode.appendChild(placemark);
      placemark.setAttribute("id", "linestring1");
      Element name = doc.createElement("name");
      name.appendChild(doc.createTextNode("My Path"));
      placemark.appendChild(name);
      Element desc = doc.createElement("description");
      desc.appendChild(doc.createTextNode("This is the path that" +
      " I took through my favorite restaurants in Seattle"));
      placemark.appendChild(desc);
      Element ls = doc.createElement("LineString");
      placemark.appendChild(ls);
      Element extr = doc.createElement("extrude");
      extr.appendChild(doc.createTextNode("1"));
      ls.appendChild(extr);
      Element am = doc.createElement("altitudeMode");
      am.appendChild(doc.createTextNode("relativeToGround"));
      ls.appendChild(am);
      Element cord = doc.createElement("coordinates");
      stmt = con.createStatement();
      ls.appendChild(cord);
      rs = stmt.executeQuery("SELECT * FROM shipplotter WHERE type=33");
      while(rs.next())
      {
        KML.lat = rs.getFloat("lat");
        KML.lng = rs.getFloat("lon");
        cord.appendChild(doc.createTextNode(KML.lng + "," + KML.lat+",100 "));
      }
      Source src = new DOMSource(doc);
      Result dest = new StreamResult(new File("LineString.kml"));
      aTransformer.transform(src, dest);
      System.out.println("Completed.....");
    } catch (Exception e)
        {
        System.out.println(e.getMessage());
        }
  }
}
