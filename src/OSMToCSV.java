

import info.pavie.basicosmparser.controller.*;
import java.io.File;

import info.pavie.basicosmparser.controller.*;
import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class OSMToCSV {

    private OSMParser parser = new OSMParser();
    private File OSMFile = null;

    private String OSMFilePath = null;

    public OSMToCSV(){

    }

    public void searchFiles(String directoryPath) {
        File directory = new File(directoryPath);

        // Check if the provided directory path is valid
        if (!directory.isDirectory()) {
            System.out.println("Invalid directory path: " + directoryPath);
            return;
        }

        // Get all files and subdirectories inside the directory
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursive call for subdirectories
                    searchFiles(file.getAbsolutePath());
                } else {
                    // Check if the file has the desired extension
                    String fileName = file.getName();
                    if (fileName.endsWith(".osm")) {
                        OSMFilePath = file.getAbsolutePath();
                        readOSMFile();
                    }
                }
            }
        }
    }

    public void readOSMFile() {
        String csvFilePath = null;
        FileWriter writer = null;
        try {
            OSMFile = new File(OSMFilePath);
            if (OSMFilePath.length() >= 4) {
                int startIndex = OSMFilePath.length() - 4;
                csvFilePath = OSMFilePath.substring(0, startIndex) + ".csv";
                File file = new File(csvFilePath);
                if (file.exists()) {
                    System.out.println("csv exists");
                } else {
                    System.out.println("csv doesn't exists");
                }
                writer = new FileWriter(csvFilePath);
            } else {
                System.out.println("// String is too short, no change needed.");
            }

            Map<String, Element> result = parser.parse(OSMFile);
            Element currentElem = null;
    //            int count = 0;
            for (String key : result.keySet()) {
                currentElem = (Element) result.get(key);
                if (currentElem instanceof Node) {
                    Node currentNode = (Node) currentElem;
//                    System.out.println(key + " : " + currentNode.getLat() + ";" + currentNode.getLon());
                    writer.append(key + ", " + currentNode.getLat() + ", " + currentNode.getLon() + "\n");
    //                    count++;
                }
            }

            writer.flush();
            writer.close();

        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }


}
