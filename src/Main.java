import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Main {
    public static void main(String[] args){
        String schemaFile = "schema.sql";
        String databaseURL = "jdbc:sqlite:rideshare.db";

        // establish connection through jdbc (.jar file in lib)
        // if no such database is found it will create locally for you, otherwise connect to db
        try (Connection c = DriverManager.getConnection(databaseURL);
             Statement s = c.createStatement()) {
            // Read the entire schema.sql file into a String
            String sql = Files.readString(Paths.get(schemaFile));
            // Execute each statement separated by semicolons
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    s.execute(trimmed + ";");
                }
            }

            System.out.println("Schema loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading schema: " + e.getMessage());
        }
    }
}

