import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// This class handles interactions between our program and our database.
// DAOs contain all the database specific code that allows the application
// to interact with data without dealing with how it's stored or retrieved.
class DriverDAOSQLite implements DriverDAO{

    // Adds a new Driver record/row to the database
    // driver_id is automatically generate by the database so it's not included
    // in the INSERT statement, but added to the Driver object for the application after.
    @Override
    public int insert(Driver d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String INSERT_DRIVER_SQL = "INSERT INTO driver (first_name, last_name, email, " +
            "phone_no, license_no, dob, street_address, city, state, country, zip_code" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(INSERT_DRIVER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Placeholder ? get filled with values from Driver object
            ps.setString(1, d.getdFirstName());
            ps.setString(2, d.getdLastName());
            ps.setString(3, d.getdEmail());
            ps.setString(4, d.getdPhone());
            ps.setString(5, d.getdLicense());
            ps.setString(6, d.getDob());
            ps.setString(7, d.getdStreetAddress());
            ps.setString(8, d.getdCity());
            ps.setString(9, d.getdState());
            ps.setString(10, d.getdCountry());
            ps.setString(11, d.getdZipCode());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Pull driver_id from database (auto-generated) and store in Driver object for reference
                int newDriverID = rs.next() ? rs.getInt(1) : 0;
                d.setdId(newDriverID);
                return newDriverID;
            }
        }
    }

    // Identify single Driver from the database using their driver_id
    @Override
    public Optional<Driver> findById(int driverID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_DRIVER_BY_ID_SQL = "SELECT driver_id, first_name, last_name, email, " +
            "phone_no, license_no, dob, street_address, city, state, country, zip_code" +
            "FROM driver WHERE driver_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_DRIVER_BY_ID_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, driverID);
            try(ResultSet rs = ps.executeQuery()) {
                // If not matching ID found
                if(!rs.next()) return Optional.empty();
                // Build a new Driver object from database info
                Driver d = new Driver(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_no"),
                        rs.getString("license_no"),
                        rs.getString("dob"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("zip_code")
                );
                d.setdId(rs.getInt("driver_id"));
                return Optional.of(d);
            }
        }
    }

    // Pulls all drivers from database
    @Override
    public List<Driver> findAll() throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_DRIVERS_SQL = "SELECT driver_id, first_name, last_name, email, " +
            "phone_no, license_no, dob, street_address, city, state, country, zip_code" +
            "FROM driver ORDER BY driver_id";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_DRIVERS_SQL);
            ResultSet rs = ps.executeQuery()) {

            List<Driver> out = new ArrayList<>();
            // Loop through all rows in result set (each row = 1 driver)
            while(rs.next()){
                Driver d = new Driver(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_no"),
                        rs.getString("license_no"),
                        rs.getString("dob"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("zip_code")
                );
                d.setdId(rs.getInt("driver_id"));
                out.add(d);
            }
            return out;
        }
    }

    // Update existing driver in database, using driver_id (primary key)
    @Override
    public int update(Driver d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String UPDATE_DRIVER_SQL = "UPDATE driver SET first_name = ?, last_name = ?, " +
            "email = ?, phone_no = ?, license_no = ?, dob = ?, street_address = ?, city = ?, " +
            "state = ?, country = ?, zip_code = ? WHERE driver_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(UPDATE_DRIVER_SQL)) {

            // Placeholder ? get updated with new values
            ps.setString(1, d.getdFirstName());
            ps.setString(2, d.getdLastName());
            ps.setString(3, d.getdEmail());
            ps.setString(4, d.getdPhone());
            ps.setString(5, d.getdLicense());
            ps.setString(6, d.getDob());
            ps.setString(7, d.getdStreetAddress());
            ps.setString(8, d.getdCity());
            ps.setString(9, d.getdState());
            ps.setString(10, d.getdCountry());
            ps.setString(11, d.getdZipCode());
            ps.setInt(12, d.getdId());
            return ps.executeUpdate();
        }
    }

    // Delete driver from database using driver_id
    @Override
    public int delete(int driverID) throws Exception{
        // SQL query statement for easy usage and maintenance
        String DELETE_DRIVER_SQL = "DELETE FROM driver WHERE driver_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(DELETE_DRIVER_SQL)) {
            ps.setInt(1, driverID);
            return ps.executeUpdate();
        }
    }

}