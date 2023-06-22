import java.io.Serializable;

public class Bounds implements Serializable{

    private Double upperBound;
    private Double lowerBound;

    public Bounds(Double upperBound, Double lowerBound) {
        this.upperBound=upperBound;
        this.lowerBound=lowerBound;
    }

    public Bounds() {
    }

    public Double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }
}
