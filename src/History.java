
public class History {
    private int tripID;
    private int riderID;
    private int carID;
    private String requestedAt;
    private String pickupLoc;
    private String dropoffLoc;
    private Double fare;
    private String status;

    // Constructor
    public History(Integer riderID, Integer carID, String requestedAt,
                   String pickupLoc, String dropoffLoc, Double fare, String status) {
        this.riderID = riderID;
        this.carID = carID;
        this.requestedAt = requestedAt;
        this.pickupLoc = pickupLoc;
        this.dropoffLoc = dropoffLoc;
        this.fare = fare;
        this.status = status;
    }

    // getters
    public int getHistoryID() {
        return tripID;
    }
    public int getRiderID() {
        return riderID;
    }
    public int getCarID() {
        return carID;
    }
    public String getRequestedAt() {
        return requestedAt;
    }
    public String getPickupLoc() { return pickupLoc; }
    public String getDropoffLoc() { return dropoffLoc; }
    public Double getFare() { return fare; }
    public String getStatus() { return status; }

    // Needs validation
    public void setRiderID(Integer driverID) {
        this.riderID = riderID;
    }

    // Needs validation
    public void setCarID(Integer carID) {
        this.carID = carID;
    }

    public void setHistoryID(int id) { this.tripID = tripID; }

    // check Rider id is not empty
    private String validateRiderID(String rID) {
        //TODO check length is correct
        if (rID == null || rID.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid riderID: " + rID);
        }
        return rID;
    }

    // check car id is not empty
    private String validateCarID(String cID) {
        //TODO check length is correct
        if (cID == null || cID.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid carID: " + cID);
        }
        return cID;
    }
}
