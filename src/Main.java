import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Main {
    public static void main(String[] args){
        String schemaFile = "schema.sql";
        String databaseURL = "jdbc:sqlite:rideshare.db";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             Statement stmt = conn.createStatement()) {

            // Read the entire schema.sql file into a String
            String sql = Files.readString(Paths.get(schemaFile));

            // Execute each statement separated by semicolons
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed + ";");
                }
            }

            System.out.println("Schema loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading schema: " + e.getMessage());
        }



        //  history implementayion
        //HistoryDAO history = new HistoryDAO();

        // Manager class executes history
        // DatabaseManager dbManager = new DatabaseManager(history);

        // Create a new HISTORY obj and save it
        //History h1 = new History("15-S", "AA6", "S8FW-86", "S5-D7F8ES2F2");
        //History h2 = new History("kdoo", "5476", "f88-evd", "1227-568592");
        //History h3 = new History("98oo", "ii76", "777-evd", "3d-96654448");

        //history.save_H(h3);
        //history.save_H(h1);
        //history.save_H(h2);
        //history.save_H(h3);
    }
}

