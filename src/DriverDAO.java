import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This Class will provide access to the Driver database table.
 */
public class DriverDAO {
    private String path = "jdbc:sqlite:our_dbTable.db";

    // Default constructor for the DriverDAO
    public DriverDAO(){
        createDriverDB();
    }

    // Constructor for the HistoryDAO and allow to change databases
    public DriverDAO(String dbURL){
        this.path = dbURL;
        createDriverDB();
    }

    public void createDriverDB(){
        String sql = "CREATE TABLE IF NOT EXISTS drivers ("
                + " d_id TEXT PRIMARY KEY,"
                + " d_name TEXT NOT NULL,"
                + " d_phone TEXT,"
                + " d_license TEXT NOT NULL,"
                + " d_dob TEXT NOT NULL,"
                + " d_address TEXT,"
                + " c_id TEXT NOT NULL,"
                + " h_id INTEGER NOT NULL,"
                + " FOREIGN KEY(c_id) REFERENCES cars(c_id),"
                + " FOREIGN KEY(h_id) REFERENCES history(h_id)"
                + ");";

        try (Connection ctn = DriverManager.getConnection(path);
            Statement stmt = ctn.createStatement()){
            stmt.execute(sql);
            System.out.println("\n>> Driver table created! <<");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void save_D(Driver d){
        String sql = "INSERT INTO drivers(" +
                "d_name," +
                "d_phone," +
                "d_license," +
                "d_dob," +
                "d_address," +
                "c_id," +
                "h_id) "+
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (Connection cnt = DriverManager.getConnection(path);
        PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(2, d.getdName());
            stmt.setString(3, d.getdPhone());
            stmt.setString(4, d.getdLicense());
            stmt.setString(5, d.getDob());
            stmt.setString(6, d.getdAddress());
            stmt.setString(7, d.getdCarID());
            stmt.setInt(8, d.getdHistoryID());
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // find driver by the Driver id
    public Optional<Driver> find_D_ByID(String dID){
        String sql = "SELECT * FROM drivers WHERE id";
        Driver driver = null;


        try (Connection cnt = DriverManager.getConnection(path);
             PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(1,dID);
            ResultSet rst = stmt.executeQuery();

            //if found, create obj
            if (rst.next()){
                driver = new Driver(rst.getString("d_name"),
                        rst.getString("d_phone"),
                        rst.getString("d_license"),
                        rst.getString("d_dob"),
                        rst.getString("d_address"),
                        rst.getString("c_id"),
                        rst.getInt("h_id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        // if not found
        return Optional.ofNullable(driver);
    }

    // find all drivers in the table
    public  List<Driver> findAll(){
        String sql = "SELECT * FROM drivers";
        List<Driver> drivers = new ArrayList<>();

        try (Connection cnt = DriverManager.getConnection(path);
        Statement stmt = cnt.createStatement();
        ResultSet rst = stmt.executeQuery(sql)){

            while(rst.next()){
                Driver driver = new Driver(rst.getString("d_name"),
                        rst.getString("d_phone"),
                        rst.getString("d_license"),
                        rst.getString("d_dob"),
                        rst.getString("d_address"),
                        rst.getString("c_id"),
                        rst.getInt("h_id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return drivers;
    }

    // Update a given driver table
    public void  updateD(Driver d){
        String sql = "UPDATE drivers SET d_name = ?, d_phone = ?," +
                "d_license = ?, d_dob = ?, d_address = ?, c_id = ?, h_id = ?";

        try (Connection cnt = DriverManager.getConnection(path);
            PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(1, d.getdName());
            stmt.setString(2, d.getdPhone());
            stmt.setString(3, d.getdLicense());
            stmt.setString(4, d.getDob());
            stmt.setString(5, d.getdAddress());
            stmt.setString(6, d.getdCarID());
            stmt.setInt(7, d.getdHistoryID());
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    // delete data table
    public void dDelete(String d_ID){
        String sql = "DELETE FROM drivers WHERE d_id = ?";

        try (Connection cnt = DriverManager.getConnection(path);
            PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(1, d_ID);
            stmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
