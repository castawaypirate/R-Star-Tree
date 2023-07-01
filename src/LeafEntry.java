public class LeafEntry extends Entry {
    private long recordId;

    public LeafEntry(long recordId, BoundingBox boundingBox) {
        super();
        super.setBoundingBox(boundingBox);
        this.recordId = recordId;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return super.getBoundingBox();
    }

    @Override
    public void showEntry() {
        System.out.println();
        System.out.println("Record ID: " + recordId);
        getBoundingBox().getLowerLeft().showPoint();
    }

    public long getLeafEntryId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public boolean matches(LeafEntry incomingEntry){
        return super.getBoundingBox().identical(incomingEntry.getBoundingBox());
    }
}
