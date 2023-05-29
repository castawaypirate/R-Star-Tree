import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    private final int blockid;
    private final String blockname;
    private ArrayList<Record> records;

    public Block(int blockid, String blockname){
        this.blockid = blockid;
        this.blockname = blockname;
        records = new ArrayList<>();
    }

    public boolean addRecordToBlock(Record record){
        return records.add(record);
    }

    public int getNumberOfRecordsInTheBlock(){
        return records.size();
    }

//    public boolean searchRecordInTheBlock
}
