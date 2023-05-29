import java.util.ArrayList;
import java.util.Arrays;

public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    public RAsteriskTree(int dimensions){
        this.dimensions=dimensions;
        fileManager = new FileManager(dimensions);
    }

    public void bulkLoading(String CSVfilePath){
        // must do or the entries must be sorted, only time will tell
        ArrayList<Record> sortedRecords = fileManager.readDataFromCSVFile(CSVfilePath);
        fileManager.writeToDatafile(sortedRecords);
        insert(new Record(632980450, "632980450",new ArrayList<Double>(Arrays.asList(41.5448493,26.5947027))));
    }

    public void insert(Record record) {
        ArrayList<Bounds> boundsInEachDimension = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            Bounds bounds = new Bounds(record.getCoordinates().get(i), record.getCoordinates().get(i));
            boundsInEachDimension.add(bounds);
        }
        Entry entry = new LeafEntry(record.getId(), new BoundingBox(boundsInEachDimension));
        entry.showEntry();
    }

}
