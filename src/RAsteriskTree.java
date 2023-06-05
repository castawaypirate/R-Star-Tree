import java.util.ArrayList;
import java.util.Arrays;

public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    private ArrayList<Boolean> otInLevel;
    private final static int leafLevel = 1;
    //defines how many entries of a node should be used during ChooseSubtree
    private final static int RTREE_CHOOSE_SUBTREE_P = 32;
    private Node root;
    private int M;
    private int m;
    private int numberOfEntriesInTheTree;
    public RAsteriskTree(int dimensions){
        this.dimensions = dimensions;
        otInLevel = new ArrayList<>();
        otInLevel.add(false);
        fileManager = new FileManager(dimensions);
        root = new Node(1, true);
        numberOfEntriesInTheTree = 0;
        M=64;
        m=16;

    }

    public void bulkLoading(String CSVfilePath){
        // must do or the entries must be sorted, only time will tell
        ArrayList<Record> sortedRecords = fileManager.readDataFromCSVFile(CSVfilePath);
        fileManager.writeToDatafile(sortedRecords);
        insertData(new Record(632980450, "632980450", new ArrayList<Double>(Arrays.asList(41.5448493,26.5947027))));
    }

    public void insertData(Record record) {
        // Takes a record as a parameter, makes and entry out of it
        // and inserts in to the tree
        ArrayList<Bounds> boundsInEachDimension = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            Bounds bounds = new Bounds(record.getCoordinates().get(i), record.getCoordinates().get(i));
            boundsInEachDimension.add(bounds);
        }
        insert(new LeafEntry(record.getId(), new BoundingBox(boundsInEachDimension)), root, leafLevel);
        numberOfEntriesInTheTree++;
    }

    public Node insert(Entry entry, Node node, int level) {
        // I4
        if (node.getParentEntry() == null) {
            System.out.println("We still in root");
        } else {
            node.getParentEntry().setBoundingBox(node.getParentEntry().assumingBoundingBox(entry));
        }
        // CS2
        if (node.getLevel() == level) {
            node.addEntry(entry);
        } else {
            //I1
            Node chosenNode = chooseSubtree(node, entry, level);
            Node entryToAdd = insert(entry, chosenNode, level);

            if (entryToAdd == null) {
                return null;
            }

//            node.addEntry(entryToAdd.);
        }

        // Overflow treatment
        if (node.getEntries().size()>=M) {
            return overflowTreatment(node);
        }

        return null;
    }

    public Node chooseSubtree(Node node, Entry entry, int level) {
        if (node.hasLeaves()) {
            if (M > (RTREE_CHOOSE_SUBTREE_P*2)/3  && node.getEntries().size() > RTREE_CHOOSE_SUBTREE_P) {
                // ** alternative algorithm:
                // Sort the rectangles in N in increasing order of
                // then area enlargement needed to include the new
                // data rectangle

                // Let A be the group of the first p entries

                // From the items in A, considering all items in
                // N, choose the leaf/entry whose rectangle needs the
                // least overlap enlargement



                // Choose the entry in N whose bounding box needs the least overlap enlargement
                // to include the new data
                Entry N = node.getEntryWithTheLeastOverlapEnlargement(entry);

                return N.getChildNode();
            }
            // choose the leaf/entry in N whose rectangle needs the least
            // overlap enlargement to include the new data
            // rectangle. Resolve ties by choosing the leaf/entry
            // whose rectangle needs the least area enlargement, then
            // the leaf with the rectangle of smallest area
            Entry N = node.getEntryWithTheLeastOverlapEnlargement(entry);

            return N.getChildNode();

        }

        Entry N = node.getEntryWithTheLeastAreaEnlargement(entry, node.getEntries());
        return N.getChildNode();
    }

    public Node overflowTreatment(Node node) {
        // OT1: If the level is not the root level AND this is the first
        // call of OverflowTreatment in the given level during the
        // insertion of one data rectangle, then invoke Reinsert
        if (!node.isRoot() && otInLevel.get(node.getLevel()-1)==false) {
            // overflow treatment was called in this level
            otInLevel.set(node.getLevel()-1, true);
            // set the next level false
            otInLevel.add(false);
            reinsert(node);
            return null;
        }

        Node split = split(node);

        if (node.isRoot()) {

        }

        return split;
    }

    public void reinsert(Node node) {

    }

    public Node split(Node node) {
        return null;
    }

    public void chooseSplitAxis(Node node) {

    }



}
