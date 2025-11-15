import java.util.List;
import java.util.Optional;
/**
 * HistoryDAO
 * version 0.1
 * 10/19/25
 * Interface defines basic operations (CRUD)
 */
public interface HistoryDAO {
    int insert(History d) throws Exception;
    Optional<History> findById(int tripID) throws Exception;
    List<History> findAll() throws Exception;
    List<History> findUserHistory(int carID) throws Exception;
    int update(History d) throws Exception;
    int delete(int tripID) throws Exception;
}