import info.pavie.basicosmparser.controller.*;
import info.pavie.basicosmparser.model.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OSMFileReader {
    private OSMParser parser = new OSMParser();
    private File OSMFile = null;

    private String OSMFilePath = null;

    public OSMFileReader(String OSMFilePath){
        this.OSMFilePath = OSMFilePath;
    }

    public void readOSMFile(){
        try {
            OSMFile = new File(OSMFilePath);
            Map<String, Element> result = parser.parse(OSMFile);
            Element currentElem = null;
            System.out.println(result.size());
//            int count = 0;
            for (String key : result.keySet()){
                currentElem = (Element)result.get(key);
                if (currentElem instanceof Node) {
                    Node currentNode = (Node) currentElem;
                    System.out.println(key + " : " + currentNode.getLat() + ";" + currentNode.getLon());
//                    count++;
                }
            }
//            System.out.println(count);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

}
