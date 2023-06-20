import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    private final int blockid;
    private final String blockname;
    private int numberOfRecordsInsideDatafile;
    private ArrayList<Record> records;
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
    public int getNumberOfRecordsInsideBlock(){
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

    public void showRecordsInBlock() {
        System.out.println("--------------------");
        System.out.println("Block ID: " + blockid);
        for (Record record : records) {
            System.out.println("--------------------");
            System.out.println("Record ID: " + record.getId());
            System.out.println("Name: " + record.getName());
            System.out.println("Coordinates: " + record.getCoordinates());
            System.out.println("--------------------");
        }
    }
}
