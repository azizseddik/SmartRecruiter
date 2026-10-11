package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByNomIgnoreCase(String nom);
}
