import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Node {
    private ArrayList<Entry> entries;
    private int level;
    private boolean isRoot;

    public Node(int level, boolean isRoot) {
        this.entries = new ArrayList<>();
        this.level = level;
        this.isRoot = isRoot;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void changeRootStatus(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean hasLeaves () {
        if (entries.size()>0) {
            if (entries.get(entries.size()-1) instanceof LeafEntry) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Entry> sortEntriesByAreaEnlargement (Entry incomingEntry) {
        ArrayList<Entry> sortedEntries = new ArrayList<>();

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

        return sortedEntries;
    }
}
