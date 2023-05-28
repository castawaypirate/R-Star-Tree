import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private final static String pathToDatafile = ".\\resources\\datafile.dat";
    private File datafile;
    public FileManager(){

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
            System.out.println("test 2");
        }
        return true;
    }

    public boolean datafileExists(){
        File f = new File(pathToDatafile);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}
