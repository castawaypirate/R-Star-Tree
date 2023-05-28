public class RAsteriskTree {
    private FileManager fileManager = new FileManager();
    public RAsteriskTree(){

    }

    public void bulkLoading(String CSVfilePath){
        fileManager.writeToDatafile(fileManager.readDataFromCSVFile(CSVfilePath));
    }

    public void insertRecord(){

    }
}
