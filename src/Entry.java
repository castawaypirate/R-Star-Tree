public class Entry {
    public BoundingBox boundingBox;

    public Entry(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void showEntry() {
        boundingBox.showBoundingBox();
    }
}
