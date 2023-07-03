import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //main
        args = new String[1];
        args[0]="2";
        System.out.println("welcome");
        if(args.length == 0){
            System.out.println("you need to give the number of dimensions as an argument my fellow companion");
        }else{
            System.out.println("dimensions: " + args[0]);
            UserInterface ui = new UserInterface();
            ui.init(args[0]);
        }
        //
    }
}