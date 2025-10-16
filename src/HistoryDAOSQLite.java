import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// This class handles interactions between our program and our database.
// DAOs contain all the database specific code that allows the application
// to interact with data without dealing with how it's stored or retrieved.
public class HistoryDAOSQLite implements HistoryDAO{
    // Adds a new Trip record/row to the database
    // trip_id is automatically generate by the database so it's not included
    // in the INSERT statement, but added to the History object for the application after.
    @Override
    public int insert(History h) throws Exception {
        // SQL query statement for easy usage and maintenance
        String INSERT_HISTORY_SQL = "INSERT INTO history (rider_id, car_id, requested_at, pickup_loc, " +
                "dropoff_loc, fare_total, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseManager.get();
             PreparedStatement ps = c.prepareStatement(INSERT_HISTORY_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Placeholder ? get filled with values from History object
            ps.setInt(1, h.getRiderID());
            ps.setInt(2, h.getCarID());
            ps.setString(3, h.getRequestedAt());
            ps.setString(4, h.getPickupLoc());
            ps.setString(5, h.getDropoffLoc());
            ps.setDouble(6, h.getFare());
            ps.setString(7, h.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Pull driver_id from database (auto-generated) and store in Driver object for reference
                int newHistoryID = rs.next() ? rs.getInt(1) : 0;
                h.setHistoryID(newHistoryID);
                return newHistoryID;
            }
        }
    }

    // Identify single Trip from the database history table using trip_id
    @Override
    public Optional<History> findById(int tripID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_HISTORY_BY_ID_SQL = "SELECT * FROM history WHERE trip_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_HISTORY_BY_ID_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, tripID);
            try(ResultSet rs = ps.executeQuery()) {
                // If not matching ID found
                if(!rs.next()) return Optional.empty();
                // Build a new History object from database info
                History h = new History(
                        rs.getInt("rider_id"),
                        rs.getInt("car_id"),
                        rs.getString("requested_at"),
                        rs.getString("pickup_loc"),
                        rs.getString("dropoff_loc"),
                        rs.getDouble("fare_total"),
                        rs.getString("status")
                );
                h.setHistoryID(rs.getInt("trip_id"));
                return Optional.of(h);
            }
        }
    }

    // Pulls all trips from database
    @Override
    public List<History> findAll() throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_HISTORY_SQL = "SELECT * FROM history ORDER BY trip_id";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_HISTORY_SQL);
            ResultSet rs = ps.executeQuery()) {

            List<History> out = new ArrayList<>();
            // Loop through all rows in result set (each row = 1 trip)
            while(rs.next()){
                History h = new History(
                        rs.getInt("rider_id"),
                        rs.getInt("car_id"),
                        rs.getString("requested_at"),
                        rs.getString("pickup_loc"),
                        rs.getString("dropoff_loc"),
                        rs.getDouble("fare_total"),
                        rs.getString("status")
                );
                h.setHistoryID(rs.getInt("trip_id"));
                out.add(h);
            }
            return out;
        }
    }

    // Pulls all trips for single rider from database
    @Override
    public List<History> findRiderHistory(int riderID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_RIDER_HISTORY_SQL = "SELECT * FROM history WHERE rider_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_RIDER_HISTORY_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, riderID);
            try(ResultSet rs = ps.executeQuery()) {
                List<History> out = new ArrayList<>();
                // Loop through all rows in result set (each row = 1 trip)
                while(rs.next()){
                    History h = new History(
                            rs.getInt("rider_id"),
                            rs.getInt("car_id"),
                            rs.getString("requested_at"),
                            rs.getString("pickup_loc"),
                            rs.getString("dropoff_loc"),
                            rs.getDouble("fare_total"),
                            rs.getString("status")
                    );
                    h.setHistoryID(rs.getInt("trip_id"));
                    out.add(h);
                }
                return out;
            }
        }
    }

    // Pulls all trips for single driver from database
    @Override
    public List<History> findDriverHistory(int riderID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_DRIVER_HISTORY_SQL = "SELECT h.*, c.driver_id, c.license_plate_no " +
                "FROM history h JOIN car c ON h.car_id = c.car_id " +
                "WHERE c.driver_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_DRIVER_HISTORY_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, riderID);
            try(ResultSet rs = ps.executeQuery()) {
                List<History> out = new ArrayList<>();
                // Loop through all rows in result set (each row = 1 trip)
                while(rs.next()){
                    History h = new History(
                            rs.getInt("rider_id"),
                            rs.getInt("car_id"),
                            rs.getString("requested_at"),
                            rs.getString("pickup_loc"),
                            rs.getString("dropoff_loc"),
                            rs.getDouble("fare_total"),
                            rs.getString("status")
                    );
                    h.setHistoryID(rs.getInt("trip_id"));
                    out.add(h);
                }
                return out;
            }
        }
    }

    // Update existing trip in database, using trip_id (primary key)
    @Override
    public int update(History h) throws Exception {
        // SQL query statement for easy usage and maintenance
        String UPDATE_DRIVER_SQL = "UPDATE history SET rider_id = ?, car_id = ?, " +
                "requested_at = ?, pickup_loc = ?, dropoff_loc = ?, fare_total = ?, status = ? " +
                "WHERE trip_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(UPDATE_DRIVER_SQL)) {

            // Placeholder ? get updated with new values
            ps.setInt(1, h.getRiderID());
            ps.setInt(2, h.getCarID());
            ps.setString(3, h.getRequestedAt());
            ps.setString(4, h.getPickupLoc());
            ps.setString(5, h.getDropoffLoc());
            ps.setDouble(6, h.getFare());
            ps.setString(7, h.getStatus());
            ps.setInt(8, h.getHistoryID());
            return ps.executeUpdate();
        }
    }

    // Delete trip from database using trip_id
    @Override
    public int delete(int tripID) throws Exception{
        // SQL query statement for easy usage and maintenance
        String DELETE_TRIP_SQL = "DELETE FROM history WHERE trip_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(DELETE_TRIP_SQL)) {
            ps.setInt(1, tripID);
            return ps.executeUpdate();
        }
    }
}
