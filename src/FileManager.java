import java.io.*;
import java.util.ArrayList;
import java.lang.instrument.Instrumentation;


public class FileManager {
    private final static String pathToDatafile = ".\\resources\\datafile.dat";
    private static final int maxBlockSize = 32*1024;
    private int dimensions;
    private File datafile;
    public FileManager(int dimensions){
        this.dimensions=dimensions;
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

    public boolean writeToDatafile(ArrayList<Record> records){
        if(datafileExists()){
            System.out.println("test");
        }else{
            ArrayList<Block> blocks = new ArrayList<>();
            Block block0 = new Block(0, "block0");
            System.out.println(numberOfRecordsInABlock());
        }
        return true;
    }

    public int numberOfRecordsInABlock(){
        Block block = new Block(1,"block1");
        int maxRecords = 0;
        for(int i=0;i<Integer.MAX_VALUE;i++){
            ArrayList<Double> coordinates = new ArrayList<>();
            for(int j=1;j<=dimensions;j++){
                coordinates.add(0.0);
            }
            block.addRecordToBlock(new Record(0,"0", coordinates));
            byte[] blockInBytes;
            try{
                blockInBytes = serialize(block);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(blockInBytes.length>maxBlockSize){
                System.out.println(blockInBytes.length);
                maxRecords=i;
                break;
            }
        }
        writeObjectToDatFile(block);
        return maxRecords;
    }

    public void writeObjectToDatFile(Object object) {

        try {
            FileOutputStream fileOut = new FileOutputStream(pathToDatafile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
