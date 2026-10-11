package SmartRecruiter.example.SmartRecruiter.Repostitory;
import SmartRecruiter.example.SmartRecruiter.Entity.OffreSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface OffreSkillRepository extends JpaRepository<OffreSkill, Long> {
}
