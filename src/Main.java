import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //main
//        args = new String[1];
//        args[0]="2";
//        System.out.println("welcome");
//        if(args.length == 0){
//            System.out.println("you need to give the number of dimensions as an argument my fellow companion");
//        }else{
//            System.out.println("dimensions: " + args[0]);
//            UserInterface ui = new UserInterface();
//            ui.init(args[0]);
//        }

        FileManager fileManager = new FileManager(2);
        RAsteriskTree tree = new RAsteriskTree(2, fileManager);
//
//        Record r1 = new Record(1, "1", new ArrayList<Double>(Arrays.asList(1.0, 1.0)));
//        tree.insertData(r1);
//
//        Record r2 = new Record(2, "2", new ArrayList<Double>(Arrays.asList(2.0, 2.0)));
//        tree.insertData(r2);
//
//        Record r3 = new Record(3, "3", new ArrayList<Double>(Arrays.asList(3.0, 3.0)));
//        tree.insertData(r3);
//
//        Record r4 = new Record(4, "4", new ArrayList<Double>(Arrays.asList(4.0, 4.0)));
//        tree.insertData(r4);
//
//        Record r5 = new Record(5, "5", new ArrayList<Double>(Arrays.asList(5.0, 5.0)));
//        tree.insertData(r5);

        tree.showTree();
//
        fileManager.readIndexfileTest();
//        fileManager.readDatafile();


//        FileManager f = new FileManager(2);
//        Record r1 = new Record(1, "1", new ArrayList<Double>(Arrays.asList(1.0, 1.0)));
//        f.writeRecordToDatafile(r1);

//        UserInterface in = new UserInterface();
//        in.init("2");

//        // insert testing
//        RAsteriskTree tree = new RAsteriskTree(2);
//
//        Record r1 = new Record(1, "1", new ArrayList<Double>(Arrays.asList(1.0, 1.0)));
//        tree.insertData(r1);
//
//        Record r2 = new Record(2, "2", new ArrayList<Double>(Arrays.asList(2.0, 2.0)));
//        tree.insertData(r2);
//
//        Record r3 = new Record(3, "3", new ArrayList<Double>(Arrays.asList(3.0, 3.0)));
//        tree.insertData(r3);
//
//        Record r4 = new Record(4, "4", new ArrayList<Double>(Arrays.asList(777.0, 777.0)));
//        tree.insertData(r4);
//
//        tree.showTree();

//        Record r5 = new Record(5, "5", new ArrayList<Double>(Arrays.asList(5.0, 5.0)));
//        tree.insertData(r5);
//
//        Record r6 = new Record(6, "6", new ArrayList<Double>(Arrays.asList(6.0, 6.0)));
//        tree.insertData(r6);
//
//        Record r7 = new Record(7, "7", new ArrayList<Double>(Arrays.asList(7.0, 7.0)));
//        tree.insertData(r7);
//
//        Record r8 = new Record(8, "8", new ArrayList<Double>(Arrays.asList(8.0, 8.0)));
//        tree.insertData(r8);
//
//        Record r9 = new Record(9, "9", new ArrayList<Double>(Arrays.asList(9.0, 9.0)));
//        tree.insertData(r9);
//
//        Record r10 = new Record(10, "10", new ArrayList<Double>(Arrays.asList(10.0, 10.0)));
//        tree.insertData(r10);
//
//        Record r11 = new Record(11, "11", new ArrayList<Double>(Arrays.asList(11.0, 11.0)));
//        tree.insertData(r11);
//
//        Record r12 = new Record(12, "12", new ArrayList<Double>(Arrays.asList(12.0, 12.0)));
//        tree.insertData(r12);



//        for (Entry entry : tree.getRoot().getEntries()) {
//            entry.getBoundingBox().showBoundingBox();
//            System.out.println(entry.getChildNode().getLevel());
//        }

//        System.out.println(tree.getRoot().getEntries().get(0).getChildNode().getEntries().get(0).getBoundingBox());
//        System.out.println("Entry A of Node");
//        Entry A = tree.getRoot().getEntries().get(0);
//        A.showEntry();
//        System.out.println(A.getChildNode().getEntries().size());

//        Entry B = tree.getRoot().getEntries().get(1);
//        B.getChildNode().getEntries().get(0).getChildNode().getEntries().get(0).showEntry();
//        B.getChildNode().getEntries().get(1).getChildNode().getEntries().get(0).showEntry();
//        B.getChildNode().getEntries().get(1).getChildNode().getEntries().get(1).showEntry();





        // test hasLeaves
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new LeafEntry(0,box1);
//
//        Bounds bounds3 = new Bounds(2.0,1.0);
//        Bounds bounds4 = new Bounds(2.0,1.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new Entry(box2);
//        Node n1 = new Node(1);
//        Node n2 = new Node(2);
//
//        n1.addEntry(entry1);
//
//        n2.addEntry(entry2);
//
//        System.out.println(n1.hasLeaves());
//        System.out.println(n2.hasLeaves());

        // reinsert testing
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
//        Bounds bounds9 = new Bounds(5.0,4.0);
//        Bounds bounds10 = new Bounds(5.0,4.0);
//        ArrayList<Bounds> arrbounds5 = new ArrayList<>();
//        arrbounds5.add(bounds9);
//        arrbounds5.add(bounds10);
//        BoundingBox box5 = new BoundingBox(arrbounds5);
//        Entry entry5 = new Entry(box5);
//
//
//        r.getRoot().getEntries().add(entry1);
//        r.getRoot().getEntries().add(entry2);
//        r.getRoot().getEntries().add(entry3);
//        r.getRoot().getEntries().add(entry4);
//        r.getRoot().getEntries().add(entry5);
//        r.reinsert(r.getRoot());

////        // test split node
//        RAsteriskTree r = new RAsteriskTree(2);
//
//        Bounds bounds1 = new Bounds(1.0,0.0);
//        Bounds bounds2 = new Bounds(1.0,0.0);
//        ArrayList<Bounds> arrbounds1 = new ArrayList<>();
//        arrbounds1.add(bounds1);
//        arrbounds1.add(bounds2);
//        BoundingBox box1 = new BoundingBox(arrbounds1);
//        Entry entry1 = new LeafEntry(1, box1);
//
//        Bounds bounds3 = new Bounds(21.0,20.0);
//        Bounds bounds4 = new Bounds(21.0,20.0);
//        ArrayList<Bounds> arrbounds2 = new ArrayList<>();
//        arrbounds2.add(bounds3);
//        arrbounds2.add(bounds4);
//        BoundingBox box2 = new BoundingBox(arrbounds2);
//        Entry entry2 = new LeafEntry(2, box2);
//
//        Bounds bounds5 = new Bounds(15.0,14.0);
//        Bounds bounds6 = new Bounds(15.0,14.0);
//        ArrayList<Bounds> arrbounds3 = new ArrayList<>();
//        arrbounds3.add(bounds5);
//        arrbounds3.add(bounds6);
//        BoundingBox box3 = new BoundingBox(arrbounds3);
//        Entry entry3 = new LeafEntry(3, box3);
//
//        Bounds bounds7 = new Bounds(5.0,4.0);
//        Bounds bounds8 = new Bounds(5.0,4.0);
//        ArrayList<Bounds> arrbounds4 = new ArrayList<>();
//        arrbounds4.add(bounds7);
//        arrbounds4.add(bounds8);
//        BoundingBox box4 = new BoundingBox(arrbounds4);
//        Entry entry4 = new LeafEntry(4, box4);
//
//        Node node = new Node(1);
//
//        node.addEntry(entry1);
//        node.addEntry(entry2);
//        node.addEntry(entry3);
////        node.addEntry(entry4);
//
//        Pair<ArrayList<Entry>, ArrayList<Entry>> split = r.split(node);
//
//        System.out.println("first group");
//        for (Entry entry : split.getFirst()) {
//            entry.showEntry();
//        }
//        System.out.println();
//        System.out.println("second group");
//        for (Entry entry : split.getSecond()) {
//            entry.showEntry();
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



    }
}