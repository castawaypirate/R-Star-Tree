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
        coordinates = new ArrayList<>();
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

    public Point createLowerPoint(Point incomingPoint) {
        ArrayList<Double> incomingCoordinates = incomingPoint.getCoordinates();
        if (pointDimensions != incomingCoordinates.size()) {
            throw new IllegalArgumentException("points must have the same number of dimensions");
        }

        ArrayList<Double> lowerCoordinates = new ArrayList<>(pointDimensions);
        for (int i = 0; i < pointDimensions; i++) {
            double currentCoordinate = coordinates.get(i);
            double incomingCoordinate = incomingCoordinates.get(i);
            double lowerCoordinate = Math.min(currentCoordinate, incomingCoordinate);
            lowerCoordinates.add(lowerCoordinate);
        }
        return new Point(lowerCoordinates);
    }

    public Point createUpperPoint(Point incomingPoint) {
        ArrayList<Double> incomingCoordinates = incomingPoint.getCoordinates();
        if (pointDimensions != incomingCoordinates.size()) {
            throw new IllegalArgumentException("points must have the same number of dimensions");
        }

        ArrayList<Double> upperCoordinates = new ArrayList<>(pointDimensions);
        for (int i = 0; i < pointDimensions; i++) {
            double currentCoordinate = coordinates.get(i);
            double incomingCoordinate = incomingCoordinates.get(i);
            double upperCoordinate = Math.max(currentCoordinate, incomingCoordinate);
            upperCoordinates.add(upperCoordinate);
        }

        return new Point(upperCoordinates);
    }

    public Double computeMinDistanceFromBoundingBox(BoundingBox incomingBox) {
        if (getPointDimensions() != incomingBox.getLowerLeft().getPointDimensions() || getPointDimensions() != incomingBox.getUpperRight().getPointDimensions()) {
            throw new IllegalArgumentException("point dimensions must match the dimensions of the bounding box");
        }

        for (int i = 0; i < getPointDimensions(); i++) {
            double coord = getCoordinates().get(i);
            double lower = incomingBox.getLowerLeft().getCoordinates().get(i);
            double upper = incomingBox.getUpperRight().getCoordinates().get(i);

            if (coord < lower || coord > upper) {
                double minDistance = 0.0;
                for (int j = 0; j < getPointDimensions(); j++) {
                    double currentCoord = getCoordinates().get(j);
                    double currentLower = incomingBox.getLowerLeft().getCoordinates().get(j);
                    double currentUpper = incomingBox.getUpperRight().getCoordinates().get(j);

                    if (currentCoord < currentLower) {
                        minDistance += Math.pow(currentLower - currentCoord, 2);
                    } else if (currentCoord > currentUpper) {
                        minDistance += Math.pow(currentCoord - currentUpper, 2);
                    }
                }
                return Math.sqrt(minDistance);
            }
        }

        return 0.0; // point is inside the bounding box
    }
}
