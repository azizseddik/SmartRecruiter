package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {  List<Candidature> findByOffreId(Long offreId);

    List<Candidature> findByCandidatId(Long candidatId);

    Optional<Candidature> findByCandidatIdAndOffreId(
            Long candidatId,
            Long offreId
    );
}
