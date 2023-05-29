import java.util.ArrayList;

public class Node {
    private ArrayList<Entry> entries;
    private int level;

    public Node() {

    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }
}
