public class LeafEntry extends Entry {
    public long entryId;

    public LeafEntry(long entryId, BoundingBox boundingBox) {
        super(boundingBox);
        this.entryId = entryId;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return super.getBoundingBox();
    }

    @Override
    public void showEntry() {
        System.out.println("Entry ID:" + entryId);
        super.showEntry();
    }

    public long getEntryId() {
        return entryId;
    }
}
