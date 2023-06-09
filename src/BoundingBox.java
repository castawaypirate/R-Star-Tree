import java.util.ArrayList;

public class BoundingBox {
    private ArrayList<Bounds> bounds;
    private ArrayList<Double> center;
    public BoundingBox(ArrayList<Bounds> bounds) {
        this.bounds = bounds;
        center = computeCenter();
    }

    public BoundingBox() {
        bounds = new ArrayList<>();
    }

    public void showBoundingBox() {
        System.out.println("Bounding Box:");
        for(int i=0;i<bounds.size();i++) {
            System.out.println("Bounds in dimension " + (i+1) + ": " + bounds.get(i).getUpperBound() + ", " + bounds.get(i).getLowerBound());
        }
    }


    public ArrayList<Bounds> getBounds() {
        return bounds;
    }

    public double computeArea() {
        Double area = 1.0;
        for (Bounds bound : bounds) {
            Double dimensionLength = bound.getUpperBound() - bound.getLowerBound();
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

    public double computeMargin() {
        double margin = 0.0;
        for (Bounds bound : bounds) {
            double dimensionLength = bound.getUpperBound() - bound.getLowerBound();
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

}
