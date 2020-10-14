package Data;

import java.util.LinkedList;

public class Intersection {

        final static double RADIUS_MAJOR = 6378137.0;
        final static int NORMAL_POINT = 0;
        public final static int PICKUP_POINT = 1;
        public final static int DELIVERY_POINT = 2;
        public final static int DEPOT_POINT = 3;
        final static int WHITE = 0;
        final static int GRAY = 1;
        final static int BLACK = 2;

        public float latitude;
        public float longitude;
        double x ;
        double y ;
        long id;
        int type = NORMAL_POINT;
        int color = WHITE;
        int duration;
        String departTime;




    LinkedList<Intersection> toIntersections = new LinkedList<Intersection>();
        LinkedList<Double> distanceList = new LinkedList<Double>();

        public Intersection(long newId, float newLatitude, float newLongitude) {
            latitude = newLatitude;
            longitude = newLongitude;
            id = newId;
            xAxisProjection();
            yAxisProjection();
        }

        public void addIntersections(Intersection Intersection, double distanceTo) {
            toIntersections.add(Intersection);
            distanceList.add(distanceTo);
        }


        void xAxisProjection() {
            x = Math.toRadians(latitude) * RADIUS_MAJOR;
        }

        void yAxisProjection() {
            y = Math.log(Math.tan(Math.PI / 4 + Math.toRadians(longitude) / 2)) * RADIUS_MAJOR;
        }

        public void setType(int newType, int newDuration, String newDepartTime) {
            type = newType;
            duration = newDuration;
            departTime = newDepartTime == "" ? null : newDepartTime;
        }

        public double getX() {
             return x;
        }

         public double getY() {
            return y;
        }

        public int getType() {
            return type;
        }

        public LinkedList<Intersection> getToIntersections() {
            return toIntersections;
        }
}
