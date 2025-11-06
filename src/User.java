public class User {

    private int userID;
    private String username;
    private String password;
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
    private String role;
    private String status;

    // Empty Constructor
    public User(){}

    // Constructor
    public User(String username,
                  String password,
                  String firstName,
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
                  String role,
                  String status){

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.license = license;
        this.dob = DOB;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = county;
        this.zipCode = zipCode;
        this.role = role;
        this.status = status;
    }

    // Getters
    public int getId(){
        return userID;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
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
    public String getRole() { return role; }
    public String getStatus() { return status; }

    //Setters
    public void setId(int userID){
        this.userID = userID;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
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
    public void setRole(String role) { this.role = role; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "User { " +
                "ID: " + userID + "\n" +
                "Name: " + getFirstName() + " " + getLastName()+ "\n" +
                "Email: " + getEmail() + "\n" +
                "Phone #: " + getPhone() + "\n" +
                "License #: " + getLicense() + "\n" +
                "DOB: " + getDob() + "\n" +
                "Address: " + getStreetAddress() + " " + getCity() + ", " +
                getState() + " " + getZipCode() + " " + getCountry()+ "\n" +
                "Status: " + getStatus() + " }";
    }

}
