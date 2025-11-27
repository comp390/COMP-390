import java.util.List;
import java.util.Optional;

/**
 * CarDAO
 * version 0.1
 * 10/19/25
 * Interface defines basic operations (CRUD)
 * Each method here is implemented in UserDAOSQLite
 */
public interface CarDAO {
    int insert(Car c) throws Exception;
    Optional<Car> findById(int carId) throws Exception;
    List<Car> findAll() throws Exception;
    List<Car> findAllByUserId(int userId) throws Exception;
    int update(Car c) throws Exception;
    int delete(int carId) throws Exception;
}
