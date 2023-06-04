import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    private final int blockid;
    private final String blockname;
    private int numberOfBlocks;
    private int numberOfRecordsInsideDatafile;
    private ArrayList<Record> records;
    // more thought must be applied here
    private ArrayList<Entry> entries;

    public Block(int blockid, String blockname){
        this.blockid = blockid;
        this.blockname = blockname;
        records = new ArrayList<>();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public int getBlockid() {
        return blockid;
    }

    public String getBlockname() {
        return blockname;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public void setNumberOfBlocks(int numberOfBlocks) {
        this.numberOfBlocks = numberOfBlocks;
    }

    public int getNumberOfRecordsInsideDatafile() {
        return numberOfRecordsInsideDatafile;
    }

    public void setNumberOfRecordsInsideDatafile(int numberOfRecordsInsideDatafile) {
        this.numberOfRecordsInsideDatafile = numberOfRecordsInsideDatafile;
    }

    public boolean addRecordToBlock(Record record){
        return records.add(record);
    }

    public void removeRecordFromBlock(long recordId) {
        for(int i=0;i<records.size();i++) {
            if (records.get(i).getId()==recordId) {
                records.remove(i);
                break;
            }
        }
    }
    public int getNumberOfRecordsInTheBlock(){
        return records.size();
    }

    public Record searchRecordInTheBlock(int recordId){
        Record record = null;
        for(int i=0;i<records.size();i++) {
            if (records.get(i).getId()==recordId) {
                record = records.get(i);
                break;
            }
        }
        return record;
    }
}
