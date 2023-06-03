import java.util.ArrayList;
import java.util.Arrays;

public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    private final static int leafLevel = 1;
    //defines how many entries of a node should be used during ChooseSubtree
    private final static int RTREE_CHOOSE_SUBTREE_P = 32;
    private Node root;
    private int M;
    private int m;
    private int numberOfEntriesInTheTree;
    public RAsteriskTree(int dimensions){
        this.dimensions=dimensions;
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
        insertData(new Record(632980450, "632980450",new ArrayList<Double>(Arrays.asList(41.5448493,26.5947027))));
    }

    public void insertData(Record record) {
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

        // CS2
        if (node.hasLeaves()) {
            node.addEntry(entry);
        } else {
            //I1
            Node chosenNode = chooseSubtree(node, entry, level);
            Node entryToAdd = insert(entry, chosenNode, level);

            if (chosenNode == null) {
                return null;
            }

            node.addEntry(entry);
        }

        // Overflow treatment


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
                node.getEntryWithTheLeastAreaEnlargement(entry, node.getEntries());

                // From the items in A, considering all items in
                // N, choose the leaf/entry whose rectangle needs the
                // least overlap enlargement
                node.getEntryWithTheLeastOverlapEnlargement(entry);
                return node.getEntries().get(0).getChildNode();
            }
            // choose the leaf/entry in N whose rectangle needs the least
            // overlap enlargement to include the new data
            // rectangle. Resolve ties by choosing the leaf/entry
            // whose rectangle needs the least area enlargement, then
            // the leaf with the rectangle of smallest area

        }
        return null;
    }



}
