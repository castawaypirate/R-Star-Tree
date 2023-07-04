import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearSearch {
    private FileManager fileManager;
    ArrayList<DataBlock> blocks = new ArrayList<>();
    public LinearSearch(FileManager fileManager){
        this.fileManager = fileManager;
    }

    public void search(BoundingBox searchBox){
        long startTime = System.currentTimeMillis();
        fileManager.readDatafile();
        blocks = fileManager.getDatafileBlocks();
        ArrayList<Record> result = new ArrayList<>();
        for(DataBlock block : blocks) {
            for(Record record : block.getRecords()) {
                BoundingBox recordBox = new BoundingBox(new Point(record.getCoordinates()), new Point(record.getCoordinates()));
                if(recordBox.overlap(searchBox)) {
                    result.add(record);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Range query with linear search result: ");
        System.out.println(result.size());
        fileManager.showRecords(result);
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    public void knnQuery(Point point, int k){
        long startTime = System.currentTimeMillis();
        fileManager.readDatafile();
        blocks = fileManager.getDatafileBlocks();
        HashMap<Record, Double> map = new HashMap<>();
        for(DataBlock block : blocks) {
            for(Record record : block.getRecords()) {
                map.put(record, point.computeDistanceFromPoint(new Point(record.getCoordinates())));
            }
        }
        List<Map.Entry<Record, Double>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        List<Map.Entry<Record, Double>> kNearestNeighbors = entryList.subList(0, Math.min(k, entryList.size()));
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("K Nearest Neighbors result for k=" + k + ": ");
//        for (Map.Entry<Record, Double> entry : kNearestNeighbors) {
//            Record record = entry.getKey();
//            double distance = entry.getValue();
//            System.out.println("Record ID: " + record.getId() + ", Distance: " + distance);
//        }
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }
}
