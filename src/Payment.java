

public class Payment {
    private int paymentID;
    private int tripID;
    private String paymentMethod;
    private double amountPaid;
    private String status;
    private String paymentDate;


    /**
     * This is the constructor for this class
     * @param Paid Integer, representing the amount paid by the client
     * @param pMethod String, Method of payment (e.g., credit card  )
     * @param pID   Integer, The database key value tracker
     * @param pDate String, The time stamp for when payment is processed
     */
    public Payment(int pID, String pMethod, double Paid, String status, String pDate){
        this.paymentID = pID;
        this.paymentMethod = pMethod;
        this.amountPaid = Paid;
        this.status = status;
        this.paymentDate = pDate;
    }
    public double getAmountPaid(){
        return amountPaid;
    }
    public String getPaymentMethod(){
        return paymentMethod;
    }
    public int getPaymentID(){
        return paymentID;
    }
    public String getPaymentDate(){
        return paymentDate;
    }
    public int getTripID(){
        return tripID;
    }
    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void processPayment(){

    }
    public void verifyPayment(){

    }
    public int refundPayment(int amountPaid){
        return 0;
    }
    public String generateReceipt(){
        double tax = amountPaid * 0.625;
        double withTax = amountPaid + tax;

        return  String.format("""
            ID: %d
            
            Trip Fare---------------------$%.2f
            Tax---------------------------$%.2f
            Total-------------------------$%.2f
            
            Payment Method:
            %s
            %s
            """, tripID,
                amountPaid,
                tax,
                withTax,
                paymentMethod.toUpperCase(),
                paymentDate);
    }
}
