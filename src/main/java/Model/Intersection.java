package Model;

public class Intersection {

    final static double RADIUS_MAJOR = 6378137.0;

    private Long id;
    private double latitude;
    private double longitude;
    private double x;
    private double y;

    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.x = xAxisProjection(latitude);
        this.y = yAxisProjection(longitude);
    }

    static double xAxisProjection(double latitude) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2)) * RADIUS_MAJOR;
    }

    static double yAxisProjection(double longitude) {
        return Math.toRadians(longitude) * RADIUS_MAJOR;
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
