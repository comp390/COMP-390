import java.util.List;
import java.util.Optional;
// Interface defines basic operations (CRUD)
// Each method here is implemented in UserDAOSQLite

public interface UserDAO {
    int insert(User d) throws Exception;
    Optional<User> findById(int UserId) throws Exception;
    List<User> findAll() throws Exception;
    int update(User d) throws Exception;
    int delete(int UserId) throws Exception;
}