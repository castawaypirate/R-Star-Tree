import java.util.ArrayList;

public class Entry {
    private BoundingBox boundingBox;
    private Node childNode;

    public Entry(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void showEntry() {
        boundingBox.showBoundingBox();
    }

    public Node getChildNode() {
        return childNode;
    }

    public BoundingBox assumingBoundingBox (Entry incomingEntry) {
        ArrayList<Bounds> boundsInEachDimension = new ArrayList<>();
        for (int i = 0; i < boundingBox.getBounds().size(); i++) {
            Bounds bounds = new Bounds();
            Bounds existingBounds = boundingBox.getBounds().get(i);
            Bounds incomingBounds = incomingEntry.getBoundingBox().getBounds().get(i);
            bounds.setLowerBound(Math.min(existingBounds.getLowerBound(), incomingBounds.getLowerBound()));
            bounds.setUpperBound(Math.max(existingBounds.getUpperBound(), incomingBounds.getUpperBound()));
            boundsInEachDimension.add(bounds);
        }
        return new BoundingBox(boundsInEachDimension);
    }
}
