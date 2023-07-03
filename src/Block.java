import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    private final int blockid;
    private final String blockname;
    private ArrayList<Integer> blockids;

    public Block(int blockid, String blockname){
        this.blockid = blockid;
        this.blockname = blockname;
        this.blockids = new ArrayList<>();
    }

    public int getBlockid() {
        return blockid;
    }

    public String getBlockname() {
        return blockname;
    }

    public ArrayList<Integer> getBlockids() {
        return blockids;
    }
}
