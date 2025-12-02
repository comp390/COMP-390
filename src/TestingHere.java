import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TestingHere
 * version 0.1
 * 10/19/25
 * This class in meant to test the use cases
 * and funtionality of the application
 */
public class TestingHere {
  
    /**
     * Testing the connection to the database
     * @throws SQLException
     */
    @Test
    void testDatabaseConnection() throws SQLException {
        try(Connection c = DatabaseManager.get()) {
            assertNotNull(c);
        }
    }

    /**
     * Test for user insertion and update
     * @throws Exception
     */
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

    /**
     * Test for Car insertion and update
     * @throws Exception
     */
    @Test
    void testCarSQL() throws Exception {
        CarDAO dao = new CarDAOSQLite();
        Car c = new Car(1, "Tesla", "Model Y", 2025, "White", "Black", "Leather",
        60000, "good", "1MA130");
        int car_id = dao.insert(c);
        c.setCarId(car_id);

        List<Car> cars = new ArrayList<>();
        cars = dao.findAllByUserId(1);
        System.out.println(cars.size());
    }

    /**
     * UT001: Car - Create car object and verify attributes
     */
    @Test
    void testCar() {
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
    }

    /**
     * UT002: User - Create user object and verify attributes
     */
    @Test
    void testUser() {
        User u = new User();
        u.setFirstName("Jane");
        u.setLastName("Doe");
        u.setLicense("S12345");
        u.setState("MA");
        
        assertEquals("Jane", u.getFirstName());
        assertEquals("Doe", u.getLastName());
        assertEquals("S12345", u.getLicense());
        assertEquals("MA", u.getState());
    }

    /**
     * UT003: History - Create trip history and verify attributes
     */
    @Test
    void testHistory() {
        History h = new History();
        h.setPickupLoc("BSU");
        h.setDropoffLoc("Home");
        h.setFare(12.50);
        
        assertEquals("BSU", h.getPickupLoc());
        assertEquals("Home", h.getDropoffLoc());
        assertEquals(12.50, h.getFare());
        assertNotEquals("Home", h.getPickupLoc());
        assertNull(h.getStatus());
    }

    /**
     * UT004: Payment - Create payment and generate receipt
     */
    @Test
    void testPayment() {
        Payment p = new Payment();
        p.setAmountPaid(15.00);
        p.setTripID(1);
        p.setPaymentMethod("apple pay");
        p.setPaymentDate("2025-12-01");
        
        assertNotNull(p.generateReceipt());
        assertEquals(15.00, p.getAmountPaid());
        assertEquals(1, p.getTripID());
    }

    /**
     * UT005: FareCalculator - Calculate standard fare
     */
    @Test
    void testStandardFare() {
        double fare = FareCalculator.calculateStandardFare(10.0, 25);
        
        assertEquals(27.25, fare, 0.01);
        assertTrue(fare >= 5.00);
    }

    /**
     * UT006: FareCalculator - Calculate premium fare
     */
    @Test
    void testPremiumFare() {
        double standardFare = FareCalculator.calculateStandardFare(10.0, 25);
        double premiumFare = FareCalculator.calculatePremiumFare(10.0, 25);
        
        assertEquals(53.50, premiumFare, 0.01);
        assertTrue(premiumFare > standardFare);
    }


    /**
     * UT007: FareCalculator - Minimum fare enforcement
     */
    @Test
    void testMinimumFare() {
        double fare = FareCalculator.calculateStandardFare(0.5, 2);
        
        assertEquals(5.00, fare, 0.01);
    }

    /**
     * UT008: FareCalculator - Validate negative distance rejection
     */
    @Test
    void testNegativeDistance() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> FareCalculator.calculateStandardFare(-5.0, 10)
        );
        
        assertTrue(exception.getMessage().contains("Distance cannot be negative"));
    }

    /**
     * UT009: FareCalculator - Validate negative duration rejection
     */
    @Test
    void testNegativeDuration() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> FareCalculator.calculateStandardFare(5.0, -10)
        );
        
        assertTrue(exception.getMessage().contains("Duration cannot be negative"));
    }

    
}
