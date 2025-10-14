public class Driver {

    private int driverID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String license;
    private String dob;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    // Constructor
    public Driver(String dFirstName,
                  String dLastName,
                  String dEmail,
                  String dPhone,
                  String dLicense,
                  String dDOB,
                  String dStreetAddress,
                  String dCity,
                  String dState,
                  String dCounty,
                  String dZipCode){

        this.firstName = dFirstName;
        this.lastName = dLastName;
        this.email = dEmail;
        this.phone = dPhone;
        this.license = dLicense;
        this.dob = dDOB;
        this.streetAddress = dStreetAddress;
        this.city = dCity;
        this.state = dState;
        this.country = dCounty;
        this.zipCode = dZipCode;
    }

    // Getters
    public int getdId(){
        return driverID;
    }
    public String getdFirstName(){
        return firstName;
    }
    public String getdLastName(){
        return lastName;
    }
    public String getdEmail(){
        return email;
    }
    public String getdPhone(){
        return phone;
    }
    public String getdLicense(){
        return license;
    }
    public String getDob(){
        return dob;
    }
    public String getdStreetAddress(){
        return streetAddress;
    }
    public String getdCity(){
        return city;
    }
    public String getdState(){
        return state;
    }
    public String getdCountry(){
        return country;
    }
    public String getdZipCode(){
        return zipCode;
    }

    //Setters
    public void setdId(int driverID){
        this.driverID = driverID;
    }

}
