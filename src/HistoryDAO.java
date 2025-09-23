import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This Class will provide access to the History database table.
 */
public class HistoryDAO {
    private String path = "jdbc:sqlite:our_dbTable.db";

    // Default constructor for the HistoryDAO
    public  HistoryDAO(){
        createHistoryDB();
    }
    // Constructor for the HistoryDAO and allow to change database
    public HistoryDAO(String dbURL){
        this.path = dbURL;
        createHistoryDB();
    }

//    public void connect(){
//
//        // try to connect to database
//        try (var cnt = DriverManager.getConnection(path)){
//            System.out.println("\n**** Connection to SQLite has been established! ****");
//
//            // Execute database operations
//            // ...
//
//        } catch (SQLException e){
//            System.err.println("Database error: " + e.getMessage());
//        }
//        // Connection will be close here
//    }

    public void createHistoryDB(){

        String sql = """ 
                CREATE TABLE IF NOT EXISTS history (
                    h_id INTEGER PRIMARY KEY,
                    d_id TEXT NOT NULL,
                    r_id TEXT NOT NULL,
                    c_id TEXT,
                    p_id TEXT NOT NULL,
                    FOREIGN KEY(r_id) REFERENCES riders(r_id),
                    FOREIGN KEY(d_id) REFERENCES drivers(d_id),
                    FOREIGN KEY(p_id) REFERENCES payments(p_id),
                    FOREIGN KEY(c_id) REFERENCES cars(c_id),
                    UNIQUE(d_id, r_id, c_id, p_id)
                    );
                """;

        try (Connection cnt = DriverManager.getConnection(path);
             Statement stmt = cnt.createStatement()) {
            stmt.execute(sql);
            System.out.println("\n>> History table created! <<");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void save_H(History h){
        String sql = """
                        INSERT OR IGNORE INTO history (d_id, r_id, c_id, p_id) VALUES(?, ?, ?, ?);
                     """;
        try (Connection cnt = DriverManager.getConnection(path);
             PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(1, h.getDriverID());
            stmt.setString(2, h.getRiderID());
            stmt.setString(3, h.getCarID());
            stmt.setString(4, h.getPaymentID());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New record inserted");
            } else {
                System.out.println("Duplicate record - insertion skipped");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Find the history table with the given id
     * @param h_id An integer that represents the table id
     * @return An history obj
     */
    public Optional<History> find_H_ByID(int h_id){
        String sql = "SELECT * FROM history WHERE id = ?";
        History history = null;

        try (Connection cnt = DriverManager.getConnection(path);
            PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setInt(1, h_id);
            ResultSet rst = stmt.executeQuery();

            // IF found, create object
            if (rst.next()){
                history = new History(
                        rst.getString("d_id"),
                        rst.getString("r_id"),
                        rst.getString("c_id"),
                        rst.getString("p_id"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // If not found returns Optional.empty() history obj
        return Optional.ofNullable(history);
    }

    /**
     * Search table and creates the obj in each row
     * @return A list of found objs in the table
     */
    public List<History> findAll(){
        String sql = "SELECT * FROM history";
        List<History> histories = new ArrayList<>();

        try (Connection cnt = DriverManager.getConnection(path);
            Statement stmt = cnt.createStatement();
            ResultSet rst = stmt.executeQuery(sql)){

            while (rst.next()){
                History history = new History(
                        rst.getInt("h_id"),
                        rst.getString("d_id"),
                        rst.getString("r_id"),
                        rst.getString("c_id"),
                        rst.getString("p_id"));
                histories.add(history);
            }
            // TODO: print what happend
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return histories;
    }


    //TODO SECURITY: This could be bad, unless a real reason to change IDs.
    // (pay-OK, in case of cancelation or something)
    public void updateH(History h){
        String sql = "UPDATE history SET d_id = ?, r_id = ?, c_id =?, p_id = ? WHERE h_id = ?";

        try (Connection cnt = DriverManager.getConnection(path);
            PreparedStatement stmt = cnt.prepareStatement(sql)){
            stmt.setString(1, h.getDriverID());
            stmt.setString(2, h.getRiderID());
            stmt.setString(3, h.getCarID());
            stmt.setString(4, h.getPaymentID());
            System.out.println("\nTable has been updated with:");
            System.out.println("Driver ID: "+h.getDriverID()+
                    "\nRider ID: "+h.getRiderID()+
                    "\nCar ID: "+h.getCarID()+
                    "\nPayment ID: "+h.getPaymentID());

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void printHTable(){
        String sql = "SELECT * FROM history;";

        try (Connection cnt = DriverManager.getConnection(path);
            Statement stmt = cnt.createStatement();
            ResultSet rst = stmt.executeQuery(sql)){


            while(rst.next()){
                System.out.println(
                        "\nhistory_id: "+rst.getInt("h_id")+
                        "\ndriver_id: "+rst.getString("d_id")+
                        "\nrider_id: "+rst.getString("r_id")+
                        "\ncar_id: "+rst.getString("c_id")+
                        "\npayment_id: "+rst.getString("p_id")
                );
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // TODO: Think of a resonable reason for deleting a history table,
    //  if so create a delete() method
}
