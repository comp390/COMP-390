import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class implemets the contract with PaymentDAO and allows for data storage and retrieval.
 */
public class PaymentDAOSQLite implements PaymentDAO {
    private final String PATH = "jdbc:sqlite:sqlite.db";

    @Override
    public int insert(Payment p) throws Exception{
        final String INSERT_PAYMENT_SQL = "INSERT INTO payment (paid_id, paid_method, fare_total, status, " +
                "paid_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = DatabaseManager.get();
             PreparedStatement ps = c.prepareStatement(INSERT_PAYMENT_SQL, Statement.RETURN_GENERATED_KEYS)){
            ps.setDouble(1, p.getAmountPaid());
            ps.setString(2, p.getPaymentMethod());
            ps.setInt(3, p.getPaymentID());
            ps.setString(4, p.getPaymentDate());
            ps.setString(5, p.getStatus());

            // Retrive the generated key
            int insertedRows = ps.executeUpdate();
            if (insertedRows > 0) {
                try (ResultSet genKey = ps.getGeneratedKeys()) {
                    if (genKey.next()) {
                        return genKey.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    @Override
    public List<Payment> findAll() throws Exception {
        final String SELECT_ALL_PAYMENT_SQL = "SELECT * FROM payment ";

        try (Connection cnt = DatabaseManager.get();
        PreparedStatement ps = cnt.prepareStatement(SELECT_ALL_PAYMENT_SQL);
        ResultSet rs = ps.executeQuery()){

            List<Payment> payments = new ArrayList<>();
            while (rs.next()){
                Payment p = new Payment(
                  rs.getInt("payment_id"),
                  rs.getString("pay_method"),
                  rs.getDouble("amount"),
                  rs.getString("status"),
                  rs.getString("processed_at")
                );

                p.setTripID(rs.getInt("trip_id"));
                payments.add(p);
            }
            return payments;
        }
    }

    /**
     *  Finds the payment record in the database using the paymentID
     * @param paymentID The identifier of the payment record to search for
     * @return THe an instance of the class with the data being search
     * @throws Exception if a database access error occurs
     */
    @Override
    public Optional<Payment> findById(int paymentID) throws Exception{
       final String SELECT_PAYMENT_BY_ID_SQL = "SELECT * FROM payment WHERE payment_id = ?";

        try (Connection cnt = DatabaseManager.get();
            PreparedStatement ps = cnt.prepareStatement(SELECT_PAYMENT_BY_ID_SQL)){

            // Should I have search for tripID instead??
            ps.setInt(1, paymentID);
            try (ResultSet r = ps.executeQuery()){
                if (!r.next()) return Optional.empty();{
                    Payment p = new Payment(
                        r.getInt("payment_id"),
                        r.getString("pay_method"),
                        r.getDouble("amount"),
                        r.getString("status"),
                        r.getString("processed_at")
                    );
                    p.setTripID(r.getInt("trip_id"));
                    return Optional.of(p);
                }
            }
        }
    }

    @Override
    public int update(Payment p) throws Exception{

    String UPDATE_PAYMENT_SQL = "UPDATE payment SET pay_method = ?, amount = ?, " +
            "status = ?, processed_at = ? WHERE payment_id = ?";
    try (Connection cnt = DatabaseManager.get();
    PreparedStatement ps = cnt.prepareStatement(UPDATE_PAYMENT_SQL)){
        ps.setString(1,p.getPaymentMethod());
        ps.setDouble(2, p.getAmountPaid());
        ps.setString(3, p.getStatus());
        ps.setString(4,p.getPaymentDate());
        ps.setInt(5, p.getPaymentID());
        return ps.executeUpdate();
    }
}

    /**
     * Deletes a payment record from the database based on the payment ID.
     * @param paymentID The identifier of the payment record to delete
     * @return  number of rows (1 if successful, 0 if no payment found)
     * @throws Exception if a database access error occurs
     */
    @Override
    public int delete(int paymentID) throws Exception{
        String DEL_PAYMENT_SQL = "DELETE FROM payment WHERE payment_id = ?";

        try (Connection cnt = DatabaseManager.get();
            PreparedStatement ps = cnt.prepareStatement(DEL_PAYMENT_SQL)){
            // Will it make sense deleting the Payment table or deleting the trip?
            ps.setInt(1, paymentID);
            return ps.executeUpdate();
        }
    }
}
