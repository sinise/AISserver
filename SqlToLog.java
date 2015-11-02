import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.*;
import java.util.Date;
public class SqlToLog {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost/shipplotter";
        String user = "shipplotter";
        String password = "shipplotter";

        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT mmsi,timestamp,status,type,lat,lon,speed,course,heading FROM shipplotter WHERE type=33");
            rs = pst.executeQuery();
            String mmsi;
            int timestamp;
            int status;
            int type;
            String lat;
            String lon;
            String speed;
            String course;
            String heading;
            int count = 0;
            DateFormat df = new SimpleDateFormat("yyMMdd HHmmss");

            while (rs.next()) {
                mmsi = rs.getString(1);
                timestamp = rs.getInt(2);
                status = rs.getInt(3);
                type = rs.getInt(4);
                lat = rs.getString(5);
                lon = rs.getString(6);
                speed = rs.getString(7);
                course = rs.getString(8);
                heading = rs.getString(9);
                timestamp = timestamp * 1000;
                while (mmsi.length() < 9)
                {
                  mmsi = "0" + mmsi;
                }
                while (lat.length() < 9)
                {
                  lat = lat + "0";
                } 
                while (lon.length() < 9)
                {
                  lon = lon + "0";
                } 
		if (!speed.contains("."))
                  speed = speed + ".0";
		while (speed.length() < 4)
                {
                  speed = " " +  speed;
                }

                Date time = new Date(timestamp);
                String ftime = df.format(time);
                if (mmsi.length() < 9)
                  count++;
                System.out.println(mmsi + ";" + "under way " + ";" + "000�'" + ";" + speed + "kt" + ";" + lat + "N" + ";" + lon +
                                   "E" + ";" + course + "�" + ";" + heading + "�" + ";" + "49s" + ";" + ftime + ";" + "audio[1]");
            }

        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(SqlToLog.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(SqlToLog.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
