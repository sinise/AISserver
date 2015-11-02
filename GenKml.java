import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

//import com.google.marker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenKml {
	public int id;
	public String name;
	public String dest;
	public float lat;
	public float lng;
	public String type;
	public long timestamp;

 /* Generate a kml file to use in google earth
  * @rs the resultset containing the points
  * @filname output filename
  * @SpeedTreshold speed higer than this has another color
  * @timeFallout for how long time the fallOut was.
  */
	public static void makeKml(ResultSet rs, String filename, float speedTreshold, int timeFallout){
        int filePrefix = 0;

		GenKMLPlaceMarker KML = new GenKMLPlaceMarker();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String formatedTimeFallout = format.format(timeFallout * 1000);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			TransformerFactory tranFactory = TransformerFactory.newInstance();
      Transformer aTransformer = tranFactory.newTransformer();

			Document doc = builder.newDocument();
			Element root = doc.createElement("kml");
			root.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
			doc.appendChild(root);
			Element dnode = doc.createElement("Document");
			root.appendChild(dnode);

			Element rstyle = doc.createElement("Style");
			rstyle.setAttribute("id", "restaurantStyleLow");
			Element ristyle = doc.createElement("IconStyle");
			ristyle.setAttribute("id", "restaurantIcon");
			Element ricon = doc.createElement("Icon");
			Element riconhref = doc.createElement("href");
	  		riconhref.appendChild(doc.createTextNode("http://earth.google.com/images/kml-icons/track-directional/track-none.png"));
			rstyle.appendChild(ristyle);
			ricon.appendChild(riconhref);
			ristyle.appendChild(ricon);
			dnode.appendChild(rstyle);

			Element rhstyle = doc.createElement("Style");
			rhstyle.setAttribute("id", "restaurantStyleHigh");
			Element rhistyle = doc.createElement("IconStyle");
			rhistyle.setAttribute("id", "restaurantIcon");
			Element rhicon = doc.createElement("Icon");
			Element rhiconhref = doc.createElement("href");
			rhiconhref.appendChild(doc.createTextNode("http://earth.google.com/images/kml-icons/track-directional/track-none.png"));
			rhstyle.appendChild(rhistyle);
			rhicon.appendChild(rhiconhref);
			rhistyle.appendChild(rhicon);
			dnode.appendChild(rhstyle);


			Element bstyle = doc.createElement("Style");
			bstyle.setAttribute("id", "barStyle");
			Element bistyle = doc.createElement("IconStyle");
			bistyle.setAttribute("id", "barIcon");
			Element bicon = doc.createElement("Icon");
			Element biconhref = doc.createElement("href");
			biconhref.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pal2/icon27.png"));
			bstyle.appendChild(bistyle);
			bicon.appendChild(biconhref);
			bistyle.appendChild(bicon);
			dnode.appendChild(bstyle);

			int count = 0;
			while(rs.next()){
				count++;
				KML.timestamp = rs.getLong("timestamp");
				KML.id = rs.getInt("mmsi");
				KML.name = rs.getString("name");
				KML.dest = rs.getString("dest");
				KML.lat = rs.getFloat("lat");
				KML.lng = rs.getFloat("lon");
				float speed = rs.getFloat("speed");
				KML.type = rs.getString("type");

			        String speedValue;
			        //set the style from speedTreshold
			        if(speed < speedTreshold){
        	  			speedValue = "Low";
        			}
        			else{
	          			speedValue = "High";
        			}
				String formatedTime = format.format(KML.timestamp * 1000);

				Element placemark = doc.createElement("Placemark");
				dnode.appendChild(placemark);


				Element descrip = doc.createElement("description");
				descrip.appendChild(doc.createTextNode("s " + speed + "  " + KML.name + "  time " + formatedTime));
				placemark.appendChild(descrip);

				Element styleUrl = doc.createElement("styleUrl");
				styleUrl.appendChild(doc.createTextNode( "#restaurantStyle" + speedValue));
				placemark.appendChild(styleUrl);

				Element TimeStamp = doc.createElement("TimeStamp");
				Element when = doc.createElement("when");
				when.appendChild(doc.createTextNode(formatedTime + ""));
				TimeStamp.appendChild(when);
				placemark.appendChild(TimeStamp);

				Element point = doc.createElement("Point");
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(KML.lng+ "," + KML.lat));
				point.appendChild(coordinates);
				placemark.appendChild(point);
				if(count % 10000 == 0) {
					System.out.printf("lines %d\n", count);

					Source src = new DOMSource(doc);

					Result dest = new StreamResult(new File(filename + "--" + formatedTimeFallout + "--" +filePrefix + ".kml"));
					filePrefix++;
					aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
					aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					aTransformer.transform(src, dest);

					doc = builder.newDocument();
					root = doc.createElement("kml");
					root.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
					doc.appendChild(root);
					dnode = doc.createElement("Document");
					root.appendChild(dnode);



					Element rxstyle = doc.createElement("Style");
					rxstyle.setAttribute("id", "restaurantStyleLow");
					Element rxistyle = doc.createElement("IconStyle");
					rxistyle.setAttribute("id", "restaurantIcon");
					Element rxicon = doc.createElement("Icon");
					Element rxiconhref = doc.createElement("href");
					rxiconhref.appendChild(doc.createTextNode("http://momentos.dk/track-none-yellow.png"));
					rxstyle.appendChild(rxistyle);
					rxicon.appendChild(rxiconhref);
					rxistyle.appendChild(rxicon);
					dnode.appendChild(rxstyle);

			    		Element rhxstyle = doc.createElement("Style");
			    		rhxstyle.setAttribute("id", "restaurantStyleHigh");
			    		Element rhxistyle = doc.createElement("IconStyle");
			    		rhxistyle.setAttribute("id", "restaurantIcon");
			    		Element rhxicon = doc.createElement("Icon");
			    		Element rhxiconhref = doc.createElement("href");
			    		rhxiconhref.appendChild(doc.createTextNode("http://earth.google.com/images/kml-icons/track-directional/track-none.png"));
			    		rhxstyle.appendChild(rhxistyle);
			    		rhxicon.appendChild(rhxiconhref);
			    		rhxistyle.appendChild(rhxicon);
			    		dnode.appendChild(rhxstyle);


				}
			}
			Source src = new DOMSource(doc);
			Result dest = new StreamResult(new File(filename + "--" + formatedTimeFallout + "--" +filePrefix + ".kml"));
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			aTransformer.transform(src, dest);
			System.out.println("Completed.....");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
