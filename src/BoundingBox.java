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
}
