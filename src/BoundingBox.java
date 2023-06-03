import java.util.ArrayList;

public class BoundingBox {
    private ArrayList<Bounds> bounds;
    public BoundingBox(ArrayList<Bounds> bounds) {
        this.bounds = bounds;
    }

    public void showBoundingBox() {
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
}
