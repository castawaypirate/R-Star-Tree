import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private final static String pathToDatafile = ".\\resources\\datafile.dat";
    private static final int maxBlockSize = 32*1024;
    private ArrayList<Block> blocks;
    private int dimensions;
    private File datafile;
    public FileManager(int dimensions){
        this.dimensions=dimensions;
        this.blocks=new ArrayList<>();
    }

    public ArrayList<Record> readDataFromCSVFile(String CSVfilePath){
        ArrayList<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVfilePath))) {
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

    public ArrayList<Block> readDatafile() {
        ArrayList<Block> blocksFromDatafile = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(pathToDatafile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            blocksFromDatafile = (ArrayList<Block>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blocksFromDatafile;
    }

    public void writeToDatafile(ArrayList<Record> records){
        if(datafileExists()) {
            System.out.println("datafile.dat already exists");

        }else {
            Block block0 = new Block(0, "block0");
            // set the number of records inside the datafile
            block0.setNumberOfRecordsInsideDatafile(block0.getNumberOfRecordsInsideDatafile()+records.size());
//            System.out.println("block0 bytes:" + getBytesOfObject(block0));
            blocks.add(block0);
//            System.out.println("blocks bytes:" + getBytesOfObject(blocks));
            int maxRecordsPerBlock = numberOfRecordsInABlock(records.get(records.size()-1));
//            System.out.println(maxRecordsPerBlock);
            int blockId = 1;
            for(int i=0;i<records.size();i++) {
                Block block = new Block(blockId, "block"+blockId);
                for(int j=0;j<maxRecordsPerBlock;j++) {
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

    public int numberOfRecordsInABlock(Record record){
        Block block = new Block(1,"block1");
//        System.out.println(getBytesOfObject(record));
        int maxRecords = 0;
        for(int i=0;i<Integer.MAX_VALUE;i++){
//            ArrayList<Double> coordinates = new ArrayList<>();
//            for(int j=1;j<=dimensions;j++){
//                //maybe use random to set the coordinates for each record
//                coordinates.add(0.0);
//            }
//            Record record = new Record(0,"0", coordinates);
//            System.out.println(getBytesOfObject(record));
//            block.addRecordToBlock(new Record(0,"0", coordinates));
            block.addRecordToBlock(record);
            byte[] blockInBytes;
            try{
                blockInBytes = serialize(block);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(blockInBytes.length>maxBlockSize){
//                System.out.println(blockInBytes.length);
                maxRecords=i;
                break;
            }
        }
//        System.out.println(maxRecords);
//        System.out.println(block.getRecords().size());
//        System.out.println(getBytesOfObject(block));
//        block.removeRecordFromBlock(record.getId());
//        block.removeRecordFromBlock(0);
//        writeObjectToDatafile(block);
        return maxRecords;
    }

    public void writeObjectToDatafile(Object object) {
        try {
            FileOutputStream fileOut = new FileOutputStream(pathToDatafile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
            System.out.println("The Object  was successfully written to a file");

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
}
