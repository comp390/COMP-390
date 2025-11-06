import java.util.List;
import java.util.Optional;
// Interface defines basic operations (CRUD)
// Each method here is implemented in DriverDAOSQLite

public interface HistoryDAO {
    int insert(History d) throws Exception;
    Optional<History> findById(int tripID) throws Exception;
    List<History> findAll() throws Exception;
    List<History> findUserHistory(int carID) throws Exception;
    int update(History d) throws Exception;
    int delete(int tripID) throws Exception;
}