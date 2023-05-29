public class RAsteriskTree {
    private FileManager fileManager;
    private int dimensions;
    public RAsteriskTree(int dimensions){
        this.dimensions=dimensions;
        fileManager = new FileManager(dimensions);
    }

    public void bulkLoading(String CSVfilePath){
        fileManager.writeToDatafile(fileManager.readDataFromCSVFile(CSVfilePath));
    }

    public void insertRecord(){

    }
}
