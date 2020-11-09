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

    /**
     * Constuctor of object Request. Constructing by a given Request
     * @param request the request which we want to use.
     */
    public Request(Request request) {
        this.pickup = request.pickup;
        this.delivery = request.delivery;
        this.pickupDuration = request.pickupDuration;
        this.deliveryDuration = request.deliveryDuration;
    }

    /**
     * Obtain the intersection for the pickup point of this request
     * @return pickup the intersection for the pickup point of this request
     */
    public Intersection getPickup() {
        return pickup;
    }

    /**
     * Obtain the intersection for the delivery point of this request
     * @return pickup the intersection for the delivery point of this request
     */
    public Intersection getDelivery() {
        return delivery;
    }

    /**
     * Obtain the duration for the pickup of this request
     * @return pickupDuration the duration for the pickup of this request
     */
    public int getPickupDuration() {
        return pickupDuration;
    }

    /**
     * Obtain the duration for the delivery of this request
     * @return deliveryDuration the duration for the delivery of this request
     */
    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    /**
     * Set the duration for the pickup of this request
     * @param pickupDuration the duration for the pickup of this request
     */
    public void setPickupDuration(int pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    /**
     * Set the duration for the delivery of this request
     * @param deliveryDuration the duration for the pickup of this request
     */
    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}
