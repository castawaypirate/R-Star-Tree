import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private FileManager fileManager;
    private final static String resourcesDirectoryPath = ".\\resources\\";
    Scanner sc = new Scanner(System.in);
    private RAsteriskTree tree;
    public UserInterface(){}

    public void init(String dimensions){
        startUp(dimensions);
    }

    private void startUp(String dimensions){
        fileManager = new FileManager(Integer.parseInt(dimensions));
        if (fileManager.datafileExists() && fileManager.indexfileExists()) {
            System.out.println("datafile and indexfile found\n" +
                    "keep: y\n" +
                    "delete: n");
            char choice = sc.nextLine().charAt(0);
            if (choice=='y'){

            }else if (choice=='n'){
                fileManager.deleteDatafile();
                fileManager.deleteIndexfile();
            }else {
                System.out.println("invalid input");
                System.exit(0);
            }
        }
        menu();
    }

    private void menu(){
        tree = new RAsteriskTree(fileManager.getDimensions(), fileManager);
        long startTime;
        long endTime;
        long executionTime;
        char choice;
        do {
            System.out.println("type 1 to insert a record");
            System.out.println("type 2 to delete a record");
            System.out.println("type 3 to perform a range query");
            System.out.println("type 4 to find the k nearest neighbors");
            System.out.println("type 5 to perform a skyline query");
            System.out.println("type 6 to perform bulk loading");
            System.out.println("type 7 to see tree");
            System.out.println("type 0 to exit");
            choice = sc.nextLine().charAt(0);
            System.out.println();
            switch (choice) {
                case '0':
                    System.out.println("terminate program");
                    break;
                case '1':
                    // insert record
                    Record record = readRecordFromUser();
                    tree.insertData(record);
                    break;
                case '2':
                    // delete record
                    LeafEntry leafEntry = readRecordCoordinatesFromUser();
                    if(tree.delete(leafEntry)) {
                        System.out.println("record was deleted successfully");
                    }
                    break;
                case '3':
                    // range query
                    BoundingBox searchBox = readRangeQueryFromUser();
                    System.out.println("search result:");
                    startTime = System.currentTimeMillis();
                    tree.search(searchBox, tree.getRoot());
                    endTime = System.currentTimeMillis();
                    executionTime = endTime - startTime;
                    System.out.println("Execution time: " + executionTime + " milliseconds");
                    break;
                case '4':
                    // k nearest neighbors
                    Point point = readCoordinatesOfPointFromUser();
                    System.out.print("k:");
                    int k = sc.nextInt();
                    sc.nextLine();
                    startTime = System.currentTimeMillis();
                    tree.knnQuery(tree.getRoot(), point, k, null);
                    endTime = System.currentTimeMillis();
                    executionTime = endTime - startTime;
                    System.out.println("Execution time: " + executionTime + " milliseconds");
                    break;
                case '5':
                    // skyline query
                    System.out.println("skyline result:");
                    startTime = System.currentTimeMillis();
                    tree.branchAndBoundSkyline();
                    endTime = System.currentTimeMillis();
                    executionTime = endTime - startTime;
                    System.out.println("Execution time: " + executionTime + " milliseconds");
                    break;
                case '6':
                    if (fileManager.datafileExists()) {
                        System.out.println("datafile and indexfile must be deleted before performing bulk loading");
                    } else {
                        OSMToCSV oc = new OSMToCSV();
                        oc.searchOSMFiles(resourcesDirectoryPath);
                        List<File> csvFiles = fileManager.getCSVFiles(resourcesDirectoryPath);
                        if (!csvFiles.isEmpty()) {
                            for (int i=0;i<csvFiles.size();i++) {
                                System.out.println(csvFiles.get(i).getName() + ": " + i);
                            }
                            int index;
                            do {
                                System.out.print("bulk loading from file: ");
                                index = sc.nextInt();
                                sc.nextLine();
                            } while (index < 0 || index >= csvFiles.size());
                            startTime = System.currentTimeMillis();
                            tree.bulkLoading(csvFiles.get(index).getAbsolutePath());
                            endTime = System.currentTimeMillis();
                            executionTime = endTime - startTime;
                            System.out.println("bulk loading done");
                            System.out.println("Execution time: " + executionTime + " milliseconds");
                        }
                    }
                    break;
                case '7':
                    if(tree.getRoot()!=null) {
                        tree.showTree();
                    }
                    break;
                case '8':
                    if (fileManager.datafileExists()) {
                        System.out.println("datafile and indexfile must be deleted before performing one by one build");
                    } else {
                        OSMToCSV oc = new OSMToCSV();
                        oc.searchOSMFiles(resourcesDirectoryPath);
                        List<File> csvFiles = fileManager.getCSVFiles(resourcesDirectoryPath);
                        if (!csvFiles.isEmpty()) {
                            for (int i=0;i<csvFiles.size();i++) {
                                System.out.println(csvFiles.get(i).getName() + ": " + i);
                            }
                            int index;
                            do {
                                System.out.print("one by one build from file: ");
                                index = sc.nextInt();
                                sc.nextLine();
                            } while (index < 0 || index >= csvFiles.size());
                            startTime = System.currentTimeMillis();
                            tree.oneByOneTreeBuild(csvFiles.get(index).getAbsolutePath());
                            endTime = System.currentTimeMillis();
                            executionTime = endTime - startTime;
                            System.out.println("one by one build done");
                            System.out.println("Execution time: " + executionTime + " milliseconds");
                        }
                    }
                    break;
                default:
                    System.out.println("try again");
                    break;
            }
            System.out.println();
        } while (choice != '0');
    }

    public Record readRecordFromUser(){
        System.out.print("ID:");
        long id = sc.nextLong();
        sc.nextLine();
        System.out.print("Name:");
        String name = sc.nextLine();
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            coordinates.add(value);
        }
        sc.nextLine();
        System.out.println();
        return new Record(id, name, coordinates);
    }

    public LeafEntry readRecordCoordinatesFromUser(){
        System.out.println("Type the coordinates of the record you want to delete");
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            coordinates.add(value);
        }
        sc.nextLine();
        return new LeafEntry(0, new BoundingBox(new Point(coordinates), new Point(coordinates)));
    }

    public BoundingBox readRangeQueryFromUser(){
        System.out.println("Lower left point of S");
        ArrayList<Double> lowerLeft = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            lowerLeft.add(value);
        }
        sc.nextLine();

        System.out.println("Upper right point of S");
        ArrayList<Double> upperRight = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            upperRight.add(value);
        }
        sc.nextLine();
        System.out.println();
        return new BoundingBox(new Point(lowerLeft), new Point(upperRight));
    }

    public Point readCoordinatesOfPointFromUser(){
        System.out.println("Type the coordinates of the point");
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            coordinates.add(value);
        }
        sc.nextLine();
        return new Point(coordinates);
    }
}
