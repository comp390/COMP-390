/**
 * History
 * version 0.1
 * 10/19/25
 * This class holds information about the trip itself
 */
public class History {
    private int tripID;     //
    private int userID;
    private int carID;
    private String requestedAt;
    private String pickupLoc;
    private String dropoffLoc;
    private Double fare;
    private String status;
    private String licensePlateNo;

    /**
     * Empty Constructor for this class
     */
    public History() {
    }

    /**
     * Constructor for this class
     *
     * @param userID         Integer, Unique identifier
     * @param carID          Integer, Unique identifier.
     * @param requestedAt    String, The time of initial request.
     * @param pickupLoc      String, Address where driver picks rider.
     * @param dropoffLoc     String, Address where driver drops rider.
     * @param fare           Double, The calculated fare.
     * @param status         String,
     * @param licensePlateNo String, Unique char for the license plate.
     */
    public History(Integer userID, Integer carID, String requestedAt,
                   String pickupLoc, String dropoffLoc, Double fare, String status,
                   String licensePlateNo) {
        this.userID = userID;
        this.carID = carID;
        this.requestedAt = requestedAt;
        this.pickupLoc = pickupLoc;
        this.dropoffLoc = dropoffLoc;
        this.fare = fare;
        this.status = status;
        this.licensePlateNo = licensePlateNo;
    }

    /**
     * This function returns the object's trip ID
     *
     * @return Integer, Unique Trip ID
     */
    public int getHistoryID() {
        return tripID;
    }

    /**
     * This function returns the object's rider ID
     *
     * @return Integer, Unique rider ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * This function returns the object's car ID
     *
     * @return Integer, Unique car ID
     */
    public int getCarID() {
        return carID;
    }

    /**
     * This function returns the time rider was requested
     *
     * @return String, value of the request time
     */
    public String getRequestedAt() {
        return requestedAt;
    }

    /**
     * This function returns the location where driver meets the rider
     *
     * @return String, A address will be return
     */
    public String getPickupLoc() {
        return pickupLoc;
    }

    /**
     * This function returns the location where the drive takes the rider
     *
     * @return String, A address will be return
     */
    public String getDropoffLoc() {
        return dropoffLoc;
    }

    /**
     * This function returns the amount pay by the rider
     *
     * @return Double, amount in the decimal format #.##
     */
    public Double getFare() {
        return fare;
    }

    /**
     * This function returns the status of the history object
     *
     * @return String, The tate of the trip
     */
    public String getStatus() {
        return status;
    }

    /**
     * This function returns the object's licence Plate num
     *
     * @return String, Unique licence plate for the car
     */
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    /**
     * Sets the history ID
     * @param tripID Integer, The ID of the trip
     */
    public void setHistoryID(int tripID) {
        this.tripID = tripID;
    }

    /**
     * Sets the time of the request
     * @param requestedAt String, Time
     */
    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }

    /**
     *  Sets the location where to pick up the rider
     * @param pickupLoc String, address of the location
     */
    public void setPickupLoc(String pickupLoc) {
        this.pickupLoc = pickupLoc;
    }

    /**
     * Sets the location to drop the rider
     * @param dropoffLoc String, address on the location
     */
    public void setDropoffLoc(String dropoffLoc) {
        this.dropoffLoc = dropoffLoc;
    }

    /**
     * The amount being charge to the rider
     * @param fare Double, total amount
     */
    public void setFare(Double fare) {
        this.fare = fare;
    }

    /**
     * Sets the status of the trip
     * @param status String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the license plate number of the car used by the driver
     * @param licensePlateNo String
     */
    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    /**
     * The ID of the rider user
     * @param riderID Integer
     */
    public void setUserID(Integer riderID) {
        this.userID = validateUserID(userID);
    }

    /**
     * The ID given to the car
     * @param carID Integer
     */
    public void setCarID(Integer carID) {
        this.carID = validateCarID(carID);
    }

    /**
     * This function validate the rider given ID and returns it back if it's valid.
     *
     * @param rID Integer, Rider ID to be validated
     * @return Integer, Validated rider ID
     * @throws IllegalArgumentException if rider ID is invalid
     */
    private int validateUserID(int rID) {
        // make sure it is a valid num
        if ((rID < 0)) {
            throw new IllegalArgumentException("Invalid riderID: " + rID);
        }
        return rID;
    }

    /**
     * This function validate the car given ID and returns it back if it's valid.
     *
     * @param cID Integer, Car ID to be validated
     * @return Integer, Validated car ID
     * @throws IllegalArgumentException if car ID is invalid
     */
    private int validateCarID(int cID) {
        if ((cID < 0)) {
            throw new IllegalArgumentException("Invalid carID: " + cID);
        }
        return cID;
    }
}

