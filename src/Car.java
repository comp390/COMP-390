import java.util.Scanner;
public class Car {
    Driver driver;
    private int carId;
    private int driverId;
    private String make;
    private String model;
    private int year;
    private String exteriorColor;
    private String interiorColor;
    private String licensePlate;
    private double price;
    private String condition;
    private boolean isAvailable;

    // Empty Constructor
    public Car(){}

    //Constructor
    public Car(int carId, int driverId, Driver driver, String make, String model, int year,
               String exteriorColor, String interiorColor, String licensePlate,
               double price, String condition, boolean isAvailable)
    {
        this.carId = carId;
        this.driverId = driverId;
        this.driver = driver;
        this.make = make;
        this.model = model;
        this.year = year;
        this.exteriorColor = exteriorColor;
        this.interiorColor = interiorColor;
        this.licensePlate = licensePlate;
        this.price = price;
        this.condition = condition;
        this.isAvailable = isAvailable;
    }

    //Getters
    public Driver getDriver() {
        return driver;
    }
    public int getCarId() {
        return carId;
    }
    public int getDriverId() {
        return carId;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public int getYear() {
        return year;
    }
    public String getExteriorColor() {
        return exteriorColor;
    }
    public String getInteriorColor() {
        return interiorColor;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public double getPrice() {
        return price;
    }
    public String getCondition() {
        return condition;
    }
    public boolean isAvailable() {
        return isAvailable;
    }

    //Setters
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }
    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }


}
