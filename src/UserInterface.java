import java.util.Scanner;

public class UserInterface {
    private String dimensions = null;
    public UserInterface(String dimensions){
        this.dimensions = dimensions;
    }

    public void init(){
        startUp();
    }

    private void startUp(){
        System.out.println("Hello there!");
        Scanner sc = new Scanner(System.in);
        System.out.println("Type 1 for Bulk Loading");
        System.out.println("Type 0 to exit");
        char c = sc.nextLine().charAt(0);
        while(c != '0' && c != '1'){
            System.out.println(c + " is an invalid command! Try again!");
            c = sc.next().charAt(0);
        }
        if(c == '0'){
            System.out.println("That's all folks!");
        }else if(c == '1'){
            System.out.println("Type the path of the osm file!");
//            OSMFileReader r = new OSMFileReader(".\\resources\\map.osm");
            String filePath = sc.nextLine();
            OSMFileReader r = new OSMFileReader(filePath);
            r.readOSMFile();

        }
    }
}
