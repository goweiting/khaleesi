package vision.tools;

/**
 * Created by Simon Rovder
 */
public class DirectedPoint extends VectorGeometry {

    public double direction;

    /**
     * @param x,y   current coordinates on the field
     * @param theta The direction of the vector point
     */
    public DirectedPoint(double x, double y, double theta) {
        super(x, y);
        this.direction = theta;
    }

    public DirectedPoint clone() {
        return new DirectedPoint(this.x, this.y, this.direction);
    }

    @Override
    public String toString() {
        return "[ " + this.x + " , " + this.y + " ] - " + this.direction;
    }

    public double getDirection() {
        return this.direction;
    }
}
