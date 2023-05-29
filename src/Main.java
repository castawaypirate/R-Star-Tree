public class Main {
    public static void main(String[] args) {
        args = new String[1];
        args[0]="2";
        System.out.println("Let's go!");
        if(args.length == 0){
            System.out.println("You need to give the number of dimensions as an argument my fellow companion");
        }else{
            System.out.println("Dimensions : " + args[0]);
            UserInterface ui = new UserInterface();
            ui.init(args[0]);
        }
    }
}