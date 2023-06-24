import java.io.Serializable;
import java.util.ArrayList;

public class BoundingBox implements Serializable{
    private ArrayList<Bounds> bounds;
    private Point upperRight;
    private Point lowerLeft;
    private Point centerPoint;

    private int dimensions;
    private ArrayList<Double> center;

    public BoundingBox(ArrayList<Bounds> bounds) {
        this.bounds = bounds;
        center = computeCenter();
        if (!bounds.isEmpty()) {
            int dimensions = bounds.size();
            ArrayList<Double> upperRightCoordinates = new ArrayList<>(dimensions);
            ArrayList<Double> downLeftCoordinates = new ArrayList<>(dimensions);

            for (Bounds bound : bounds) {
                upperRightCoordinates.add(bound.getUpperBound());
                downLeftCoordinates.add(bound.getLowerBound());
            }

            upperRight = new Point(upperRightCoordinates);
            lowerLeft = new Point(downLeftCoordinates);
        }
        dimensions = upperRight.getPointDimensions();
    }

    public BoundingBox() {
        bounds = new ArrayList<>();
    }

    public BoundingBox(Point lowerLeft, Point upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public void showBoundingBox() {
        System.out.println("Bounding Box:");
        for(int i=0;i<bounds.size();i++) {
            System.out.println("Bounds in dimension " + (i+1) + ": " + bounds.get(i).getUpperBound() + ", " + bounds.get(i).getLowerBound());
        }
    }

    public void showBoundingBoxPoint() {
        System.out.println("Bounding Box:");
        System.out.println("Upper Right Point:");
        upperRight.showPoint();
        System.out.println("Down Left Point:");
        lowerLeft.showPoint();
    }

    public ArrayList<Bounds> getBounds() {
        return bounds;
    }
    public Point getUpperRight() {
        return upperRight;
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public double computeArea() {
        Double area = 1.0;
        for (Bounds bound : bounds) {
            Double dimensionLength = bound.getUpperBound() - bound.getLowerBound();
            area *= dimensionLength;
        }
        return area;
    }

    public double computeAreaPoint() {
        double area = 1.0;
        for (int i = 0; i < dimensions; i++) {
            double dimensionLength = upperRight.getCoordinates().get(i) - lowerLeft.getCoordinates().get(i);
            area *= dimensionLength;
        }
        return area;
    }

    public Double computeOverlap(BoundingBox incomingBox) {
        Double overlap = 1.0;
        ArrayList<Bounds> incomingBoxBounds = incomingBox.getBounds();

        for (int i = 0; i < bounds.size(); i++) {
            Double lower1 = bounds.get(i).getLowerBound();
            Double upper1 = bounds.get(i).getUpperBound();
            Double lower2 = incomingBoxBounds.get(i).getLowerBound();
            Double upper2 = incomingBoxBounds.get(i).getUpperBound();

            Double intersectionLength = Math.max(0, Math.min(upper1, upper2) - Math.max(lower1, lower2));
            overlap *= intersectionLength;
        }

        return overlap;
    }

    public Double computeOverlapPoint(BoundingBox incomingBox) {
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
        for (Bounds bound : bounds) {
            double dimensionLength = bound.getUpperBound() - bound.getLowerBound();
            margin += 2 * dimensionLength; // Add the length of both sides of the dimension
        }
        return margin;
    }

    public double computeMarginPoint() {
        double margin = 0.0;
        ArrayList<Double> upperRightCoordinates = upperRight.getCoordinates();
        ArrayList<Double> downLeftCoordinates = lowerLeft.getCoordinates();

        for (int i = 0; i < dimensions; i++) {
            double dimensionLength = upperRightCoordinates.get(i) - downLeftCoordinates.get(i);
            margin += 2 * dimensionLength; // Add the length of both sides of the dimension
        }
        return margin;
    }

    public ArrayList<Double> computeCenter() {
        ArrayList<Double> center = new ArrayList<>();
        for (Bounds bound : bounds) {
            Double dimensionCenter = (bound.getUpperBound() + bound.getLowerBound()) / 2.0;
            center.add(dimensionCenter);
        }
        return center;
    }

    public Point computeCenterPoint() {
        ArrayList<Double> center = new ArrayList<>();
        for (int i = 0; i < dimensions; i++) {
            Double dimensionCenter = (upperRight.getCoordinates().get(i) + lowerLeft.getCoordinates().get(i)) / 2.0;
            center.add(dimensionCenter);
        }
        return new Point(center);
    }

    public Double computeDistanceBetweenCenters(ArrayList<Double> incomingCenter) {
        // Calculate the Euclidean distance between the centers
        Double distance = 0.0;
        for (int i = 0; i < center.size(); i++) {
            Double diff = center.get(i) - incomingCenter.get(i);
            distance += Math.pow(diff, 2);
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    public Double computeDistanceBetweenCentersPoint(Point incomingCenter) {
        return centerPoint.computeDistanceFromPoint(incomingCenter);
    }

    public void createBoundingBoxOfEntries(ArrayList<Entry> entries) {
        bounds.clear();
        // initialize the bounds with the values from the first entry
        Entry firstEntry = entries.get(0);
        ArrayList<Bounds> firstEntryBounds = firstEntry.getBoundingBox().getBounds();
        for (Bounds bound : firstEntryBounds) {
            bounds.add(new Bounds(bound.getUpperBound(), bound.getLowerBound()));
        }

        // iterate over the remaining entries and update the bounds
        for (int i = 1; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            ArrayList<Bounds> entryBounds = entry.getBoundingBox().getBounds();

            // update the bounds for each dimension
            for (int j = 0; j < bounds.size(); j++) {
                Bounds currentBounds = bounds.get(j);
                Bounds entryBound = entryBounds.get(j);

                // update the upper and lower bounds if necessary
                if (entryBound.getUpperBound() > currentBounds.getUpperBound()) {
                    currentBounds.setUpperBound(entryBound.getUpperBound());
                }
                if (entryBound.getLowerBound() < currentBounds.getLowerBound()) {
                    currentBounds.setLowerBound(entryBound.getLowerBound());
                }
            }
        }
        center = computeCenter();
    }

    public void createBoundingBoxOfEntriesPoint(ArrayList<Entry> entries) {
        upperRight = new Point();
        lowerLeft = new Point();

        // initialize the upper right and down left points with the values from the first entry
        Entry firstEntry = entries.get(0);
        BoundingBox firstEntryBoundingBox = firstEntry.getBoundingBox();
        upperRight.setCoordinates(new ArrayList<>(firstEntryBoundingBox.getUpperRight().getCoordinates()));
        lowerLeft.setCoordinates(new ArrayList<>(firstEntryBoundingBox.getLowerLeft().getCoordinates()));

        // iterate over the remaining entries and update the upper right and down left points
        for (int i = 1; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            BoundingBox entryBoundingBox = entry.getBoundingBox();
            Point entryUpperRight = entryBoundingBox.getUpperRight();
            Point entryDownLeft = entryBoundingBox.getLowerLeft();

            // update the upper right coordinates
            ArrayList<Double> upperRightCoordinates = upperRight.getCoordinates();
            ArrayList<Double> entryUpperRightCoordinates = entryUpperRight.getCoordinates();
            for (int j = 0; j < upperRightCoordinates.size(); j++) {
                Double currentMax = upperRightCoordinates.get(j);
                Double entryValue = entryUpperRightCoordinates.get(j);
                if (entryValue > currentMax) {
                    upperRightCoordinates.set(j, entryValue);
                }
            }

            // update the down left coordinates
            ArrayList<Double> downLeftCoordinates = lowerLeft.getCoordinates();
            ArrayList<Double> entryDownLeftCoordinates = entryDownLeft.getCoordinates();
            for (int j = 0; j < downLeftCoordinates.size(); j++) {
                Double currentMin = downLeftCoordinates.get(j);
                Double entryValue = entryDownLeftCoordinates.get(j);
                if (entryValue < currentMin) {
                    downLeftCoordinates.set(j, entryValue);
                }
            }
        }

        // Update the center using computeCenterPoint method
        centerPoint = computeCenterPoint();
    }

    public boolean overlap(BoundingBox box) {
        for (int i = 0; i < bounds.size(); i++) {
            Bounds currentBounds = bounds.get(i);
            Bounds boxBounds = box.getBounds().get(i);

            // check if there is no overlap in the current dimension
            if (currentBounds.getUpperBound() < boxBounds.getLowerBound()
                    || currentBounds.getLowerBound() > boxBounds.getUpperBound()) {
                return false; // no overlap in at least one dimension
            }
        }
        return true; // overlap in all dimensions
    }

    public boolean overlapPoint(BoundingBox incomingBox) {

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

    public double computeManhattanDistanceFromPoint(ArrayList<Double> point) {
        double distance = 0.0;
        ArrayList<Double> bottomLeftPoint = getBottomLeftPoint();

        for (int i = 0; i < bottomLeftPoint.size(); i++) {
            double diff = Math.abs(bottomLeftPoint.get(i) - point.get(i));
            distance += diff;
        }

        return distance;
    }

    public Double computeManhattanDistanceFromPoint(Point incomingPoint) {
        return lowerLeft.computeManhattanDistanceFromPoint(incomingPoint);
    }

    private ArrayList<Double> getBottomLeftPoint() {
        ArrayList<Double> bottomLeftPoint = new ArrayList<>();
        for (Bounds bound : bounds) {
            bottomLeftPoint.add(bound.getLowerBound());
        }
        return bottomLeftPoint;
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

}
