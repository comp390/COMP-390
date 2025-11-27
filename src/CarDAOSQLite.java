import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CarDAOSQLite
 * version 0.1
 * 10/19/25
 * This class handles interactions between our program and our database.
 * DAOs contain all the database specific code that allows the application
 * to interact with data without dealing with how it's stored or retrieved.
 */
class CarDAOSQLite implements CarDAO {

    /**
     * Adds a new Car record/row to the database car_id is automatically
     * generate by the database so it's not included in the INSERT statement,
     * but added to the Car object for the application after.
     * @param d A Car instance
     * @return Integer, The ID of the car
     * @throws Exception
     */
    @Override
    public int insert(Car d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String INSERT_CAR_SQL = "INSERT INTO car (user_id, make, model, year, ext_color, " +
                "int_color, int_materials, price, condition, license_plate_no" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(INSERT_CAR_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Placeholder ? get filled with values from Car object
            ps.setInt(1, d.getUserId());
            ps.setString(2, d.getMake());
            ps.setString(3, d.getModel());
            ps.setInt(4, d.getYear());
            ps.setString(5, d.getExteriorColor());
            ps.setString(6, d.getInteriorColor());
            ps.setString(7, d.getInteriorMaterials());
            ps.setDouble(8, d.getPrice());
            ps.setString(9, d.getCondition());
            ps.setString(10, d.getLicensePlate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Pull car_id from database (auto-generated) and store in Car object for reference
                int newCarID = rs.next() ? rs.getInt(1) : 0;
                d.setCarId(newCarID);
                return newCarID;
            }
        }
    }

    /**
     * Identify single Car from the database using it's car_ID
     * @param carID Integer, Takes the ID of the current car
     * @return Car object with the same ID provided
     * @throws Exception
     */
    @Override
    public Optional<Car> findById(int carID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_CAR_BY_ID_SQL = "SELECT * FROM car WHERE car_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_CAR_BY_ID_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, carID);
            try(ResultSet rs = ps.executeQuery()) {
                // If not matching ID found
                if(!rs.next()) return Optional.empty();
                // Build a new Car object from database info
                Car d = new Car(
                        rs.getInt("user_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("ext_color"),
                        rs.getString("int_color"),
                        rs.getString("int_materials"),
                        rs.getDouble("price"),
                        rs.getString("condition"),
                        rs.getString("license_plate_no")
                );
                d.setCarId(rs.getInt("car_id"));
                return Optional.of(d);
            }
        }
    }

    /**
     * Identify all Cars from the database using their User_id
     * @param userID Integer, Takes the ID of the current user (only driver user)
     * @return Car objects in List with the same ID provided
     * @throws Exception
     */
    @Override
    public List<Car> findAllByUserId(int userID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_CAR_BY_USER_ID_SQL = "SELECT * FROM car WHERE user_id = ? ORDER BY car_id";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_CAR_BY_USER_ID_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, userID);
            try(ResultSet rs = ps.executeQuery()) {
                // Build a new Car object from database info
                List<Car> list = new ArrayList<>();
                while (rs.next()) {
                    Car d = new Car(
                            rs.getInt("user_id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getString("ext_color"),
                            rs.getString("int_color"),
                            rs.getString("int_materials"),
                            rs.getDouble("price"),
                            rs.getString("condition"),
                            rs.getString("license_plate_no")
                    );
                    d.setCarId(rs.getInt("car_id"));
                    list.add(d);
                }
                return list;
            }
        }
    }

    /**
     * Pulls all Cars from database
     * @return List, a list of cars instances
     * @throws Exception
     */
    @Override
    public List<Car> findAll() throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_CARS_SQL = "SELECT * FROM car ORDER BY car_id";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_CARS_SQL);
            ResultSet rs = ps.executeQuery()) {

            List<Car> out = new ArrayList<>();
            // Loop through all rows in result set (each row = 1 Car)
            while(rs.next()){
                Car d = new Car(
                        rs.getInt("user_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("ext_color"),
                        rs.getString("int_color"),
                        rs.getString("int_materials"),
                        rs.getDouble("price"),
                        rs.getString("condition"),
                        rs.getString("license_plate_no")
                );
                d.setCarId(rs.getInt("car_id"));
                out.add(d);
            }
            return out;
        }
    }

    /**
     * Update existing Car in database, using car_id (primary key)
     * {@literal User (driver) may change/acquire new car, then update cards ID}
     * @param d Car, The instance of the vehicle to update ID
     * @return -
     * @throws Exception
     */
    @Override
    public int update(Car d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String UPDATE_CAR_SQL = "UPDATE car SET user_id = ?, make = ?, model = ?, year = ?, " +
                "ext_color = ?, int_color = ?, int_materials = ?, price = ?, condition = ?, license_plate_no = ?" +
                "WHERE car_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(UPDATE_CAR_SQL)) {

            ps.setInt(1, d.getUserId());
            ps.setString(2, d.getMake());
            ps.setString(3, d.getModel());
            ps.setInt(4, d.getYear());
            ps.setString(5, d.getExteriorColor());
            ps.setString(6, d.getInteriorColor());
            ps.setString(7, d.getInteriorMaterials());
            ps.setDouble(8, d.getPrice());
            ps.setString(9, d.getCondition());
            ps.setString(10, d.getLicensePlate());
            ps.setInt(11, d.getCarId());
            return ps.executeUpdate();
        }
    }

    /**
     * Delete Car from database using car_id
     * @param carID Integer, The ID of car to be deleted
     * @return -
     * @throws Exception
     */
    @Override
    public int delete(int carID) throws Exception{
        // SQL query statement for easy usage and maintenance
        String DELETE_CAR_SQL = "DELETE FROM car WHERE car_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(DELETE_CAR_SQL)) {
            ps.setInt(1, carID);
            return ps.executeUpdate();
        }
    }

}