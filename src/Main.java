import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // reinsert testing
        RAsteriskTree r = new RAsteriskTree(2);

        Bounds bounds1 = new Bounds(1.0,0.0);
        Bounds bounds2 = new Bounds(1.0,0.0);
        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
        arrbounds1.add(bounds1);
        arrbounds1.add(bounds2);
        BoundingBox box1 = new BoundingBox(arrbounds1);
        Entry entry1 = new Entry(box1);

        Bounds bounds3 = new Bounds(2.0,1.0);
        Bounds bounds4 = new Bounds(2.0,1.0);
        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
        arrbounds2.add(bounds3);
        arrbounds2.add(bounds4);
        BoundingBox box2 = new BoundingBox(arrbounds2);
        Entry entry2 = new Entry(box2);

        Bounds bounds5 = new Bounds(3.0,2.0);
        Bounds bounds6 = new Bounds(3.0,2.0);
        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
        arrbounds3.add(bounds5);
        arrbounds3.add(bounds6);
        BoundingBox box3 = new BoundingBox(arrbounds3);
        Entry entry3 = new Entry(box3);

        Bounds bounds7 = new Bounds(4.0,3.0);
        Bounds bounds8 = new Bounds(4.0,3.0);
        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
        arrbounds4.add(bounds7);
        arrbounds4.add(bounds8);
        BoundingBox box4 = new BoundingBox(arrbounds4);
        Entry entry4 = new Entry(box4);

        Bounds bounds9 = new Bounds(5.0,4.0);
        Bounds bounds10 = new Bounds(5.0,4.0);
        ArrayList<Bounds> arrbounds5 = new ArrayList<>();
        arrbounds5.add(bounds9);
        arrbounds5.add(bounds10);
        BoundingBox box5 = new BoundingBox(arrbounds5);
        Entry entry5 = new Entry(box5);


        r.getRoot().getEntries().add(entry1);
        r.getRoot().getEntries().add(entry2);
        r.getRoot().getEntries().add(entry3);
        r.getRoot().getEntries().add(entry4);
        r.getRoot().getEntries().add(entry5);
        r.reinsert(r.getRoot());

//        // test split node
//        RAsteriskTree r = new RAsteriskTree(2);
//
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,1.0);
//        Bounds bounds4 = new Bounds(2.0,1.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(3.0,2.0);
//        Bounds bounds6 = new Bounds(3.0,2.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(4.0,3.0);
//        Bounds bounds8 = new Bounds(4.0,3.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry3);
////        node.addEntry(entry4);
//
//        List<Node> split = r.split(node);
//
//        for (Node e: split) {
//            e.showEntries();
//        }




//        // test node sortEntriesByBound
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,1.0);
//        Bounds bounds4 = new Bounds(2.0,1.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(3.0,2.0);
//        Bounds bounds6 = new Bounds(3.0,2.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(4.0,3.0);
//        Bounds bounds8 = new Bounds(4.0,3.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry2);
//        node.addEntry(entry1);
//        node.addEntry(entry3);
//        node.showEntries();
//
//        node.setEntries(node.sortEntriesByBound(1,false));
//        System.out.println();
//        node.showEntries();


        //sort entries by overlap enlargement functionality testing
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,1.0);
//        Bounds bounds4 = new Bounds(2.0,1.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(3.0,2.0);
//        Bounds bounds6 = new Bounds(3.0,2.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(4.0,3.0);
//        Bounds bounds8 = new Bounds(4.0,3.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
////        entry4.assumingBoundingBox(entry2).showBoundingBox();
////
////        System.out.println(entry4.assumingBoundingBox(entry2).computeOverlap(entry1.getBoundingBox()));
////        System.out.println(entry4.assumingBoundingBox(entry2).computeOverlap(entry3.getBoundingBox()));
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry4);
////        node.showEntries();
//        node.getEntryWithTheLeastOverlapEnlargement(entry3).showEntry();
////        node.showEntries();


        //sort entries by enlargement area and ties functionality testing
//        Bounds bounds1 = new Bounds(0.0,0.0);
//        Bounds bounds2 = new Bounds(0.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,2.0);
//        Bounds bounds4 = new Bounds(2.0,2.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(3.0,3.0);
//        Bounds bounds6 = new Bounds(3.0,3.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(4.0,4.0);
//        Bounds bounds8 = new Bounds(4.0,4.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry4);
////        node.showEntries();
//        node.sortEntriesByAreaEnlargement(entry3).showEntry();
////        node.showEntries();


        //sort entries by enlargement area functionality testing
//        Bounds bounds1 = new Bounds(0.0,0.0);
//        Bounds bounds2 = new Bounds(0.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,2.0);
//        Bounds bounds4 = new Bounds(2.0,2.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(4.0,4.0);
//        Bounds bounds6 = new Bounds(4.0,4.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(5.0,5.0);
//        Bounds bounds8 = new Bounds(5.0,5.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry3);
//        node.showEntries();
//        node.sortEntriesByAreaEnlargement(entry4);
//        node.showEntries();



        // overlap functionality testing
//        Bounds bounds9 = new Bounds(6.0,2.0);
//        Bounds bounds10 = new Bounds(6.0,0.0);
//        ArrayList<Bounds> arrbounds5 = new ArrayList<>();
//        arrbounds5.add(bounds9);
//        arrbounds5.add(bounds10);
//        BoundingBox box5 = new BoundingBox(arrbounds5);
//        System.out.println(box5.computeArea());
//
//        Bounds bounds11 = new Bounds(4.0,1.0);
//        Bounds bounds12 = new Bounds(4.0,3.0);
//        ArrayList<Bounds> arrbounds6 = new ArrayList<>();
//        arrbounds6.add(bounds11);
//        arrbounds6.add(bounds12);
//        BoundingBox box6 = new BoundingBox(arrbounds6);
//        System.out.println(box6.computeOverlap(box5));
//        node.showEntries();
//        System.out.println(yo.size());
//        for(Entry entry : yo) {
//            entry.showEntry();
//        }



        //sort entries by enlargement area functionality testing
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new Entry(box1);
//
//        Bounds bounds3 = new Bounds(2.0,1.0);
//        Bounds bounds4 = new Bounds(2.0,1.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//
//        Bounds bounds5 = new Bounds(3.0,2.0);
//        Bounds bounds6 = new Bounds(3.0,2.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new Entry(box3);
//
//        Bounds bounds7 = new Bounds(4.0,3.0);
//        Bounds bounds8 = new Bounds(4.0,3.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new Entry(box4);
//
////        entry4.assumingBoundingBox(entry2).showBoundingBox();
////
////        System.out.println(entry4.assumingBoundingBox(entry2).computeOverlap(entry1.getBoundingBox()));
////        System.out.println(entry4.assumingBoundingBox(entry2).computeOverlap(entry3.getBoundingBox()));
//
//        Node node = new Node(1,true);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry3);
//        node.showEntries();
//        node.sortEntriesByOverlapEnlargement(entry4);
//        node.showEntries();


        //main
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