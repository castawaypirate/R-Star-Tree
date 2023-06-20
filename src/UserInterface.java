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
            System.out.println("type 0 to exit");
            choice = sc.nextLine().charAt(0);
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
                default:
                    System.out.println("try again");
                    break;
            }
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

        return new Record(id, name, coordinates);
    }
}
