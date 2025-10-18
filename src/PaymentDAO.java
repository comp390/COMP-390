import java.util.List;
import java.util.Optional;


public interface PaymentDAO {
    int insert(Payment p) throws Exception;
    Optional<Payment> findById(int paymentID) throws Exception;
    List<Payment> findAll() throws Exception;
    int update(Payment p) throws Exception;
    int delete(int paymentID) throws Exception;
}

