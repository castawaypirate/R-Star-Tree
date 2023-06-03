import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Bounds bounds1 = new Bounds(0.0,0.0);
        Bounds bounds2 = new Bounds(0.0,0.0);
        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
        arrbounds1.add(bounds1);
        arrbounds1.add(bounds2);
        BoundingBox box1 = new BoundingBox(arrbounds1);
        Entry entry1 = new Entry(box1);

        Bounds bounds3 = new Bounds(2.0,2.0);
        Bounds bounds4 = new Bounds(2.0,2.0);
        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
        arrbounds2.add(bounds3);
        arrbounds2.add(bounds4);
        BoundingBox box2 = new BoundingBox(arrbounds2);
        Entry entry2 = new Entry(box2);

        Bounds bounds5 = new Bounds(4.0,4.0);
        Bounds bounds6 = new Bounds(4.0,4.0);
        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
        arrbounds3.add(bounds5);
        arrbounds3.add(bounds6);
        BoundingBox box3 = new BoundingBox(arrbounds3);
        Entry entry3 = new Entry(box3);

        Bounds bounds7 = new Bounds(5.0,5.0);
        Bounds bounds8 = new Bounds(5.0,5.0);
        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
        arrbounds3.add(bounds7);
        arrbounds3.add(bounds8);
        BoundingBox box4 = new BoundingBox(arrbounds4);
        Entry entry4 = new Entry(box4);

        Node node = new Node(1,true);

        node.addEntry(entry1);
        node.addEntry(entry2);
        node.addEntry(entry3);

//        ArrayList<Entry> = e

        System.out.println(entry1.getBoundingBox().computeArea());
//        args = new String[1];
//        args[0]="2";
//        System.out.println("Let's go!");
//        if(args.length == 0){
//            System.out.println("You need to give the number of dimensions as an argument my fellow companion");
//        }else{
//            System.out.println("Dimensions : " + args[0]);
//            UserInterface ui = new UserInterface();
//            ui.init(args[0]);
//        }
    }
}