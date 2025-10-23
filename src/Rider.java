public class Rider extends User{

    private int riderID;

    // Empty Constructor
    public Rider(){}

    // Constructor
    public Rider(String firstName,
                 String lastName,
                 String email,
                 String phone,
                 String status){
        super(firstName, lastName, email, phone, status);
    }

    // Getters
    public int getId(){
        return riderID;
    }

    //Setters
    public void setId(int riderID){
        this.riderID = riderID;
    }

    @Override
    public String toString() {
        return "Driver {" +
                "ID: " + riderID + "\n" +
                "Name: " + getFirstName() + " " + getLastName()+ "\n" +
                "Email: " + getEmail() + "\n" +
                "Phone #: " + getPhone() + "\n" +
                "Status: " + getStatus();
    }

}
