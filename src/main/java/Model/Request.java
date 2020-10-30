package Model;

public class Request {
    private Intersection pickup;
    private Intersection delivery;
    private int pickupDuration;
    private int deliveryDuration;

    /**
     * Constructor of object Request, creates a request with its pickup Intersection, delivery Intersection, and their
     * stay time duration
     * @param pickup the Intersection of the pickup point
     * @param delivery the Intersection of the delivery point
     * @param pickupDuration the stay time duration in the pickup point, expressed in seconds
     * @param deliveryDuration the stay time duration in the delivery point, expressed in seconds
     */
    public Request(Intersection pickup, Intersection delivery, int pickupDuration, int deliveryDuration) {
        this.pickup = pickup;
        this.delivery = delivery;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    public Intersection getPickup() {
        return pickup;
    }

    public Intersection getDelivery() {
        return delivery;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }
}
