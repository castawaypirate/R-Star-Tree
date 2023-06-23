import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private FileManager fileManager;
    private final static String resourcesDirectoryPath = ".\\resources\\";
    Scanner sc = new Scanner(System.in);
    private RAsteriskTree tree;
    public UserInterface(){
    }

    public void init(String dimensions){
        startUp(dimensions);
    }

    private void startUp(String dimensions){
        fileManager = new FileManager(Integer.parseInt(dimensions));
        tree = new RAsteriskTree(Integer.parseInt(dimensions), fileManager);
        if (fileManager.datafileExists() || fileManager.indexfileExists()) {
            System.out.println("datafile and indexfile found\n" +
                    "keep it: y\n" +
                    "delete and create new: n");

            char choice = sc.nextLine().charAt(0);
//            char choice = 'y';
            if (choice=='y'){

            }else if (choice=='n'){
//                fileManager.deleteDatafile();
//                fileManager.deleteIndexfile();
                OSMToCSV oc = new OSMToCSV();
                oc.searchOSMFiles(resourcesDirectoryPath);
                List<File> csvFiles = fileManager.getCSVFiles(resourcesDirectoryPath);
                if (!csvFiles.isEmpty()) {
                    for (int i=0;i<csvFiles.size();i++) {
                        System.out.println(csvFiles.get(i).getName() + ":" + i);
                    }
                    System.out.print("bulk loading from file:");
                    int index = sc.nextInt();
                    tree.bulkLoading(csvFiles.get(index).getAbsolutePath());
                }
            }else {
                System.out.println("invalid input");
                System.exit(0);
            }
        }
        menu();
    }

    private void menu(){
        char choice;
        do {
            System.out.println("type 1 to insert");
            System.out.println("type 3 for range query");
            System.out.println("type 5 for skyline query");
            System.out.println("type 0 to exit");
            choice = sc.nextLine().charAt(0);
            System.out.println();
//            choice = '1';
            switch (choice) {
                case '0':
                    System.out.println("that's all folks!");
                    break;
                case '1':
                    // insert data
                    Record record = readRecordFromUser();
                    tree.insertData(record);
                    break;
                case '3':
                    // range query
                    BoundingBox searchBox = readRangeQueryFromUser();
                    System.out.println("search result:");
                    tree.search(searchBox, tree.getRoot());
                    break;
                case '5':
                    // skyline query
                    System.out.println("skyline result:");
                    tree.branchAndBoundSkyline();
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

    public BoundingBox readRangeQueryFromUser(){
        System.out.println("Upper right point of S");
        ArrayList<Double> upperRight = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            upperRight.add(value);
        }
        sc.nextLine();
        System.out.println("Down left point of S");
        ArrayList<Double> downLeft = new ArrayList<>();
        for(int i=1;i<=tree.getDimensions();i++){
            System.out.print("Dimension " + i + ":");
            double value = sc.nextDouble();
            downLeft.add(value);
        }
        sc.nextLine();
        System.out.println();
        ArrayList<Bounds> searchBoxBounds = new ArrayList<>();
        for(int i=0;i< tree.getDimensions();i++){
            searchBoxBounds.add(new Bounds(upperRight.get(i), downLeft.get(i)));
        }
        return new BoundingBox(searchBoxBounds);
    }
}
