import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileManager {
    private final static String pathToDatafile = ".\\resources\\datafile.dat";
    private final static String pathToIndexfile = ".\\resources\\indexfile.dat";
    private static final int maxBlockSize = 32*1024;
    private int maxNumberOfRecordsInBlock;
    private int maxNumberOfEntriesInBlock;
    private ArrayList<DataBlock> datafileBlocks;
    private ArrayList<IndexBlock> indexfileBlocks;
    private int dimensions;
    private final static int rootIndexBlockid = 1;
    public FileManager(int dimensions){
        this.dimensions=dimensions;
        this.datafileBlocks=new ArrayList<>();
        this.indexfileBlocks=new ArrayList<>();
//        maxNumberOfRecordsInBlock = computeMaximumNumberOfRecordsInABlock();
//        maxNumberOfEntriesInBlock = computeMaximumNumberOfEntriesInABlock();
        maxNumberOfRecordsInBlock = 4;
        maxNumberOfEntriesInBlock = 4;
    }

    public Node getRoot() {
        if(!indexfileExists()) {
            System.out.println("initialize indexfile.dat");
            createIndexfile();
        }
        readIndexfile();
        for(int i=1;i<indexfileBlocks.size();i++) {
            if(indexfileBlocks.get(i).getNodeOfBlock().getBlockid()==rootIndexBlockid) {
                return indexfileBlocks.get(i).getNodeOfBlock();
            }
        }
        // create root at leaf level
        Node root = new Node(1);
        // create block for root
        IndexBlock rootBlock = new IndexBlock(rootIndexBlockid, "block" + rootIndexBlockid);
        rootBlock.setNodeOfBlock(root);
        indexfileBlocks.add(rootBlock);

        writeObjectToIndexfile(indexfileBlocks);
        return root;
    }

    public int getMaxNumberOfEntriesInBlock() {
        return maxNumberOfEntriesInBlock;
    }

    public Record getRecordFromDatafile(long id){
        Record record = null;
        readDatafile();
        for(DataBlock block: datafileBlocks) {
            record = block.searchRecordInTheBlock(id);
            if(record!=null) {
                break;
            }
        }
        return record;
    }

    public boolean datafileExists(){
        File f = new File(pathToDatafile);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public boolean indexfileExists(){
        File f = new File(pathToIndexfile);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public void createDatafile() {
        DataBlock block0 = new DataBlock(0, "block0");
        DataBlock block1 = new DataBlock(1, "block1");

        // add blockids to the ArrayList in block0 which stores the blockids
        block0.getBlockids().add(0);
        block0.getBlockids().add(1);

        datafileBlocks.add(block0);
        datafileBlocks.add(block1);
        writeObjectToDatafile(datafileBlocks);
    }

    public void createIndexfile() {
        IndexBlock block0 = new IndexBlock(0, "block0");

        // add blockids to the ArrayList in block0 which stores the blockids
        block0.getBlockids().add(0);

        indexfileBlocks.add(block0);
        writeObjectToIndexfile(indexfileBlocks);
    }

    public boolean deleteIndexfile(){
        File f = new File(pathToIndexfile);
        return f.delete();
    }

    public boolean deleteDatafile(){
        File f = new File(pathToDatafile);
        return f.delete();
    }

    public void readDatafile() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToDatafile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            datafileBlocks = (ArrayList<DataBlock>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readIndexfile() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToIndexfile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            indexfileBlocks = (ArrayList<IndexBlock>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readIndexfileTest() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToIndexfile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            indexfileBlocks = (ArrayList<IndexBlock>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(IndexBlock block : indexfileBlocks) {
            System.out.println();
            System.out.println("-----------------------------------");
            System.out.println("IndexBlock ID: " + block.getBlockid());
            System.out.println("-----------------------------------");
            if(block.getNodeOfBlock()!=null){
                block.getNodeOfBlock().showEntries();
                System.out.println("-----------------------------------");
            }
        }
    }

    public void readDatafileTest() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToDatafile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            datafileBlocks = (ArrayList<DataBlock>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(DataBlock block : datafileBlocks) {
            System.out.println();
            if(!block.getRecords().isEmpty()){
                block.showRecordsInBlock();
                System.out.println("-----------------------------------");
            }
        }
    }

    public int getUniqueId(ArrayList<Integer> ids){
        HashSet<Integer> idSet = new HashSet<Integer>(ids);
        int uniqueId = 2;
        while (idSet.contains(uniqueId)) {
            uniqueId++;
        }
        return uniqueId;
    }

    public void createUpdateIndexBlock(Node node){
        IndexBlock blockToAdd = null;
        for(IndexBlock indexBlock : indexfileBlocks){
            if(indexBlock.getBlockid()==node.getBlockid() && indexBlock.getBlockid()!=0){
                blockToAdd = indexBlock;
                break;
            }
        }
        if(blockToAdd == null){
            int indexBlockId = getUniqueId(indexfileBlocks.get(0).getBlockids());
            blockToAdd = new IndexBlock(indexBlockId, "block"+indexBlockId);
            indexfileBlocks.add(blockToAdd);
            // add indexBlockId to blockids of block0
            indexfileBlocks.get(0).getBlockids().add(indexBlockId);
        }
        blockToAdd.setNodeOfBlock(node);
    }

    public void writeToIndexfile(){
        writeObjectToIndexfile(indexfileBlocks);
    }

    public void writeRecordToDatafile(Record record){
        if(!datafileExists()) {
            System.out.println("initialize datafile.dat");
            createDatafile();
        }
        readDatafile();
        DataBlock blockToAdd = null;
        for(int i=1;i<datafileBlocks.size();i++){
            if(datafileBlocks.get(i).getNumberOfRecordsInsideBlock()<maxNumberOfRecordsInBlock){
                blockToAdd = datafileBlocks.get(i);
                break;
            }
        }
        if(blockToAdd == null){
            int dataBlockId = getUniqueId(datafileBlocks.get(0).getBlockids());
            blockToAdd = new DataBlock(dataBlockId, "block"+dataBlockId);
            datafileBlocks.add(blockToAdd);
            // add dataBlockId to blockids of block0
            datafileBlocks.get(0).getBlockids().add(dataBlockId);
        }
        blockToAdd.addRecordToBlock(record);
        writeObjectToDatafile(datafileBlocks);
    }

    public void writeObjectToDatafile(Object object) {
        try {
            FileOutputStream fileOut = new FileOutputStream(pathToDatafile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeObjectToIndexfile(Object object) {
        try {
            FileOutputStream fileOut = new FileOutputStream(pathToIndexfile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteRecordFromDatafile(long recordId){
        readDatafile();
        int blockIndex = 0;
        Record recordToDelete = null;
        for(int i=1;i<datafileBlocks.size();i++){
            for(Record record : datafileBlocks.get(i).getRecords()){
                if(record.getId()==recordId){
                    blockIndex=i;
                    recordToDelete = record;
                    break;
                }
            }
        }
        datafileBlocks.get(blockIndex).getRecords().remove(recordToDelete);
        writeObjectToDatafile(datafileBlocks);
    }

    public void deleteIndexBlockWithBlockid(int blockid){
        int index = 0;
        for(int i=1;i<indexfileBlocks.size();i++){
            if(indexfileBlocks.get(i).getBlockid()==blockid){
                index = i;
                break;
            }
        }
        indexfileBlocks.remove(index);
        indexfileBlocks.get(0).getBlockids().remove(Integer.valueOf(blockid));
    }

    public int getBytesOfObject(Object object){
        byte[] blockInBytes;
        try{
            blockInBytes = serialize(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return blockInBytes.length;
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    // Adds records to block, and then it serializes it to find its length
    // in bytes. When it surpasses maxBlockSize returns number of records
    // that can fit in a block
    public int computeMaximumNumberOfRecordsInABlock(){
        DataBlock testBlock = new DataBlock(0,"testBlock");
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=0;i<dimensions;i++){
            coordinates.add(Double.MAX_VALUE);
        }
        Record testRecord = new Record(0, "testRecord", coordinates);
        int maxRecords = 0;
        for(int i=0;i<Integer.MAX_VALUE;i++){
            testBlock.addRecordToBlock(testRecord);
            byte[] blockInBytes;
            try{
                // serialize block
                blockInBytes = serialize(testBlock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(blockInBytes.length>maxBlockSize){
                maxRecords=i;
                break;
            }
        }
        return maxRecords;
    }

    // Adds entries to block, and then it serializes it to find its length
    // in bytes. When it surpasses maxBlockSize returns number of entries
    // that can fit in a block
    public int computeMaximumNumberOfEntriesInABlock(){
        IndexBlock testBlock = new IndexBlock(0,"testBlock");
        Node testNode = new Node(0);
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=0;i<dimensions;i++) {
            coordinates.add(Double.MAX_VALUE);
        }
        LeafEntry testEntry = new LeafEntry(0, new BoundingBox(new Point(coordinates), new Point(coordinates)));
        int maxEntries = 0;
        for(int i=0;i<Integer.MAX_VALUE;i++){
            testNode.addEntry(testEntry);
            testBlock.setNodeOfBlock(testNode);
            byte[] blockInBytes;
            try{
                // serialize block
                blockInBytes = serialize(testBlock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(blockInBytes.length>maxBlockSize){
                maxEntries=i;
                break;
            }
        }
        return maxEntries;
    }

    public List<File> getCSVFiles(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> csvFiles = null;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (files != null) {
                csvFiles = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        csvFiles.add(file);
                    }
                }
            }
        } else {
            System.out.println("invalid directory");
        }
        return csvFiles;
    }

    public ArrayList<Record> readDataFromCSVFile(String CSVFilePath){
        ArrayList<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                ArrayList<Double> coordinates = new ArrayList<>();
                for(int i=1;i<row.length;i++){
                    coordinates.add(Double.parseDouble(row[i]));
                }
                records.add(new Record(Long.parseLong(row[0]), row[0], coordinates));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public void showRecords(ArrayList<Record> records){
        for(Record record : records){
            System.out.println("ID:" + record.getId() + ", Name:" + record.getName() + ", Coordinates:" + record.getCoordinates());
        }
    }
}
