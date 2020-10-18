package Model;

public class Intersection {

    final static double RADIUS_MAJOR = 6378137.0;

    private Long id;
    private double latitude;
    private double longitude;
    private double x;
    private double y;

    /**
     * Constructor of object Intersection, creates an intersection with id, its latitude and longitude
     * @param id the id of the intersection as it is noted in the map
     * @param latitude the latitude of the intersection
     * @param longitude the longitude of the intersection
     */
    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.x = xAxisProjection(longitude);
        this.y = yAxisProjection(latitude);
    }

    /**
     * Calculate the x position that an intersection is actually drawn on the application, given its longitude
     * @param longitude the longitude of the intersection read from the map
     * @return x position of an intersection displayed on the application
     */
    static double xAxisProjection(double longitude) {
        return Math.toRadians(longitude) * RADIUS_MAJOR;

    }

    /**
     * Calculate the y position that an intersection is actually drawn on the application, given its latitude
     * @param latitude the latitude of the intersection read from the map
     * @return y position of an intersection displayed on the application
     */
    static double yAxisProjection(double latitude) {
        return -Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2)) * RADIUS_MAJOR;
    }

    public Long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    
}
