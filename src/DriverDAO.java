import java.util.List;
import java.util.Optional;
// Interface defines basic operations (CRUD)
// Each method here is implemented in DriverDAOSQLite

public interface DriverDAO {
    int insert(Driver d) throws Exception;
    Optional<Driver> findById(int driverId) throws Exception;
    List<Driver> findAll() throws Exception;
    int update(Driver d) throws Exception;
    int delete(int driverId) throws Exception;
}