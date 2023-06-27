import java.io.Serializable;
import java.util.ArrayList;

public class BoundingBox implements Serializable{
    private Point lowerLeft;
    private Point upperRight;
    private Point center;
    private int dimensions;

    public BoundingBox() {
        lowerLeft = new Point();
        upperRight = new Point();
    }

    public BoundingBox(Point lowerLeft, Point upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        center = computeCenter();
        dimensions = lowerLeft.getPointDimensions();
    }

    public void showBoundingBox() {
        System.out.println("Bounding Box:");
        System.out.println("Upper Right Point:");
        upperRight.showPoint();
        System.out.println("Down Left Point:");
        lowerLeft.showPoint();
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public double computeArea() {
        double area = 1.0;
        for (int i = 0; i < dimensions; i++) {
            double dimensionLength = upperRight.getCoordinates().get(i) - lowerLeft.getCoordinates().get(i);
            area *= dimensionLength;
        }
        return area;
    }

    public Double computeOverlap(BoundingBox incomingBox) {
        Double overlap = 1.0;
        for (int i = 0; i < dimensions; i++) {
            Double lower1 = Math.max(lowerLeft.getCoordinates().get(i), incomingBox.getLowerLeft().getCoordinates().get(i));
            Double upper1 = Math.min(upperRight.getCoordinates().get(i), incomingBox.getUpperRight().getCoordinates().get(i));

            Double intersectionLength = Math.max(0, upper1 - lower1);
            overlap *= intersectionLength;
        }
        return overlap;
    }

    public double computeMargin() {
        double margin = 0.0;
        ArrayList<Double> upperRightCoordinates = upperRight.getCoordinates();
        ArrayList<Double> downLeftCoordinates = lowerLeft.getCoordinates();

        for (int i = 0; i < dimensions; i++) {
            double dimensionLength = upperRightCoordinates.get(i) - downLeftCoordinates.get(i);
            margin += 2 * dimensionLength; // Add the length of both sides of the dimension
        }
        return margin;
    }

    public Point computeCenter() {
        ArrayList<Double> center = new ArrayList<>();
        for (int i = 0; i < dimensions; i++) {
            Double dimensionCenter = (upperRight.getCoordinates().get(i) + lowerLeft.getCoordinates().get(i)) / 2.0;
            center.add(dimensionCenter);
        }
        return new Point(center);
    }

    public Double computeDistanceBetweenCenters(Point incomingCenter) {
        return center.computeDistanceFromPoint(incomingCenter);
    }

    public void createBoundingBoxOfEntries(ArrayList<Entry> entries) {
        if (entries.isEmpty()) {
            return; // no entries, nothing to compute
        }

        // initialize the upper right and lower left points with the values from the first entry
        Entry firstEntry = entries.get(0);
        dimensions = firstEntry.getBoundingBox().dimensions;
        BoundingBox firstEntryBoundingBox = firstEntry.getBoundingBox();
        upperRight.setCoordinates(new ArrayList<>(firstEntryBoundingBox.getUpperRight().getCoordinates()));
        lowerLeft.setCoordinates(new ArrayList<>(firstEntryBoundingBox.getLowerLeft().getCoordinates()));

        // iterate over the remaining entries and update the upper right and lower left points
        for (int i = 1; i < entries.size(); i++) {
            BoundingBox entryBoundingBox = entries.get(i).getBoundingBox();

            // update the upper right coordinates
            ArrayList<Double> entryUpperRightCoordinates = entryBoundingBox.getUpperRight().getCoordinates();
            for (int j = 0; j < dimensions; j++) {
                double currentUpperRight = upperRight.getCoordinates().get(j);
                double entryUpperRight = entryUpperRightCoordinates.get(j);
                double updatedUpperRight = Math.max(currentUpperRight, entryUpperRight);
                upperRight.getCoordinates().set(j, updatedUpperRight);
            }

            // update the lower left coordinates
            ArrayList<Double> entryLowerLeftCoordinates = entryBoundingBox.getLowerLeft().getCoordinates();
            for (int j = 0; j < dimensions; j++) {
                double currentLowerLeft = lowerLeft.getCoordinates().get(j);
                double entryLowerLeft = entryLowerLeftCoordinates.get(j);
                double updatedLowerLeft = Math.min(currentLowerLeft, entryLowerLeft);
                lowerLeft.getCoordinates().set(j, updatedLowerLeft);
            }
        }

        // update the center using computeCenter method
        center = computeCenter();
    }

    public boolean overlap(BoundingBox incomingBox) {

        for (int i = 0; i < dimensions; i++) {
            Double currentUpper = upperRight.getCoordinates().get(i);
            Double currentLower = lowerLeft.getCoordinates().get(i);
            Double incomingUpper = incomingBox.upperRight.getCoordinates().get(i);
            Double incomingLower = incomingBox.lowerLeft.getCoordinates().get(i);

            // check if there is no overlap in the current dimension
            if (currentUpper < incomingLower || currentLower > incomingUpper) {
                return false; // no overlap in at least one dimension
            }
        }
        return true; // overlap in all dimensions
    }

    public Double computeManhattanDistanceFromPoint(Point incomingPoint) {
        return lowerLeft.computeManhattanDistanceFromPoint(incomingPoint);
    }

    public boolean dominated(ArrayList<LeafEntry> skyline) {
        for (LeafEntry entry : skyline) {
            BoundingBox entryBoundingBox = entry.getBoundingBox();

            // check if the entry's bounding box dominates the current bounding box
            if (entryBoundingBox.dominates(this)) {
                return true; // the current bounding box is dominated
            }
        }
        return false; // the current bounding box is not dominated by any entry in the skyline
    }

    public boolean dominates(BoundingBox incomingBox) {
        Point incomingUpperRightPoint = incomingBox.getUpperRight();
        Point incomingLowerLeftPoint = incomingBox.getLowerLeft();

        for (int i = 0; i < dimensions; i++) {
            double currentUpperRight = upperRight.getCoordinates().get(i);
            double currentLowerLeft = lowerLeft.getCoordinates().get(i);
            double incomingUpperRight = incomingUpperRightPoint.getCoordinates().get(i);
            double incomingLowerLeft = incomingLowerLeftPoint.getCoordinates().get(i);

            // check if the current bounding box is not better in any dimension
            if (currentUpperRight > incomingUpperRight && currentLowerLeft > incomingLowerLeft) {
                return false; // the current bounding box does not dominate the other box in at least one dimension
            }
        }
        return true; // the current bounding box dominates the other box in all dimensions
    }

    public boolean identical(BoundingBox incomingBox) {
        // check if the dimensions match
        if (dimensions != incomingBox.dimensions) {
            return false;
        }

        // check if the lower left and upper right points match in each dimension
        for (int i = 0; i < dimensions; i++) {
            double currentLowerLeft = lowerLeft.getCoordinates().get(i);
            double currentUpperRight = upperRight.getCoordinates().get(i);
            double incomingLowerLeft = incomingBox.lowerLeft.getCoordinates().get(i);
            double incomingUpperRight = incomingBox.upperRight.getCoordinates().get(i);

            if (currentLowerLeft != incomingLowerLeft || currentUpperRight != incomingUpperRight) {
                return false; // value in at least one dimension does not match
            }
        }

        return true;
    }

}
