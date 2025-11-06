import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// This class handles interactions between our program and our database.
// DAOs contain all the database specific code that allows the application
// to interact with data without dealing with how it's stored or retrieved.
class UserDAOSQLite implements UserDAO{

    // Adds a new User record/row to the database
    // User_id is automatically generate by the database so it's not included
    // in the INSERT statement, but added to the User object for the application after.
    @Override
    public int insert(User d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String INSERT_User_SQL = "INSERT INTO users (username, password, first_name, last_name, email, " +
            "phone_no, license_no, dob, street_address, city, state, country, zip_code, role, status" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(INSERT_User_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Placeholder ? get filled with values from User object
            ps.setString(1, d.getUsername());
            ps.setString(2, d.getPassword());
            ps.setString(3, d.getFirstName());
            ps.setString(4, d.getLastName());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getPhone());
            ps.setString(7, d.getLicense());
            ps.setString(8, d.getDob());
            ps.setString(9, d.getStreetAddress());
            ps.setString(10, d.getCity());
            ps.setString(11, d.getState());
            ps.setString(12, d.getCountry());
            ps.setString(13, d.getZipCode());
            ps.setString(14, d.getRole());
            ps.setString(15, d.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Pull User_id from database (auto-generated) and store in User object for reference
                int newUserID = rs.next() ? rs.getInt(1) : 0;
                d.setId(newUserID);
                return newUserID;
            }
        }
    }

    // Identify single User from the database using their User_id
    @Override
    public Optional<User> findById(int UserID) throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_User_BY_ID_SQL = "SELECT * FROM users WHERE user_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_User_BY_ID_SQL)) {
            // Placeholder ? replaced by actual ID
            ps.setInt(1, UserID);
            try(ResultSet rs = ps.executeQuery()) {
                // If not matching ID found
                if(!rs.next()) return Optional.empty();
                // Build a new User object from database info
                User d = new User(
                        rs.getString("username"),
                        rs.getString("password"),
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
                        rs.getString("zip_code"),
                        rs.getString("role"),
                        rs.getString("status")
                );
                d.setId(rs.getInt("user_id"));
                return Optional.of(d);
            }
        }
    }

    // Pulls all Users from database
    @Override
    public List<User> findAll() throws Exception {
        // SQL query statement for easy usage and maintenance
        String SELECT_ALL_UserS_SQL = "SELECT * FROM users ORDER BY user_id";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(SELECT_ALL_UserS_SQL);
            ResultSet rs = ps.executeQuery()) {

            List<User> out = new ArrayList<>();
            // Loop through all rows in result set (each row = 1 User)
            while(rs.next()){
                User d = new User(
                        rs.getString("username"),
                        rs.getString("password"),
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
                        rs.getString("zip_code"),
                        rs.getString("role"),
                        rs.getString("status")
                );
                d.setId(rs.getInt("user_id"));
                out.add(d);
            }
            return out;
        }
    }

    // Update existing User in database, using User_id (primary key)
    @Override
    public int update(User d) throws Exception {
        // SQL query statement for easy usage and maintenance
        String UPDATE_User_SQL = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, " +
            "email = ?, phone_no = ?, license_no = ?, dob = ?, street_address = ?, city = ?, " +
            "state = ?, country = ?, zip_code = ?, role = ?, status = ? WHERE user_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(UPDATE_User_SQL)) {

            // Placeholder ? get updated with new values
            ps.setString(1, d.getUsername());
            ps.setString(2, d.getPassword());
            ps.setString(3, d.getFirstName());
            ps.setString(4, d.getLastName());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getPhone());
            ps.setString(7, d.getLicense());
            ps.setString(8, d.getDob());
            ps.setString(9, d.getStreetAddress());
            ps.setString(10, d.getCity());
            ps.setString(11, d.getState());
            ps.setString(12, d.getCountry());
            ps.setString(13, d.getZipCode());
            ps.setString(14, d.getRole());
            ps.setString(15, d.getStatus());
            ps.setInt(16, d.getId());
            return ps.executeUpdate();
        }
    }

    // Delete User from database using User_id
    @Override
    public int delete(int UserID) throws Exception{
        // SQL query statement for easy usage and maintenance
        String DELETE_User_SQL = "DELETE FROM users WHERE user_id = ?";
        try(Connection c = DatabaseManager.get();
            PreparedStatement ps = c.prepareStatement(DELETE_User_SQL)) {
            ps.setInt(1, UserID);
            return ps.executeUpdate();
        }
    }

}