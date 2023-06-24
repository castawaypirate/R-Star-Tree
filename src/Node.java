import java.io.Serializable;
import java.util.*;

public class Node implements Serializable{
    private ArrayList<Entry> entries;
    private int level;
    private Entry parentEntry;
    private int blockid;

    public Node(int level) {
        this.entries = new ArrayList<>();
        this.level = level;
    }

    public String getBoundingBoxInString(int dimensions) {
        BoundingBox boundingBox = getNodeBoundingBox();
        StringBuilder boundingBoxString = new StringBuilder("(");
        for (int i = 0; i < dimensions; i++) {
            boundingBoxString.append("Dimension ").append(i + 1)
                    .append(", ").append(boundingBox.getBounds().get(i).getUpperBound())
                    .append(": ").append(boundingBox.getBounds().get(i).getLowerBound());
            if (i < dimensions - 1) {
                boundingBoxString.append(", ");
            }
        }
        boundingBoxString.append(")");
        return boundingBoxString.toString();
    }

    public String getBoundingBoxInStringPoint(int dimensions) {
        BoundingBox boundingBox = getNodeBoundingBox();
        StringBuilder boundingBoxString = new StringBuilder();
        boundingBoxString.append("Lower Left Point: (");
        for (int i = 0; i < dimensions; i++) {
            boundingBoxString.append(boundingBox.getLowerLeft().getCoordinates().get(i));
            if (i < dimensions - 1) {
                boundingBoxString.append(", ");
            }
        }
        boundingBoxString.append("), Upper Right Point: (");
        for (int i = 0; i < dimensions; i++) {
            boundingBoxString.append(boundingBox.getUpperRight().getCoordinates().get(i));
            if (i < dimensions - 1) {
                boundingBoxString.append(", ");
            }
        }
        boundingBoxString.append(")");
        return boundingBoxString.toString();
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addEntry(Entry entry) {
        entry.setParentNode(this);
        entries.add(entry);
    }

    public int getBlockid(){
        return blockid;
    }

    public void setBlockid(int blockid){
        this.blockid=blockid;
    }
    public void showEntries() {
        for(int i=0; i<entries.size(); i++) {
            System.out.println("Entry in index " + i +":");
            entries.get(i).showEntry();
        }
    }

    public boolean hasLeaves() {
        if (entries.size()>0) {
            if (entries.get(entries.size()-1) instanceof LeafEntry) {
                return true;
            }
        }
        return false;
    }

    public Entry getParentEntry() {
        return parentEntry;
    }

    public void setParentEntry(Entry parentEntry) {
        this.parentEntry = parentEntry;
    }

    public BoundingBox getNodeBoundingBox () {
        if (parentEntry == null) {
            // root has no parent entry, so we have to
            // create its bounding box
            BoundingBox rootBoundingBox = new BoundingBox();
            rootBoundingBox.createBoundingBoxOfEntriesPoint(entries);
            return rootBoundingBox;
        }
        // for all other nodes assign and return the bounding
        // box of the entry which points to this node
        return parentEntry.getBoundingBox();
    }

    public void adjustBoundingBoxToFitEntry(Entry incomingEntry) {
        // exclude root case
        if (parentEntry != null) {
            ArrayList<Entry> tempEntries = new ArrayList<>();
            tempEntries.addAll(entries);
            tempEntries.add(incomingEntry);
            BoundingBox adjustedBoundingBox = new BoundingBox();
            adjustedBoundingBox.createBoundingBoxOfEntries(tempEntries);
            parentEntry.setBoundingBox(adjustedBoundingBox);
        }
    }

    public Entry getEntryWithTheLeastAreaEnlargement(Entry incomingEntry, ArrayList<Entry> entriesToSort) {
        // compute and store the areas of different bounding boxes
        HashMap<Integer, Double> areasOfDifferentBoundingBoxes = new HashMap<>();
        for (int i = 0; i < entriesToSort.size(); i++) {
            BoundingBox assumedBoundingBox = entriesToSort.get(i).assumingBoundingBox(incomingEntry);
            Double area = assumedBoundingBox.computeArea();
            areasOfDifferentBoundingBoxes.put(i, area);
        }

        // convert the HashMap to a list of entries
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(areasOfDifferentBoundingBoxes.entrySet());
        // sort the list based on the values (areas)
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });

        // find the smallest area of the assuming bounding boxes (ties)
        Double smallestAssumingBoundingBoxArea = entryList.get(0).getValue();

        // remove entries with areas other than the smallest area
        Iterator<Map.Entry<Integer, Double>> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestAssumingBoundingBoxArea)) {
                iterator.remove();
            }
        }

        // CS2 Resolve ties by choosing the entry with the rectangle of the smallest area
        Double smallestArea = Double.MAX_VALUE;
        Entry sortedEntry = null;
        for (Map.Entry<Integer, Double> entry : entryList) {
            if (entriesToSort.get(entry.getKey()).getBoundingBox().computeArea()<smallestArea) {
                sortedEntry = entriesToSort.get(entry.getKey());
            }
        }
        return sortedEntry;
    }

    public Entry getEntryWithTheLeastOverlapEnlargement(Entry incomingEntry) {
        // compute and store the overlaps of different bounding boxes
        HashMap<Integer, Double> overlapsOfDifferentBoundingBoxes = new HashMap<>();
        for (int i=0; i<entries.size(); i++) {
            BoundingBox assumingBoundingBox = entries.get(i).assumingBoundingBox(incomingEntry);
            double overlap = 0.0;
            for (int j=0; j<entries.size(); j++) {
                if (j != i) {
                    overlap += entries.get(j).getBoundingBox().computeOverlap(assumingBoundingBox);
                }
            }
            overlapsOfDifferentBoundingBoxes.put(i, overlap);
        }

        // convert the HashMap to a list of entries
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(overlapsOfDifferentBoundingBoxes.entrySet());
        // sort the list based on the values (areas)
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });


        // CS2 Resolve ties by choosing the leaf/entry
        // whose rectangle needs the least area enlargement, then
        // the leaf with the rectangle of smallest area

        // find the smallest overlap (ties)
        Double smallestOverlap = entryList.get(0).getValue();

        // remove entries with overlaps other than the smallest overlap
        Iterator<Map.Entry<Integer, Double>> iterator = entryList.iterator();
        ArrayList<Entry> sortedEntries = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestOverlap)) {
                iterator.remove();
            }
            sortedEntries.add(entries.get(entry.getKey()));
        }

        // resolve ties by calling getEntryWithTheLeastAreaEnlargement
        if (sortedEntries.size()>1) {
            return getEntryWithTheLeastAreaEnlargement(incomingEntry, sortedEntries);
        }
        return sortedEntries.get(0);
    }

    public void sortEntriesByAreaEnlargement (Entry incomingEntry) {
        // compute and store the areas of different bounding boxes
        ArrayList<Double> areasOfDifferentBoundingBoxes = new ArrayList<>();
        for (Entry entry : entries) {
            BoundingBox assumedBoundingBox = entry.assumingBoundingBox(incomingEntry);
            double area = assumedBoundingBox.computeArea();
            areasOfDifferentBoundingBoxes.add(area);
        }

        // sort the entries based on the areas of their assumed bounding boxes
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry1, Entry entry2) {
                int index1 = entries.indexOf(entry1);
                int index2 = entries.indexOf(entry2);
                double area1 = areasOfDifferentBoundingBoxes.get(index1);
                double area2 = areasOfDifferentBoundingBoxes.get(index2);
                return Double.compare(area1, area2);
            }
        });
    }

    public ArrayList<Entry> sortEntriesByBound(int dimension, boolean upperBound) {
        HashMap<Entry, Double> mapOfEntriesAndBounds = new HashMap<>();
        for (Entry entry : entries) {
            if (upperBound) {
                mapOfEntriesAndBounds.put(entry, entry.getBoundingBox().getBounds().get(dimension).getUpperBound());
            } else {
                mapOfEntriesAndBounds.put(entry, entry.getBoundingBox().getBounds().get(dimension).getLowerBound());
            }
        }

        // convert the HashMap entries to a list
        List<Map.Entry<Entry, Double>> entryList = new ArrayList<>(mapOfEntriesAndBounds.entrySet());

        // sort the list based on the values
        entryList.sort(Map.Entry.comparingByValue());

        // extract the sorted entries into a new ArrayList
        ArrayList<Entry> sortedEntries = new ArrayList<>();
        for (Map.Entry<Entry, Double> entry : entryList) {
            sortedEntries.add(entry.getKey());
        }
        return sortedEntries;
    }
}
