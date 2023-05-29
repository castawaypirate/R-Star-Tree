import java.io.Serializable;
import java.util.ArrayList;

public class Record implements Serializable{
    private long id;
    private int blockId;
    private String name;
    private ArrayList<Double> coordinates;
    public Record(long id, String name, ArrayList<Double> coordinates){
        this.id=id;
        this.name=name;
        this.coordinates=coordinates;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = new ArrayList<>(coordinates);
    }
}
