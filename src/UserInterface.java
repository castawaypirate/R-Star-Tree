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
        RAsteriskTree tree = new RAsteriskTree();
        Scanner sc = new Scanner(System.in);
        System.out.println("Type 1 for Bulk Loading");
        System.out.println("Type 2 to insert a record");
        System.out.println("Type 0 to exit");
        char c = '1';
//        char c = sc.nextLine().charAt(0);
//        while(c != '0' && c != '1' && c != '2'){
//            System.out.println(c + " is an invalid command! Try again!");
//            c = sc.next().charAt(0);
//        }
        if(c == '0'){
            System.out.println("That's all folks!");
        }else if(c == '1'){
//            System.out.println("Type the path of the file that contains the data");
//            String filePath = sc.nextLine();
//            tree.bulkLoading(filePath);
            tree.bulkLoading(".\\resources\\data.csv");
        }else if(c == '2'){

        }
    }
}
