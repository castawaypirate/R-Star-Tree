import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable{
    private final int blockid;
    private final String blockname;

    public Block(int blockid, String blockname){
        this.blockid = blockid;
        this.blockname = blockname;
    }

    public int getBlockid() {
        return blockid;
    }

    public String getBlockname() {
        return blockname;
    }
}
