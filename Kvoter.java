import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Fetch content from a saved html file
 */

public class Kvoter
{
  private ArrayList<String> list = new ArrayList<String>();
  private BufferedReader in;
  private String file;
  private String area;
    /**
     *Constructor for Kvoter wchich is used to parse data about kvoter
     *from a html file to the DB
     *@param file html file
     *@param area the name of the area
     */
    public Kvoter(String file, String area) {
      this.area = area;
      this.file = file;
    }

  public String[] getData() {
    int length = list.size();
    String[] returnArray = new String[length];
    for (int i = 0; i < length; i++) {
      returnArray[i] = list.get(i);
    }
    return returnArray;
  }
  public void fetchData() {
    try {
        System.out.printf("feching from file %s\n", file);
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream fin = new DataInputStream(fstream);
        in = new BufferedReader(new InputStreamReader(fin));
      String ch;
      String parsedData = "";
      String regEx = "<td class";
      String regEx2 = "data";
      String regStartLine = "-";
      while ((ch = in.readLine()) != null) {
        if (ch.contains(regEx) && ch.contains(regEx2)) {
          int indexEnd = ch.lastIndexOf("</td>");
          int  indexStart = ch.indexOf("data\">") + 6;
          if (ch.contains(regStartLine)) {
            if (!parsedData.equals("")){
              String[] lineSplit = parsedData.split("_");
              String regandName = lineSplit[0];
              String[] regName = regandName.split(" - ");
              String reg = regName[0].trim();
              String sName = regName[1];
              String pulje = lineSplit[1].trim();
              String andel = lineSplit[2].trim();
              String yearM = lineSplit[3].trim().replace(".","");
              String regM = lineSplit[4].trim().replace(".","");
              String area = this.area;
              list.add(reg + "_" + sName + "_" + pulje + "_" + andel + "_" + yearM + "_" + regM + "_" + area);
              parsedData = ch.substring(indexStart, indexEnd);

            }
            else {
              parsedData = ch.substring(indexStart, indexEnd);
            }
          }
          else {
            parsedData = parsedData + ("_" + ch.substring(indexStart, indexEnd));
          }
        }
      }
      for (int i = 0; i < list.size(); i++){
        System.out.println(list.get(i));
      }

      System.out.printf("Fetched %d lines", list.size());
      in.close();
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
