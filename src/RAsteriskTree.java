import java.util.*;

public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    // an arraylist of boolean where true is stored if overflow treatment has occurred to a level
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
        M=2;
        m=1;

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

        List<Node> splitResult = split(node);

        // If OverflowTreatment caused a split of the root, create a new root
        if (node.isRoot()) {

            // I4
        }

        return null;
    }

    public void reinsert(Node node) {

    }

    public List<Node> split(Node node) {
        List<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisDistribution = chooseSplitAxis(node);
        return chooseSplitIndex(chooseSplitAxisDistribution, node.getLevel());
    }

    public List<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxis(Node node) {
        // For each axis sort the entries by the lower than by the upper
        // value of their rectangles and determine all distributions as described above Compute S which is the
        // sum of all margin-values of the different distributions


        double margin = Double.MAX_VALUE;
        List<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisDistribution = new ArrayList<>();
        for (int dim = 0; dim < dimensions; dim++) {
            // sort entries by lower and upper bound and put them in two arraylists
            ArrayList<Entry> sortedByUpperBound = node.sortEntriesByBound(dim, true);
            ArrayList<Entry> sortedByLowerBound = node.sortEntriesByBound(dim, false);
            List<ArrayList<Entry>> listOfAllSortedEntries = new ArrayList<>();
            listOfAllSortedEntries.add(sortedByUpperBound);
            listOfAllSortedEntries.add(sortedByLowerBound);

//            for(Entry e : sortedByUpperBound) {
//                e.showEntry();
//            }
//
//            for(Entry e : sortedByLowerBound) {
//                e.showEntry();
//            }

            // margin of the distributions of this axis
            double axisMargin = 0;
            // list of pairs of first and second group
            List<Pair<ArrayList<Entry>, ArrayList<Entry>>> optimalDistributions = new ArrayList<>();
            // iterate all the possible distributions
            for (ArrayList<Entry> list : listOfAllSortedEntries) {
                // initialize groups
                ArrayList<Entry> firstGroup = new ArrayList<>();
                ArrayList<Entry> secondGroup = new ArrayList<>();
                // for each sort M-2m+2 distributions of the M+1 entries into two groups are
                // determined, where the k-th distribution (k=1, , (M-2m+2)) is described
                // as follows.
                for (int k=1; k<M-2*m+2; k++) {
                    //  the first group contains the first (m-1)+k entries, the second
                    //  group contains the remaining entries.
                    firstGroup.clear();
                    secondGroup.clear();
                    for (int i=0; i<node.getEntries().size(); i++) {
                        if (i<m-1+k) {
                            firstGroup.add(list.get(i));
                        } else {
                            secondGroup.add(list.get(i));
                        }
                    }
                    // construct the bounding boxes of each group
                    BoundingBox firstGroupBoundingBox = new BoundingBox();
                    firstGroupBoundingBox.createBoundingBoxOfEntries(firstGroup);
                    BoundingBox secondGroupBoundingBox = new BoundingBox();
                    secondGroupBoundingBox.createBoundingBoxOfEntries(secondGroup);
                    optimalDistributions.add(new Pair<>(firstGroup, secondGroup));
                    axisMargin += firstGroupBoundingBox.computeMargin() + secondGroupBoundingBox.computeMargin();
                }
                // Choose the axis with the minimum sum as split axis
                if (margin > axisMargin)
                {
                    // bestSplitAxis = d;
                    margin = axisMargin;
                    chooseSplitAxisDistribution.clear();
                    chooseSplitAxisDistribution = optimalDistributions;
                }
            }
        }
        return chooseSplitAxisDistribution;
    }

    public List<Node> chooseSplitIndex (List<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisDistribution, int level) {
        // compute the overlap of each pair and store in a hashmap as value
        // with key the index of list that contains the pairs
        HashMap<Integer, Double> overlap = new HashMap<>();
        for (int i=0; i<chooseSplitAxisDistribution.size(); i++) {
            BoundingBox firstGroupBoundingBox = new BoundingBox();
            firstGroupBoundingBox.createBoundingBoxOfEntries(chooseSplitAxisDistribution.get(i).getFirst());
            BoundingBox secondGroupBoundingBox = new BoundingBox();
            secondGroupBoundingBox.createBoundingBoxOfEntries(chooseSplitAxisDistribution.get(i).getSecond());
            overlap.put(i, firstGroupBoundingBox.computeOverlap(secondGroupBoundingBox));
        }
        // Convert the HashMap to a list
        List<Map.Entry<Integer, Double>> overlapList = new ArrayList<>(overlap.entrySet());
        // Sort the list based on the values (overlap)
        Collections.sort(overlapList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        // Find the smallest overlap (ties)
        Double smallestOverlap = overlapList.get(0).getValue();
        int index = 0;
        // Remove the elements with overlaps other than the smallest overlap
        Iterator<Map.Entry<Integer, Double>> iterator = overlapList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestOverlap)) {
                iterator.remove();
            }
        }

        // Resolve ties by choosing the distribution with the minimum area-value
        if (overlapList.size()>1) {
            double smallestArea = Double.MAX_VALUE;
            for (int i=0; i<overlapList.size(); i++) {
                ArrayList<Entry> merged = new ArrayList<>();
                merged.addAll(chooseSplitAxisDistribution.get(overlapList.get(i).getKey()).getFirst());
                merged.addAll(chooseSplitAxisDistribution.get(overlapList.get(i).getKey()).getSecond());
                BoundingBox box = new BoundingBox();
                box.createBoundingBoxOfEntries(merged);
                double area = box.computeArea();
                if (area<smallestArea) {
                    smallestArea = area;
                    index=i;
                }
            }
        }
        ArrayList<Node> splitNodes = new ArrayList<>();
        Node firstNode = new Node(level, false);
        firstNode.setEntries(chooseSplitAxisDistribution.get(index).getFirst());
        Node secondNode = new Node(level, false);
        secondNode.setEntries(chooseSplitAxisDistribution.get(index).getSecond());
        splitNodes.add(firstNode);
        splitNodes.add(secondNode);
        return splitNodes;

    }


}

class Pair<T1, T2> {
    private T1 first;
    private T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }
}
