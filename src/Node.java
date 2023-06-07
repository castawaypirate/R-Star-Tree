import java.util.*;

public class Node {
    private ArrayList<Entry> entries;
    private int level;
    private boolean isRoot;
    private Entry parentEntry;

    public Node(int level, boolean isRoot) {
        this.entries = new ArrayList<>();
        this.level = level;
        this.isRoot = isRoot;
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

    public void showEntries() {
        for(int i=0; i<entries.size(); i++) {
            System.out.println("Entry in index " + i +":");
            entries.get(i).showEntry();
        }
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void changeRootStatus(boolean isRoot) {
        this.isRoot = isRoot;
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

    public void adjustBoundingBoxOfParentEntry() {
        BoundingBox adjustedBoundingBox = new BoundingBox();
        adjustedBoundingBox.createBoundingBoxOfEntries(entries);
        parentEntry.setBoundingBox(adjustedBoundingBox);
    }

    public Entry getEntryWithTheLeastAreaEnlargement(Entry incomingEntry, ArrayList<Entry> entriesToSort) {
        // Compute and store the areas of different bounding boxes
        HashMap<Integer, Double> areasOfDifferentBoundingBoxes = new HashMap<>();
        for (int i = 0; i < entriesToSort.size(); i++) {
            BoundingBox assumedBoundingBox = entriesToSort.get(i).assumingBoundingBox(incomingEntry);
            Double area = assumedBoundingBox.computeArea();
            areasOfDifferentBoundingBoxes.put(i, area);
        }

        // Convert the HashMap to a list of entries
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(areasOfDifferentBoundingBoxes.entrySet());
        // Sort the list based on the values (areas)
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });

        // Find the smallest area of the assuming bounding boxes (ties)
        Double smallestAssumingBoundingBoxArea = entryList.get(0).getValue();

        // Remove entries with areas other than the smallest area
        Iterator<Map.Entry<Integer, Double>> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestAssumingBoundingBoxArea)) {
                iterator.remove();
            }
        }

        // Resolve ties by choosing the entry with the bounding box of the smallest area
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
        // Compute and store the overlaps of different bounding boxes
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

        // Convert the HashMap to a list of entries
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(overlapsOfDifferentBoundingBoxes.entrySet());
        // Sort the list based on the values (areas)
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });

        // Find the smallest overlap (ties)
        Double smallestOverlap = entryList.get(0).getValue();

        // Remove entries with overlaps other than the smallest overlap
        Iterator<Map.Entry<Integer, Double>> iterator = entryList.iterator();
        ArrayList<Entry> sortedEntries = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Double> entry = iterator.next();
            if (!entry.getValue().equals(smallestOverlap)) {
                iterator.remove();
            }
            sortedEntries.add(entries.get(entry.getKey()));
        }

        // Resolve ties by calling getEntryWithTheLeastAreaEnlargement
        if (sortedEntries.size()>1) {
            return getEntryWithTheLeastAreaEnlargement(incomingEntry, sortedEntries);
        }
        return sortedEntries.get(0);
    }

    public void sortEntriesByAreaEnlargement (Entry incomingEntry) {
        // Compute and store the areas of different bounding boxes
        ArrayList<Double> areasOfDifferentBoundingBoxes = new ArrayList<>();
        for (Entry entry : entries) {
            BoundingBox assumedBoundingBox = entry.assumingBoundingBox(incomingEntry);
            double area = assumedBoundingBox.computeArea();
            areasOfDifferentBoundingBoxes.add(area);
        }

        // Sort the entries based on the areas of their assumed bounding boxes
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

        // Convert the HashMap entries to a list
        List<Map.Entry<Entry, Double>> entryList = new ArrayList<>(mapOfEntriesAndBounds.entrySet());

        // Sort the list based on the values
        entryList.sort(Map.Entry.comparingByValue());

        // Extract the sorted entries into a new ArrayList
        ArrayList<Entry> sortedEntries = new ArrayList<>();
        for (Map.Entry<Entry, Double> entry : entryList) {
            sortedEntries.add(entry.getKey());
        }

        return sortedEntries;
    }
}
