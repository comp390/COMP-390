
public class History {
    private String driverID;
    private String riderID;
    private String paymentID;
    private String carID;
    private  int historyID;


    /**
     * Constructor for the class
     * @param driverID
     * @param riderID
     * @param paymentID
     */
    public History(String driverID, String riderID, String carID, String paymentID) {
        this.driverID = driverID;
        this.riderID = riderID;
        this.carID = carID;
        this.paymentID = paymentID;
//        this.historyID = createHistoryID(driverID, riderID, carID, paymentID);
    }

    public History(int hID, String driverID, String riderID, String carID, String paymentID) {
        this.driverID = driverID;
        this.riderID = riderID;
        this.carID = carID;
        this.paymentID = paymentID;
        this.historyID = hID;
    }

    public String getDriverID() {
        return driverID;
    }
    public String getRiderID() {
        return riderID;
    }
    public int getHistoryID() {
        return historyID;
    }
    public String getCarID(){
        return carID;
    }
    public String getPaymentID() {
        return paymentID;
    }

    // Needs validation
    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    // Needs validation
    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    // Needs validation
    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setHistoryID(int id){
        this.historyID = id;
    }
    public void setCarID(String carID){
        this.carID = carID;
    }

    // check Rider id is not empty
    private String validateRiderID(String rID) {
        //TODO check length is correct
        if (rID == null || rID.trim().isEmpty()){
            throw new IllegalArgumentException("Invalid driverID: "+ rID);
        }
        return rID;
    }

    // check driver id is not empty
    private String validateDriverID(String dID) {
        //TODO check length is correct
        if (dID == null || dID.trim().isEmpty()){
            throw new IllegalArgumentException("Invalid driverID: "+ dID);
        }
        return dID;
    }

    // check driver id is not empty
    private String validatePaymentID(String payID) {
        //TODO check length is correct
        if (payID == null || payID.trim().isEmpty()){
            throw new IllegalArgumentException("Invalid driverID: "+ payID);
        }
        return payID;
    }

//    /**
//     * Create an ID based on the attribute of the object
//     * @param dID The Driver ID
//     * @param rID The Rider ID
//     * @param pID The Payment ID
//     * @return An Integer of unique numbers
//     */
//    private int createHistoryID(String dID, String rID, String cID, String pID){
//        String addedStr = dID + rID + cID + pID;
//        return addedStr.chars().sum();
//    }
}
