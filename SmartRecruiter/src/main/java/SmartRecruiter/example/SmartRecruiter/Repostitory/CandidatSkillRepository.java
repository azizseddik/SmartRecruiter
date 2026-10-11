package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.CandidatSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CandidatSkillRepository extends JpaRepository<CandidatSkill, Long>{
    void deleteByCandidat_Id(Long candidatId);
}
