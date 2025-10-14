import java.sql.*;
// establishes connection to database for DAOSQLite classes
public class DatabaseManager {
    // open or create file rideshare.db
    private static final String URL = "jdbc:sqlite:rideshare.db";
    public static Connection get() throws SQLException {
        // Start connection
        Connection c = DriverManager.getConnection(URL);
        // Command needed for foregin keys, SQLite doesn't enforce them automatically
        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        }
        // return connection for other classes to use
        return c;
    }

}
