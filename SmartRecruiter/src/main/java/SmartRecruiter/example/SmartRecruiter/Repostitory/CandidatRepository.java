package SmartRecruiter.example.SmartRecruiter.Repostitory;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import java.util.Optional;
import java.util.List;
import java.util.Optional;
@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    List<Candidat> findByNomContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDomaineContainingIgnoreCase( String nom, String email, String domaine );

}
