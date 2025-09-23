public class Driver {

    private String dID;
    private String Name;
    private String Phone;
    private String License;
    private String dob;
    private String address;
    private String dCarID;
    private int dHistoryID;

    // Constructor
    public Driver(String dName,
                  String dPhone,
                  String dLicense,
                  String dob,
                  String address,
                  String dCar,
                  int dHistoryID){

        this.Name = dName;
        this.Phone = dPhone;
        this.License = dLicense;
        this.dob = dob;
        this.address = address;
        this.dCarID = dCar;
        this.dHistoryID = dHistoryID;
    }

    // Getters
    public String getdName(){
        return Name;
    }
    public String getdPhone(){
        return Phone;
    }
    public String getdLicense(){
        return License;
    }
    public String getDob(){
        return dob;
    }
    public String getdAddress(){
        return address;
    }
    public String getdCarID(){
        return dCarID;
    }
    public String getdID(){
        return dID;
    }
    public int getdHistoryID(){
        return dHistoryID;
    }
    public void setdID(String d){
        this.dID = d;
    }
}
