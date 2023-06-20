import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final static String pathToDatafile = ".\\resources\\datafile.dat";
    private final static String pathToIndexfile = ".\\resources\\indexfile.dat";
    private static final int maxBlockSize = 32*1024;
    private int maxNumberOfRecordsInBlock;
    private ArrayList<Block> blocks;
    private int dimensions;
    public FileManager(int dimensions){
        this.dimensions=dimensions;
        this.blocks=new ArrayList<>();
        maxNumberOfRecordsInBlock = computeMaximumNumberOfRecordsInABlock();
        maxNumberOfRecordsInBlock = 3;
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

    // Returns the index of the block from the ArrayList blocks
    public int searchBlock(int blockId) {
        int index=0;
        for(int i=0;i<blocks.size();i++) {
            if(blocks.get(i).getBlockid()==blockId) {
                index=i;
                break;
            }
        }
        return index;
    }

    public void readDatafile() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToDatafile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            blocks = (ArrayList<Block>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createDatafile() {
        Block block0 = new Block(0, "block0");
        Block block1 = new Block(1, "block1");
        blocks.add(block0);
        blocks.add(block1);
        writeObjectToDatafile(blocks);
    }

    public void writeRecordToDatafile(Record record){
        if(!datafileExists()) {
            System.out.println("Initialize datafile.dat");
            createDatafile();
        }
        readDatafile();
        Block blockToAdd = null;
        for(int i=1;i<blocks.size();i++){
            if(blocks.get(i).getNumberOfRecordsInsideBlock()<maxNumberOfRecordsInBlock){
                blockToAdd = blocks.get(i);
                break;
            }
        }
        if(blockToAdd == null){
            blockToAdd = new Block(blocks.size(), "block"+blocks.size());
            blocks.add(blockToAdd);
        }
        blockToAdd.addRecordToBlock(record);
        writeObjectToDatafile(blocks);
    }

    public void writeToDatafile(ArrayList<Record> records){
        if(datafileExists()) {
            System.out.println("datafile.dat already exists");

        }else {
            Block block0 = new Block(0, "block0");
            // set the number of records inside the datafile
//            block0.setNumberOfRecordsInsideDatafile(block0.getNumberOfRecordsInsideDatafile()+records.size());
//            System.out.println("block0 bytes:" + getBytesOfObject(block0));
            blocks.add(block0);
//            System.out.println("blocks bytes:" + getBytesOfObject(blocks));
//            System.out.println(maxRecordsPerBlock);
            int blockId = 1;
            for(int i=0;i<records.size();i++) {
                Block block = new Block(blockId, "block"+blockId);
                for(int j=0;j<maxNumberOfRecordsInBlock;j++) {
                    records.get(i).setBlockId(blockId);
//                    System.out.println(getBytesOfObject(records.get(i)));
                    block.addRecordToBlock(records.get(i));
                }
//                System.out.println(block.getRecords().size());
//                System.out.println("block"+blockId+" bytes:" + getBytesOfObject(block));
                blocks.add(block);
                blockId++;
            }
//            System.out.println(getBytesOfObject(blocks.get(1)));
//            System.out.println(getBytesOfObject(blocks.get(2)));
//            System.out.println("blocks bytes:" + getBytesOfObject(blocks));
//            System.out.println(getBytesOfObject(records.get(5)));
            writeObjectToDatafile(blocks);
        }
    }

    public void writeIndexfile(Node node) {
        // k = entries ana block (opou ena block = node)
        // node na to grapseis sto indexfile.dat
    }

    // Adds records to block, and then it serializes it to find its length
    // in bytes. When it surpasses maxBlockSize returns number of records
    // that can fit in a block
    public int computeMaximumNumberOfRecordsInABlock(){
        Block block = new Block(0,"testBlock");
        ArrayList<Double> coordinates = new ArrayList<>();
        for(int i=0;i<dimensions;i++){
            coordinates.add(Double.MAX_VALUE);
        }
        Record testRecord = new Record(0, "testRecord", coordinates);
        int maxRecords = 0;
        for(int i=0;i<Integer.MAX_VALUE;i++){
            block.addRecordToBlock(testRecord);
            byte[] blockInBytes;
            try{
                // serialize block
                blockInBytes = serialize(block);
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

    public boolean deleteIndexfile(){
        File f = new File(pathToIndexfile);
        return f.delete();
    }

    public boolean deleteDatafile(){
        File f = new File(pathToDatafile);
        return f.delete();
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
}
