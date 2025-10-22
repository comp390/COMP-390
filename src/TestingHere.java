import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    void testDriver() {
        Driver d = new Driver();
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
    void testRider() {
        Rider r = new Rider();
        r.setEmail("rider1@gmail.com");
        r.setPhone("5081231234");
        r.setStatus("active");
        assertEquals("rider1@gmail.com", r.getEmail());
        assertEquals("5081231234", r.getPhone());
        assertEquals("active", r.getStatus());
        assertNotEquals("inactive", r.getStatus());
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
    void testDriverSQL() throws Exception {
        DriverDAO dao = new DriverDAOSQLite();
        Driver d = new Driver("John", "Driver", "test@email.com", "5081231234",
                "S12345", "2000-01-01", "24 Park Ave", "Bridgewater",
                "MA", "USA", "02324", "active");

        int driver_id = dao.insert(d);
        assertTrue(driver_id > 0);
    }

}
