import java.util.*;

public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    // an arraylist of boolean where true is stored if overflow treatment has occurred to a level
    private ArrayList<Boolean> otInLevel = new ArrayList<>();
    private Node root;
    private int rootLevel;
    // maximum entries per node
    private int M;
    // minimum entries per node
    private int m;
    private final static int leafLevel = 1;
    // defines how many entries of a node should be used during ChooseSubtree
    private final static int chooseSubtree_p = 32;

    private int reinsert_p;
    public RAsteriskTree(int dimensions, FileManager fileManager){
        this.dimensions = dimensions;
        this.fileManager = fileManager;
        // read or create root
        root = fileManager.getRoot();
        rootLevel= root.getLevel();
        // set overflow treatment false for level 1
        otInLevel.add(false);
        M = fileManager.getMaxNumberOfEntriesInBlock();
        // 40% of M best performance
        m = Math.max((int) Math.floor(0.4 * M), 1);
        // 30% of M best performance
        reinsert_p = Math.max((int) Math.floor(0.3 * M), 1);
    }

    public int getDimensions(){
        return dimensions;
    }

    public Node getRoot() {
        return root;
    }

    public void showTree() {
        System.out.println();
        showNode(root, "");
        System.out.println();
    }

    private void showNode(Node node, String indent) {
        if (node != null) {
            System.out.println(indent + "Level: " + node.getLevel());
            String boundingBoxInfo = indent + "Bounding Box: " + node.getBoundingBoxInStringPoint(dimensions);
            System.out.println(boundingBoxInfo);
            indent += "\t";
            for (Entry entry : node.getEntries()) {
                if (entry instanceof LeafEntry) {
                    LeafEntry leafEntry = (LeafEntry) entry;
                    System.out.println(indent + "Leaf Entry: Record ID: " + leafEntry.getLeafEntryId());
                } else {
                    Node childNode = entry.getChildNode();
                    System.out.println(indent + "Index Node:");
                    showNode(childNode, indent);
                }
            }
        }
    }

    public void bulkLoading(String CSVfilePath){
        // must do or the entries must be sorted, only time will tell
        // look the recordings for clues for the bulk loading
        System.out.println(CSVfilePath);
//        insertData(new Record(632980450, "632980450", new ArrayList<Double>(Arrays.asList(41.5448493,26.5947027))));
//        ArrayList<Record> sortedRecords = fileManager.readDataFromCSVFile(CSVfilePath);
//        fileManager.writeToDatafile(sortedRecords);
    }

    // Takes a record as a parameter, makes a leaf entry out of it
    // and inserts in to the tree
    public void insertData(Record record) {
        ArrayList<Bounds> boundsInEachDimension = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            Bounds bounds = new Bounds(record.getCoordinates().get(i), record.getCoordinates().get(i));
            boundsInEachDimension.add(bounds);
        }
        // ID1 Invoke Insert starting with the leaf level as a
        // parameter, to insert a new data rectangle
        // CS1 Set N to be the root (as we pass the root as the node to add)
        fileManager.readIndexfile();
        insert(new LeafEntry(record.getId(), new BoundingBox(boundsInEachDimension)), root, leafLevel);
        fileManager.writeToIndexfile();
        fileManager.writeRecordToDatafile(record);
    }

    public Node insert(Entry entry, Node node, int level) {
        Node nodeToAdd = null;
        // I4 Adjust all covering rectangles in the insertion
        // path such that they are minimum bounding boxes
        // enclosing their children
        if(node.getLevel()!=rootLevel) {
            node.adjustBoundingBoxToFitEntry(entry);
            // update bounding box of node in the indexblock
            fileManager.createUpdateIndexBlock(node);
        }
        // CS2 If N is a leaf (return N)
        if (node.getLevel() == level) {
            // I2 If N has less than M entries, accommodate E in N
            node.addEntry(entry);
            fileManager.createUpdateIndexBlock(node);
        } else {
            // I1 Invoke ChooseSubtree, with the level as a parameter,
            // to find an appropriate node N, in which to place the
            // new Entry E (entry)
            Node chosenNode = chooseSubtree(node, entry, level);
            nodeToAdd = insert(entry, chosenNode, level);

            if (nodeToAdd != null) {
                node.addEntry(nodeToAdd.getParentEntry());
            }
            fileManager.createUpdateIndexBlock(node);
        }
        // I2 If N has M entries, invoke OverflowTreatment with the
        // level of N as a parameter [for reinsertion or split]
        if (node.getEntries().size() >= M) {
            Pair<ArrayList<Entry>, ArrayList<Entry>> overflowTreatmentResult = overflowTreatment(node, node.getLevel());
            // overflowTreatmentResult is null reinsert occurred, otherwise split occurred
            if (overflowTreatmentResult != null) {
                // S3 Distribute the entries into two groups
                node.getEntries().clear();
                node.setEntries(overflowTreatmentResult.getFirst());

                Node secondNode = new Node(node.getLevel());
                secondNode.setEntries(overflowTreatmentResult.getSecond());
                Entry secondEntry = new Entry(secondNode);

                if (node.getLevel() == rootLevel) {
                    // I3 If OverflowTreatment caused a split of the root, create a new root
                    rootLevel++;
                    Node newRoot = new Node(rootLevel);

                    Entry firstEntry = new Entry(node);

                    newRoot.addEntry(firstEntry);
                    newRoot.addEntry(secondEntry);

                    // copy the blockid to the new root
                    newRoot.setBlockid(root.getBlockid());
                    root = newRoot;

                    fileManager.createUpdateIndexBlock(root);
                    // reset blockid
                    node.setBlockid(0);
                    fileManager.createUpdateIndexBlock(node);
                    fileManager.createUpdateIndexBlock(secondNode);
                } else {
                    // adjust bounding box of the existing node
                    BoundingBox newNodeBoundingBox = new BoundingBox();
                    newNodeBoundingBox.createBoundingBoxOfEntries(node.getEntries());
                    node.getParentEntry().setBoundingBox(newNodeBoundingBox);

                    fileManager.createUpdateIndexBlock(node);
                    fileManager.createUpdateIndexBlock(secondNode);

                    // I3 If OverflowTreatment was called and a split was performed, propagate
                    // OverflowTreatment upwards if necessary
                    return  secondEntry.getChildNode();
                }
            }
            return null;
        }
        return null;
    }

    public Node chooseSubtree(Node node, Entry entry, int level) {
        // CS2 if the childpointers in N point to leaves [determine
        // the minimum overlap cost],

        // we need to see of the childpointers of node point to leaves
        // that's why we are checking it by increasing the leaf level by 1
        if (node.getLevel() == level + 1) {


//            if (M > (chooseSubtree_p*2)/3  && node.getEntries().size() > chooseSubtree_p) {
//                // ** alternative algorithm:
//                // Sort the rectangles in N in increasing order of
//                // then area enlargement needed to include the new
//                // data rectangle
//
//                // Let A be the group of the first p entries
//
//                // From the items in A, considering all items in
//                // N, choose the leaf/entry whose rectangle needs the
//                // least overlap enlargement
//
//                // Choose the entry in N whose bounding box needs the least overlap enlargement
//                // to include the new data
//                Entry N = node.getEntryWithTheLeastOverlapEnlargement(entry);
//
//                return N.getChildNode();
//            }


            // CS2 choose the entry in N whose rectangle needs the least
            // overlap enlargement to include the new data
            // rectangle.
            Entry N = node.getEntryWithTheLeastOverlapEnlargement(entry);
            // return the node that the entry N (which is inside a node at level + 1) points to
            return N.getChildNode();
        }
        // CS2 if the childpointers in N do not point to leaves [determine the minimum
        // area cost],
        // choose the entry in N whose rectangle needs the least area enlargement to
        // include the new data rectangle.
        Entry N = node.getEntryWithTheLeastAreaEnlargement(entry, node.getEntries());
        return N.getChildNode();
    }

    public Pair<ArrayList<Entry>, ArrayList<Entry>> overflowTreatment(Node node, int nodeLevel) {
        Pair<ArrayList<Entry>, ArrayList<Entry>> overflowTreatmentResult = null;
        // OT1 If the level is not the root level and this is the first
        // call of OverflowTreatment in the given level during the
        // insertion of one data rectangle, then
        if (nodeLevel!=rootLevel && otInLevel.get(nodeLevel-1)==false) {
            // overflow treatment was called in this level
            otInLevel.set(nodeLevel-1, true);
            otInLevel.add(false);
            // invoke Reinsert
            reinsert(node);
        } else {
            // else
            // invoke Split
            overflowTreatmentResult = split(node);
        }
        return overflowTreatmentResult;
    }

    public void reinsert(Node node) {
        // create a bounding box for the M+1 entries that the node currently has
        BoundingBox nodeBoundingBox = new BoundingBox();
        nodeBoundingBox.createBoundingBoxOfEntries(node.getEntries());
        // RI1 For all M+l items of a node N, compute the distance between the centers of their rectangles
        // and the center of the bounding rectangle of N
        HashMap<Integer, Double> distancesFromCenter = new HashMap<>();
        for (int i=0; i<node.getEntries().size(); i++) {
            distancesFromCenter.put(i, nodeBoundingBox.
                    computeDistanceBetweenCenters(node.getEntries().get(i).getBoundingBox().computeCenter()));
        }

        // RI2: Sort the items in decreasing order (or increasing order)
        // of their distances computed in RI1

        // convert the HashMap to a List of Map.Entry objects
        List<Map.Entry<Integer, Double>> entries = new ArrayList<>(distancesFromCenter.entrySet());
        // sort the list in decreasing order of values (far reinsert)
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                // compare the values in descending order
                return Double.compare(entry2.getValue(), entry1.getValue());
            }
        });
        // RI3 Remove the first p entries from N and adjust the bounding box of N
        List<Entry> entriesToRemove = new ArrayList<>();
        for (int i = 0; i < reinsert_p; i++) {
            int index = entries.get(i).getKey();
            Entry entryToRemove = node.getEntries().get(index);
            entriesToRemove.add(entryToRemove);
        }
        // remove the entries from the node
        for (Entry entryToRemove : entriesToRemove) {
            node.getEntries().remove(entryToRemove);
        }
        // adjust the bounding box of the parent entry of the node
        nodeBoundingBox.createBoundingBoxOfEntries(node.getEntries());
        node.getParentEntry().setBoundingBox(nodeBoundingBox);

        // update bounding box of node in the indexblock
        fileManager.createUpdateIndexBlock(node);

        // RI4 In the sort, defined in RI2, starting with the maximum distance
        // (= far reinsert) or minimum distance (= close reinsert), invoke Insert
        // to reinsert the entries
        for (Entry removeEntry : entriesToRemove) {
            insert(removeEntry, root, node.getLevel());
        }
    }

    // Algorithm Split
    public Pair<ArrayList<Entry>, ArrayList<Entry>> split(Node node) {
        // S1 Invoke ChooseSplitIndex to determine the axis, perpendicular to which
        // the split is performed
        ArrayList<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisListOfPairs = chooseSplitAxis(node);
        // S2 Invoke ChooseSplitIndex to determine the best distribution into two groups
        // along that axis
        return chooseSplitIndex(chooseSplitAxisListOfPairs);
    }

    // Algorithm ChooseSplitAxis
    // In order to not compute the array of pairs again, instead of the axis it returns the list of pairs
    // which contain the first and the second group of entries that have been distributed
    public ArrayList<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxis(Node node) {
        // CSA1 For each axis/dimension
        // Sort the entries by the lower, then by the upper value of their rectangles and
        // determine all distributions/pairs of groups as described in the R*-tree paper.
        // Compute S/marginS, the sum of all margin-values of the different distributions/
        // pairs fo groups
        double marginS = Double.MAX_VALUE;
//        int splitAxis = 0;
        ArrayList<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisListOfPairs = new ArrayList<>();
        for (int dim = 0; dim < dimensions; dim++) {
            // sort entries by lower, then by upper bound and put them in two ArrayLists
            ArrayList<Entry> sortedByLowerBound = node.sortEntriesByBound(dim, false);
            ArrayList<Entry> sortedByUpperBound = node.sortEntriesByBound(dim, true);

            // put the returned ArrayLists in a List
            List<ArrayList<Entry>> listOfSortedEntriesInAxis = new ArrayList<>();
            listOfSortedEntriesInAxis.add(sortedByLowerBound);
            listOfSortedEntriesInAxis.add(sortedByUpperBound);

            // margin of this axis
            double axisMargin = 0;
            // list of pairs of first and second group
            ArrayList<Pair<ArrayList<Entry>, ArrayList<Entry>>> optimalPairs = new ArrayList<>();

            // iterate all the possible distributions
            for (ArrayList<Entry> list : listOfSortedEntriesInAxis) {
                // For each sort M-2m+2 distributions of the M+1 entries into two groups are
                // determined, where the k-th distribution (k=1, , (M-2m+2)) is described
                // as follows.
                for (int k=1; k<M-2*m+2; k++) {
                    //  The first group contains the first (m-1)+k entries, the second
                    //  group contains the remaining entries.
                    ArrayList<Entry> firstGroup = new ArrayList<>();
                    ArrayList<Entry> secondGroup = new ArrayList<>();
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

                    // add the pair of groups in optimalPairs
                    optimalPairs.add(new Pair<>(firstGroup, secondGroup));
                    // add the margin of the two bounding boxes of the two groups to the axisMargin
                    axisMargin += firstGroupBoundingBox.computeMargin() + secondGroupBoundingBox.computeMargin();
                }

                // CSA2 Choose the axis with the minimum S/marginS as split axis
                if (marginS > axisMargin)
                {
//                    splitAxis=dim;
                    marginS = axisMargin;
                    chooseSplitAxisListOfPairs.clear();
                    for (Pair pair : optimalPairs) {
                        chooseSplitAxisListOfPairs.add(pair);
                    }
                }
            }
        }
        return chooseSplitAxisListOfPairs;
    }

    // Algorithm ChooseSplitIndex
    public Pair<ArrayList<Entry>, ArrayList<Entry>> chooseSplitIndex (List<Pair<ArrayList<Entry>, ArrayList<Entry>>> chooseSplitAxisDistribution) {
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
        // convert the HashMap to a list
        List<Map.Entry<Integer, Double>> overlapList = new ArrayList<>(overlap.entrySet());
        // sort the list based on the values (overlap)
        Collections.sort(overlapList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        // find the smallest overlap (ties)
        Double smallestOverlap = overlapList.get(0).getValue();
        int index = 0;
        // remove the elements with overlaps other than the smallest overlap
        Iterator<Map.Entry<Integer, Double>> iterator = overlapList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestOverlap)) {
                iterator.remove();
            }
        }
        // resolve ties by choosing the distribution with the minimum area-value
        if (overlapList.size()>1) {
            double smallestArea = Double.MAX_VALUE;
            for (int i=0; i<overlapList.size(); i++) {
                BoundingBox firstBoundingBox = new BoundingBox();
                firstBoundingBox.createBoundingBoxOfEntries(chooseSplitAxisDistribution.get(overlapList.get(i).getKey()).getFirst());
                BoundingBox secondBoundingBox = new BoundingBox();
                secondBoundingBox.createBoundingBoxOfEntries(chooseSplitAxisDistribution.get(overlapList.get(i).getKey()).getSecond());
                double area = firstBoundingBox.computeArea() + secondBoundingBox.computeArea();
                if (area<smallestArea) {
                    smallestArea = area;
                    index=i;
                }
            }
        }
        return chooseSplitAxisDistribution.get(index);
    }

    // Algorithm Search
    public void search(BoundingBox searchBox, Node node){
        // S1 [Search subtrees] If T(node) is not a leaf,
        // check each Entry E to determine whether E.I overlaps
        // S. For all overlapping entries, invoke Search on the
        // tree whose root node is pointed to by E.p
        if(node.getLevel() != leafLevel) {
            for(Entry E : node.getEntries()) {
                if(E.getBoundingBox().overlap(searchBox)) {
                    search(searchBox, E.getChildNode());
                }
            }
        }
        // S2 [Search leaf node] If T is a leaf, check all entries E
        // to determine whether E.I overlaps S. If so, E is a qualifying
        // record
        else {
            for(Entry E : node.getEntries()) {
                if(E.getBoundingBox().overlap(searchBox)) {
                    E.showEntry();
                }
            }
        }
    }

    public void branchAndBoundSkyline(){
        // list of skyline points
        ArrayList<LeafEntry> skyline = new ArrayList<>();
        MinHeap<Entry> minHeap = new MinHeap<>();
        // initialize origin
        ArrayList<Double> origin = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            origin.add(0.0);
        }
        // insert all entries of roo in the heap
        for(Entry entry : root.getEntries()) {
            minHeap.addObject(entry, entry.getBoundingBox().computeManhattanDistanceFromPoint(origin));
        }
        // while heap not empty
        while(!minHeap.getSortedEntries().isEmpty()) {
            // remove top entry
            Entry entryToCheck = minHeap.getSortedEntries().get(0).getKey();
            minHeap.getSortedEntries().remove(0);
            // if entryToCheck is not dominated by any point in skyline list
            if(!entryToCheck.getBoundingBox().dominated(skyline)) {
                // if entryToCheck is not a leaf entry
                if(!(entryToCheck instanceof LeafEntry)) {
                    // for each child in entryToCheck
                    for(Entry childEntry : entryToCheck.getChildNode().getEntries()) {
                        // if child is not dominated by any point in skyline list
                        if(!childEntry.getBoundingBox().dominated(skyline)) {
                            // insert child into heap
                            minHeap.addObject(childEntry, childEntry.getBoundingBox().computeManhattanDistanceFromPoint(origin));
                        }
                    }
                } else { // entryToCheck if a leaf entry
                    skyline.add((LeafEntry)entryToCheck);
                }
            }
        }

        for(LeafEntry entry : skyline) {
            entry.showEntry();
        }
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


class MinHeap<T> {
    private List<Map.Entry<T, Double>> sortedEntries;

    public MinHeap() {
        sortedEntries = new ArrayList<>();
    }

    public void addObject(T object, double value) {
        Map.Entry<T, Double> newEntry = new AbstractMap.SimpleEntry<>(object, value);
        sortedEntries.add(newEntry);

        // Sort the entries based on their values
        Collections.sort(sortedEntries, new Comparator<Map.Entry<T, Double>>() {
            @Override
            public int compare(Map.Entry<T, Double> entry1, Map.Entry<T, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
    }

    public List<Map.Entry<T, Double>> getSortedEntries() {
        return sortedEntries;
    }
}
