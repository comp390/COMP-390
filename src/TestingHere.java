import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class TestingHere {
    @Test
    void testCar() {
        System.out.println("Test");
        Car c = new Car();
        c.setMake("Tesla");
        c.setModel("Model S");
        c.setYear(2020);
        c.setPrice(100000);
        assertEquals("Tesla", c.getMake());
        assertEquals("Model S", c.getModel());
        assertEquals(2020, c.getYear());
        assertEquals(100000, c.getPrice());
        assertNull(c.getInteriorColor());

        System.out.println(c.toString());
        System.out.println(c.getMake());
        System.out.println(c.getModel());
        System.out.println(c.getYear());
        System.out.println(c.getPrice());
        System.out.println(c);
    }

    @Test
    void testUser() {
        User d = new User();
        d.setFirstName("Jane");
        d.setLastName("Doe");
        d.setLicense("S12345");
        d.setState("MA");
        assertEquals("Jane", d.getFirstName());
        assertEquals("Doe", d.getLastName());
        assertEquals("S12345", d.getLicense());
        assertEquals("MA", d.getState());
    }

    @Test
    void testHistory() {
        History h = new History();
        h.setPickupLoc("BSU");
        h.setDropoffLoc("home");
        h.setFare(12.50);
        assertNotEquals("home", h.getPickupLoc());
        assertEquals(12.50, h.getFare());
        assertEquals("BSU", h.getPickupLoc());
        assertNull(h.getStatus());
    }

    @Test
    void testPayment() {
        Payment p = new Payment();
        p.setAmountPaid(15.00);
        p.setTripID(1);
        p.setPaymentMethod("apple pay");
        p.setPaymentDate("2025-10-22");
        System.out.println(p.generateReceipt());
        assertNotNull(p.generateReceipt());
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        try(Connection c = DatabaseManager.get()) {
            assertNotNull(c);
        }
    }

    @Test
    void testUserSQL() throws Exception {
        UserDAO dao = new UserDAOSQLite();
        User d = new User("john.d", "password", "John", "Driver", "test@email.com", "5081231234",
                "S12345", "2000-01-01", "24 Park Ave", "Bridgewater",
                "MA", "USA", "02324", "driver", "active");

        int user_id = dao.insert(d);
        assertTrue(user_id > 0);
        d.setId(user_id);

        System.out.println(d.toString());
        d.setLicense("S67890");
        dao.update(d);

        Optional<User> d2 = dao.findById(user_id);
        assertTrue(d2.isPresent());
        if(d2.isPresent()){
            d = d2.get();
        }

        assertEquals("S67890", d.getLicense());

        dao.delete(user_id);
        Optional<User> d3 = dao.findById(user_id);
        assertFalse(d3.isPresent());

        assertFalse(dao.findAll().isEmpty());
    }


    
    @Test
    void testFareCalculator() {
        System.out.println("\n=== Testing FareCalculator ===");
    
        double fare = FareCalculator.calculateStandardFare(10.0, 25);
    
        System.out.println("Trip: 10 miles, 25 minutes");
        System.out.println("Calculated fare: $" + String.format("%.2f", fare));
    
        assertEquals(27.25, fare, 0.01, "Fare should be $27.25");
        assertTrue(fare >= 5.00, "Fare should meet $5 minimum");
    }

}
