package Model;

public class Request {
    private Intersection pickup;
    private Intersection delivery;
    private int pickupDuration;
    private int deliveryDuration;

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
