import java.io.Serializable;
import java.util.ArrayList;

public class Point implements Serializable{
    private ArrayList<Double> coordinates;
    private int pointDimensions;
    public Point(ArrayList<Double> coordinates){
        this.coordinates = coordinates;
        pointDimensions = coordinates.size();
    }
    public Point(){
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
        pointDimensions = coordinates.size();
    }

    public int getPointDimensions() {
        return pointDimensions;
    }

    public Double computeDistanceFromPoint(Point incomingPoint) {
        ArrayList<Double> incomingCoordinates = incomingPoint.getCoordinates();

        if (pointDimensions != incomingCoordinates.size()) {
            throw new IllegalArgumentException("Points must have the same number of dimensions");
        }

        double distance = 0.0;
        for (int i = 0; i < pointDimensions; i++) {
            double diff = coordinates.get(i) - incomingCoordinates.get(i);
            distance += Math.pow(diff, 2);
        }
        return Math.sqrt(distance);
    }

    public Double computeManhattanDistanceFromPoint(Point incomingPoint) {
        ArrayList<Double> incomingCoordinates = incomingPoint.getCoordinates();

        if (pointDimensions != incomingPoint.getPointDimensions()) {
            throw new IllegalArgumentException("Points must have the same number of dimensions");
        }

        double distance = 0.0;
        for (int i = 0; i < pointDimensions; i++) {
            double diff = Math.abs(coordinates.get(i) - incomingCoordinates.get(i));
            distance += diff;
        }
        return distance;
    }

    public void showPoint() {
        System.out.print("(");
        for (int i = 0; i < coordinates.size(); i++) {
            System.out.print(coordinates.get(i));
            if (i < coordinates.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(")");
        System.out.println();
    }
}
