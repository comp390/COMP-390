public class Driver extends User {

    private int driverID;
    private String license;
    private String dob;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    // Empty Constructor
    public Driver(){}

    // Constructor
    public Driver(String firstName,
                  String lastName,
                  String email,
                  String phone,
                  String license,
                  String DOB,
                  String streetAddress,
                  String city,
                  String state,
                  String county,
                  String zipCode,
                  String status){

        super(firstName, lastName, email, phone, status);
        this.license = license;
        this.dob = DOB;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = county;
        this.zipCode = zipCode;
    }

    // Getters
    public int getId(){
        return driverID;
    }
    public String getLicense(){
        return license;
    }
    public String getDob(){
        return dob;
    }
    public String getStreetAddress(){
        return streetAddress;
    }
    public String getCity(){
        return city;
    }
    public String getState(){
        return state;
    }
    public String getCountry(){
        return country;
    }
    public String getZipCode(){
        return zipCode;
    }

    //Setters
    public void setId(int driverID){
        this.driverID = driverID;
    }
    public void setLicense(String license){
        this.license = license;
    }
    public void setDob(String dob){
        this.dob = dob;
    }
    public void setStreetAddress(String streetAddress){ this.streetAddress = streetAddress; }
    public void setCity(String city){
        this.city = city;
    }
    public void setState(String state){
        this.state = state;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Driver {" +
                "ID: " + driverID + "\n" +
                "Name: " + getFirstName() + " " + getLastName()+ "\n" +
                "Email: " + getEmail() + "\n" +
                "Phone #: " + getPhone() + "\n" +
                "License #: " + getLicense() + "\n" +
                "DOB: " + getDob() + "\n" +
                "Address: " + getStreetAddress() + " " + getCity() + ", " +
                getState() + " " + getZipCode() + " " + getCountry()g+
                "Status: " + getStatus();
    }

}
