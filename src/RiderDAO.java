import java.util.List;
import java.util.Optional;
// Interface defines basic operations (CRUD)
// Each method here is implemented in RiderDAOSQLite

public interface RiderDAO {
    int insert(Rider d) throws Exception;
    Optional<Rider> findById(int riderId) throws Exception;
    List<Rider> findAll() throws Exception;
    int update(Rider d) throws Exception;
    int delete(int riderId) throws Exception;
}

