public class Bounds {

    private Double upperBound;
    private Double lowerBound;

    public Bounds(Double upperBound, Double lowerBound) {
        this.upperBound=upperBound;
        this.lowerBound=lowerBound;
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
