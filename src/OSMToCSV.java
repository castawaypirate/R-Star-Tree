

import info.pavie.basicosmparser.controller.*;
import java.io.File;

import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import org.xml.sax.SAXException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class OSMToCSV {

    private OSMParser parser = new OSMParser();
    private File OSMFile = null;

    private String OSMFilePath = null;

    public OSMToCSV(){

    }

    public void searchOSMFiles(String directoryPath) {
        File directory = new File(directoryPath);
        // Check if the provided directory path is valid
        if (!directory.isDirectory()) {
            System.out.println("invalid directory path: " + directoryPath);
            return;
        }
        // Get all files and subdirectories inside the directory
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursive call for subdirectories
                    searchOSMFiles(file.getAbsolutePath());
                } else {
                    // Check if the file has the desired extension
                    String fileName = file.getName();
                    if (fileName.endsWith(".osm")) {
                        OSMFilePath = file.getAbsolutePath();
                        System.out.println("found " + file.getName());
                        convert();
                    }
                }
            }
        }
    }

    public void convert() {
        String csvFilePath = null;
        FileWriter writer = null;
        try {
            OSMFile = new File(OSMFilePath);
            if (OSMFilePath.length() >= 4) {
                int startIndex = OSMFilePath.length() - 4;
                csvFilePath = OSMFilePath.substring(0, startIndex) + ".csv";
                File csvFile = new File(csvFilePath);
                if (csvFile.exists()) {
                    // csv file already exists
                    System.out.println(csvFile.getName() + " exists");
                } else {
                    // converting osm file to csv
                    System.out.println("converting " + OSMFile.getName() + " to " + csvFile.getName());
                    writer = new FileWriter(csvFilePath);
                    Map<String, Element> result = parser.parse(OSMFile);
                    Element currentElem = null;
                    for (String key : result.keySet()) {
                        currentElem = (Element) result.get(key);
                        if (currentElem instanceof Node) {
                            Node currentNode = (Node) currentElem;
//                            System.out.println(key + " : " + currentNode.getLat() + ";" + currentNode.getLon());
                            writer.append(key.substring(1) + "," + currentNode.getLat() + "," + currentNode.getLon() + "\n");
                        }
                    }
                    writer.flush();
                    writer.close();
                }

            } else {
                System.out.println("// invalid osm filename");
            }
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
