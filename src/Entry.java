import java.io.Serializable;
import java.util.ArrayList;

public class Entry implements Serializable{
    private BoundingBox boundingBox;
    // node to which the entry points to
    private Node childNode;
    // node in which the entry is in
    private Node parentNode;

    public Entry() {
    }

    public Entry(Node node) {
        this.childNode = node;
        BoundingBox box = new BoundingBox();
        box.createBoundingBoxOfEntries(node.getEntries());
        this.boundingBox = box;
        node.setParentEntry(this);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void showEntry() {
        boundingBox.showBoundingBox();
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public BoundingBox assumingBoundingBox(Entry incomingEntry) {
        Point lowerLeft = incomingEntry.getBoundingBox().getLowerLeft().createLowerPoint(this.boundingBox.getLowerLeft());
        Point upperRight = incomingEntry.getBoundingBox().getUpperRight().createUpperPoint(this.boundingBox.getUpperRight());
        return new BoundingBox(lowerLeft, upperRight);
    }
}
