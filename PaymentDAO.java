import java.util.List;
import java.util.Optional;

/**
 * PaymentDAO
 * version 0.1
 * 10/19/25
 * Interface defines basic operations (CRUD)
 */
public interface PaymentDAO {
    int insert(Payment p) throws Exception;
    Optional<Payment> findById(int paymentID) throws Exception;
    List<Payment> findAll() throws Exception;
    int update(Payment p) throws Exception;
    int delete(int paymentID) throws Exception;
}

