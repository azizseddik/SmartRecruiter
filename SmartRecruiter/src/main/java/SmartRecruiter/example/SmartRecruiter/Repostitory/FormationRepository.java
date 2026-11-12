package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository     extends JpaRepository<Formation, Long> {void deleteByCandidat_Id(Long candidatId);
    List<Formation> findByCandidat_Id(Long candidatId);
}
