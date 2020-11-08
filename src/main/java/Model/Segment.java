package Model;

public class Segment {

    private Intersection origin;
    private Intersection destination;
    private String name;
    private double length;

    /**
     * Constructor of object Segment, creates a segment which has a certain orientation,
     * with its name, origin and destination Intersections,and its length
     * @param origin the start/origin Intersection of the Segment
     * @param destination the end/destination Intersection of the Segment
     * @param name the name of the Segment
     * @param length the length of the Segment, expressed in meters
     */
    public Segment(Intersection origin, Intersection destination, String name, double length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

    public Segment() {

    }

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }
}
