package SmartRecruiter.example.SmartRecruiter.Repostitory;
import SmartRecruiter.example.SmartRecruiter.Entity.ResultatMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface ResultatMatchingRepository extends JpaRepository<ResultatMatching, Long>  {
}
