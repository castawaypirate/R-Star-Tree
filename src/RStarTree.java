import java.util.*;

public class RStarTree {
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
    private int reinsert_p;
    public RStarTree(int dimensions, FileManager fileManager){
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
        if(!root.getEntries().isEmpty()) {
            System.out.println();
            showNode(root, "");
            System.out.println();
        }
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
                    System.out.println(indent + "Record ID: " + leafEntry.getLeafEntryId() + ", Coordinates: " + entry.getBoundingBox().getLowerLeft().getCoordinates());
                } else {
                    Node childNode = entry.getChildNode();
                    System.out.println(indent + "Index Node: ");
                    System.out.println(indent + "IndexBlock ID: " + childNode.getBlockid());
                    showNode(childNode, indent);
                }
            }
        }
    }

    public void oneByOneTreeBuild(String CSVfilePath){
        fileManager.readIndexfile();
        HashMap<Long, Record> records = fileManager.readDataFromCSVFile(CSVfilePath);
        int count = 0;
        for(Record record : records.values()) {
            insert(new LeafEntry(record.getId(), new BoundingBox(new Point(record.getCoordinates()), new Point(record.getCoordinates()))), root, leafLevel);
            System.out.println(++count);
        }
        fileManager.writeToIndexfile();
        fileManager.writeRecordsToDatafile(records);
    }

    public void bulkLoading(String CSVfilePath){
        // read records from csv file into a hashmap
        // where key is the id and value is the record
        HashMap<Long, Record> records = fileManager.readDataFromCSVFile(CSVfilePath);
        ArrayList<Node> nodes = new ArrayList<>();
        // Map P(records) in the rank space
        ArrayList<Record> rankedRecords = rankRecords(records);
        HashMap<Record, Integer> zOrderMap = new HashMap<>();
        for(Record rankedRecord : rankedRecords) {
            zOrderMap.put(rankedRecord, computeZOrderValue(rankedRecord));
        }
        // Sort P(rankedRecords) in ascending order of the Z-order values
        List<Map.Entry<Record, Integer>> zOrderList = new ArrayList<>(zOrderMap.entrySet());
        zOrderList.sort(Map.Entry.comparingByValue());
        // add create leaf entries and add them to nodes
        int count = 0;
        ArrayList<Node> Q = new ArrayList<>();
        Q.add(new Node(leafLevel));
        for (Map.Entry<Record, Integer> rankedRecordEntry : zOrderList) {
            // for every M entries create a new node to add entries
            if(count==M-1) {
                Q.add(new Node(leafLevel));
                count=0;
            }
            // find inside the hashmap the corresponding record using the id of the ranked/sorted records
            Q.get(Q.size()-1)
                    .addEntry(new LeafEntry(records.get(rankedRecordEntry.getKey().getId()).getId(),
                              new BoundingBox(new Point(records.get(rankedRecordEntry.getKey().getId()).getCoordinates()),
                                              new Point(records.get(rankedRecordEntry.getKey().getId()).getCoordinates()))));
            count++;
        }

        nodes.addAll(Q);
        while (Q.size() > 1) {
            List<Node> dequeuedNodes = new ArrayList<>();
            int level = -1;
            // dequeue the first M nodes
            for (int i = 0; i < M - 1; i++) {
                if (!(i>=Q.size())) {
                    dequeuedNodes.add(Q.get(i));
                    if (level == -1) {
                        level = Q.get(i).getLevel();
                    }
                } else {
                    break;
                }
            }
            // remove nodes from Q
            for(Node node : dequeuedNodes){
                Q.remove(node);
            }
            // create new node
            Node newNode = new Node(level + 1);
            for (Node node : dequeuedNodes) {
                // create entries for each node and add them
                // to the new node of the upper level
                Entry entry = new Entry(node);
                newNode.addEntry(entry);
            }
            // add new node to Q
            Q.add(newNode);
            nodes.add(newNode);
        }
        // set root
        Q.get(Q.size()-1).setBlockid(1);
        root = Q.get(Q.size()-1);
        rootLevel = root.getLevel();
        // write to datafile and indexfile
        fileManager.writeRecordsToDatafile(records);
        fileManager.writeNodesToIndexfile(nodes);
    }

    public int computeZOrderValue(Record record) {
        ArrayList<int[]> binary = new ArrayList<>();
        // covert the coordinate of each dimension to 32-bit integer
        for (int i = 0; i < dimensions; i++) {
            binary.add(convertToBinaryArray(record.getCoordinates().get(i).intValue()));
        }
        int[] zOrderValue = new int[32 * dimensions];
        int index = 0;
        // iterate over each bit position
        for (int bitPosition = 0; bitPosition < 32; bitPosition++) {
            // iterate over each dimension
            for (int dim = 0; dim < dimensions; dim++) {
                int[] binaryArray = binary.get(dim);
                if (bitPosition < binaryArray.length) {
                    // get the bit value at the current bit position
                    int bitValue = binaryArray[bitPosition];
                    // set the corresponding bit in the zOrderValue array
                    zOrderValue[index++] = bitValue;
                }
            }
        }
        int zOrderInteger = 0;
        for (int i = 0; i < zOrderValue.length; i++) {
            zOrderInteger = (zOrderInteger << 1) | zOrderValue[i];
        }
        return zOrderInteger;
    }

    public int[] convertToBinaryArray(int number) {
        int[] binaryArray = new int[32]; // assuming a 32-bit integer
        for (int i = 31; i >= 0; i--) {
            binaryArray[i] = number & 1; // get the least significant bit
            number >>= 1; // right shift the number by 1 bit
        }
        return binaryArray;
    }


    public ArrayList<Record> rankRecords(HashMap<Long, Record> records) {
        // create new list for storing the ranked records
        ArrayList<Record> rankRecords = new ArrayList<>(records.size());
        for(Record record : records.values()) {
            rankRecords.add(new Record(record.getId(), record.getName(), dimensions));
        }
        int recordCount = rankRecords.size();
        // preallocate memory for the rankings array
        int[][] rankings = new int[recordCount][dimensions];
        // create a map to store record indices for efficient lookup
        HashMap<Long, Integer> recordIndexMap = new HashMap<>(recordCount);
        // fill the record index map
        int count = 0;
        for(Record record : records.values()) {
            recordIndexMap.put(record.getId(), count);
            count++;
        }
        // iterate over each dimension
        for (int i = 0; i < dimensions; i++) {
            // create a map to store rankings for the current dimension
            HashMap<Long, Double> dimensionRank = new HashMap<>(recordCount);
            // populate the dimensionRank map with record IDs and their respective coordinate values
            for(Record record : records.values()) {
                dimensionRank.put(record.getId(), record.getCoordinates().get(i));
            }
            // convert the dimensionRank map to a list of entries and sort it based on coordinate values
            List<Map.Entry<Long, Double>> entryList = new ArrayList<>(dimensionRank.entrySet());
            entryList.sort(Map.Entry.comparingByValue());
            int rank = 0;
            double previousValue = Double.NEGATIVE_INFINITY;
            // iterate over the sorted entry list and assign ranks to records
            for (Map.Entry<Long, Double> entry : entryList) {
                double currentValue = entry.getValue();
                if (currentValue != previousValue) {
                    previousValue = currentValue;
                }
                int recordIndex = recordIndexMap.get(entry.getKey());
                rankings[recordIndex][i] = rank;
                rank++;
            }
            // update the coordinate values of the records in the rankRecords list
            for (Record record : rankRecords) {
                int recordIndex = recordIndexMap.get(record.getId());
                record.setCoordinateInDimension(i, rankings[recordIndex][i]);
            }
        }
        return rankRecords;
    }

    // Takes a record as a parameter, makes a leaf entry out of it
    // and inserts in to the tree
    public void insertData(Record record) {
        // ID1 Invoke Insert starting with the leaf level as a
        // parameter, to insert a new data rectangle
        // CS1 Set N to be the root (as we pass the root as the node to add)
        fileManager.readIndexfile();
        insert(new LeafEntry(record.getId(), new BoundingBox(new Point(record.getCoordinates()), new Point(record.getCoordinates()))), root, leafLevel);
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
            // sort entries by lower, then by upper value and put them in two ArrayLists
            ArrayList<Entry> sortedByLowerBound = node.sortEntriesByBoundingBoxValue(dim, false);
            ArrayList<Entry> sortedByUpperBound = node.sortEntriesByBoundingBoxValue(dim, true);

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

    // Algorithm Delete
    public boolean delete(LeafEntry leafEntry){
        fileManager.readIndexfile();
        // D1 [Find node containing record] Invoke FindLeaf to locate
        // the leaf of L containing E(leafEntry). Stop if the record
        // was not found.
        ArrayList<LeafEntry> listOfLeafEntries = findLeaf(leafEntry, root, new ArrayList<>());
        if(listOfLeafEntries.isEmpty()) {
            System.out.println("There was no record with these coordinates");
            return false;
        }
        // the first record found with the given coordinate is the one to be removed
        LeafEntry leafEntryToRemove = listOfLeafEntries.get(0);
        long id = leafEntryToRemove.getLeafEntryId();
        Node node = listOfLeafEntries.get(0).getParentNode();
        // D2 [Delete record] Remove E(leafEntry) From L(node)
        if(node.removeEntry(leafEntryToRemove)){
            fileManager.createUpdateIndexBlock(node);
            // D3 [Propagate changes] Invoke CondenseTree, passing L(node).
            condenseTree(node);
            // D4 [Shorten tree] If the root node has only one child after the tree has
            // been adjusted, make the child the new root.
            if(root.getEntries().size()==1){
                rootLevel--;
                int childBlockid = root.getEntries().get(0).getChildNode().getBlockid();
                Node newRoot = root.getEntries().get(0).getChildNode();
                newRoot.setBlockid(root.getBlockid());
                newRoot.setParentEntry(null);
                root = newRoot;
                fileManager.deleteIndexBlockWithBlockid(childBlockid);
                fileManager.createUpdateIndexBlock(root);
            }
            fileManager.writeToIndexfile();
            fileManager.deleteRecordFromDatafile(id);
            return true;
        }
        System.out.println("The records was not removed successfully");
        return false;
    }

    // Algorithm CondenseTree
    public void condenseTree(Node node){
        // CT1 [Initialize] Set N=L. Set Q, the set of eliminated
        // nodes, to be empty.
       ArrayList<Node> qNodes = new ArrayList<>();
        // CT2 [Find parent entry] If N is the root, go to CT6. Otherwise,
        // let P be the parent of N, and let EN be N's entry in P.
        while(node.getLevel() != rootLevel){
            Node pNode = node.getParentEntry().getParentNode();
            // CT3 [Eliminate under-full node] If N has fewer than m entries,
            // delete EN from P and add N to set Q.
            if(node.getEntries().size() < m){
                pNode.removeEntry(node.getParentEntry());
                fileManager.deleteIndexBlockWithBlockid(node.getBlockid());
                fileManager.createUpdateIndexBlock(pNode);
                qNodes.add(node);
            } else {
                // CT4 [Adjust covering rectangle] If N has not been eliminated,
                // adjust ENI to tightly contain all entries in N.
                node.adjustBoundingBoxToFitEntry(null);
                fileManager.createUpdateIndexBlock(node);
            }
            // CT5 [Move up one level in tree] Set N=P and repeat from CT2.
            node = pNode;
        }
        // CT6 [Re-insert orphaned entries] Re-insert all entries of nodes in set Q.
        // Entries from eliminated leaf nodes are re-inserted in tree leaves as
        // described in Algorithm Insert, but entries from higher-level nodes must
        // be placed higher in the tree, so that leaves of their dependent subtrees
        // will be on the same level as leaves of the main tree.
        for(int i=qNodes.size()-1;i>=0;i--){
            for(Entry e : qNodes.get(i).getEntries()){
                insert(e, root, qNodes.get(i).getLevel());
            }
        }
    }

    // Algorithm FindLeaf
    public ArrayList<LeafEntry> findLeaf(LeafEntry leafEntry, Node node, ArrayList<LeafEntry> leafEntries) {
        // FL1 [Search subtrees] If T(node) is not a leaf check each entry F in T
        // if F.I overlaps E.I (leafEntry.BoundingBox). For each such entry
        // invoke FindLeaf on the tree whose root is pointed to by F.p until
        // E is found or all entries have been checked.
        if(node.getLevel() != leafLevel) {
            for(Entry E : node.getEntries()) {
                if(E.getBoundingBox().overlap(leafEntry.getBoundingBox())) {
                    leafEntries = findLeaf(leafEntry, E.getChildNode(), leafEntries);
                }
            }
        }
        // FL2 [Search leaf node for record] If T is a leaf, check each entry to
        // see if it matches E. If E is found return T.
        else {
            for(Entry E : node.getEntries()) {
                if(E.getBoundingBox().identical(leafEntry.getBoundingBox())) {
                    leafEntries.add((LeafEntry) E);
                }
            }
        }
        return leafEntries;
    }

    public boolean deleteWithID(long id){
        return false;
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

    // Algorithm Branch and Bound Skyline
    public void branchAndBoundSkyline(){
        // list of skyline points
        ArrayList<LeafEntry> skyline = new ArrayList<>();
        MinMaxHeap<Entry> minHeap = new MinMaxHeap<>(false);
        // initialize origin
        ArrayList<Double> originCoordinates = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            originCoordinates.add(0.0);
        }
        Point origin = new Point(originCoordinates);
        // insert all entries of root in the heap
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
        // sort skyline ArrayList according to dim dimension
        int dim = 1;
        Collections.sort(skyline, new Comparator<LeafEntry>() {
            @Override
            public int compare(LeafEntry entry1, LeafEntry entry2) {
                double x1 = entry1.getBoundingBox().getLowerLeft().getCoordinates().get(dim-1); // Assuming x coordinate is at index 0
                double x2 = entry2.getBoundingBox().getLowerLeft().getCoordinates().get(dim-1); // Assuming x coordinate is at index 0
                return Double.compare(x1, x2);
            }
        });
        System.out.println(skyline.size());
        for(LeafEntry entry : skyline) {
            entry.showEntry();
        }
    }

    // Algorithm K Nearest Neighbors
    public void knnQuery(Node node, Point point, int k, MinMaxHeap<Entry> nearest){
        // initialize nearest
        if (nearest == null) {
            nearest = new MinMaxHeap<>(true, k);
        }
        // add all root entries to active branch
        MinMaxHeap<Entry> activeBranchList = new MinMaxHeap<>(false);
        for(Entry entry : node.getEntries()) {
            activeBranchList.addObject(entry, point.computeMinDistanceFromBoundingBox(entry.getBoundingBox()));
        }
        // while active branch list is not empty
        while(!activeBranchList.getSortedEntries().isEmpty()) {
            // remove top entry
            Entry entryToCheck = activeBranchList.getFirstElement().getKey();
            double distance = activeBranchList.getFirstElement().getValue();
            activeBranchList.getSortedEntries().remove(0);
            // if nearest is empty our guess for the nearest neighbor distances is infinity
            double neighborDistance = Double.MAX_VALUE;
            if (!nearest.getSortedEntries().isEmpty()){
                // in case nearest hasn't found k neighbors yet
                if (!(nearest.getSortedEntries().size()<k)){
                    neighborDistance = nearest.getFirstElement().getValue();
                }
            }
            if(distance < neighborDistance) {
                // if entryToCheck is not a leaf entry
                if(!(entryToCheck instanceof LeafEntry)) {
                    for(Entry childEntry : entryToCheck.getChildNode().getEntries()) {
                        // add all child entries of the child node to active branch list
                        activeBranchList.addObject(childEntry, point.computeMinDistanceFromBoundingBox(childEntry.getBoundingBox()));
                    }
                } else { // entryToCheck if a leaf entry add it to nearest
                    nearest.addObject(entryToCheck, point.computeMinDistanceFromBoundingBox(entryToCheck.getBoundingBox()));
                }
            }
        }
        System.out.println("Nearest neighbors for k=" + k + ": ");
        for (Map.Entry<Entry, Double> entry : nearest.getSortedEntries()){
            entry.getKey().showEntry();
            System.out.println("Distance: " + entry.getValue());
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

class MinMaxHeap<T> {
    private List<Map.Entry<T, Double>> sortedEntries;
    private boolean isMaxHeap;
    private int maxEntries;

    public MinMaxHeap(boolean isMaxHeap) {
        this(isMaxHeap, 0);
    }

    public MinMaxHeap(boolean isMaxHeap, int maxEntries) {
        this.isMaxHeap = isMaxHeap;
        this.maxEntries = maxEntries;
        sortedEntries = new ArrayList<>();
    }

    public void addObject(T object, double value) {
        boolean insert = false;
        if (maxEntries > 0) {
            if (sortedEntries.size() >= maxEntries) {
                if (value < sortedEntries.get(0).getValue()) {
                    sortedEntries.remove(0);
                    insert = true;
                }
            } else {
                insert = true;
            }
        } else {
            insert = true;
        }
        if (insert) {
            Map.Entry<T, Double> newEntry = new AbstractMap.SimpleEntry<>(object, value);
            sortedEntries.add(newEntry);

            // sort the entries based on their values and heap type
            Collections.sort(sortedEntries, new Comparator<Map.Entry<T, Double>>() {
                @Override
                public int compare(Map.Entry<T, Double> entry1, Map.Entry<T, Double> entry2) {
                    int result = entry1.getValue().compareTo(entry2.getValue());
                    return isMaxHeap ? -result : result;
                }
            });
        }
    }

    public List<Map.Entry<T, Double>> getSortedEntries() {
        return sortedEntries;
    }

    public Map.Entry<T, Double> getFirstElement() {
        if (sortedEntries.isEmpty()) {
            return null; // handle empty heap case
        }
        return sortedEntries.get(0);
    }

    public Map.Entry<T, Double> getLastElement() {
        if (sortedEntries.isEmpty()) {
            return null; // handle empty heap case
        }
        return sortedEntries.get(sortedEntries.size() - 1);
    }
}