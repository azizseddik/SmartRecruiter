package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository  extends JpaRepository<Offre, Long> {
    List<Offre> findByTitreContainingIgnoreCaseOrDomaineContainingIgnoreCase(
            String titre,
            String domaine
    );
}
