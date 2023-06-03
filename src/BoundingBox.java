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
}
