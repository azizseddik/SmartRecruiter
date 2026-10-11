package SmartRecruiter.example.SmartRecruiter.Repostitory;
import SmartRecruiter.example.SmartRecruiter.Entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository


public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    void deleteByCandidat_Id(Long candidatId);
}
