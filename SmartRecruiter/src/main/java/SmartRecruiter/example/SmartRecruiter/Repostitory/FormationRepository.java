package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepository     extends JpaRepository<Formation, Long> {void deleteByCandidat_Id(Long candidatId);
}
