package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Entity.CandidatSkill;
import SmartRecruiter.example.SmartRecruiter.service.CandidatSkillServic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/candidat-skills")
@CrossOrigin(origins = "*")
public class CandidatSkillController {
    private  CandidatSkillServic candidatSkillService;
    public CandidatSkillController( CandidatSkillServic candidatSkillService)
    { this.candidatSkillService = candidatSkillService; }
    // Ajouter une compétence à un candidat
    @PostMapping
    public ResponseEntity<CandidatSkill> ajouter( @RequestParam Long candidatId, @RequestParam Long skillId, @RequestParam Integer niveau, @RequestParam Integer anneesExperience)
    { CandidatSkill candidatSkill = candidatSkillService.ajouter( candidatId, skillId, niveau, anneesExperience );
        return new ResponseEntity<>( candidatSkill, HttpStatus.CREATED ); }
    // Récupérer les compétences d'un candidat
    @GetMapping("/candidat/{candidatId}") public ResponseEntity<List<CandidatSkill>> getSkillsByCandidat( @PathVariable Long candidatId)
    { return ResponseEntity.ok( candidatSkillService.getSkillsByCandidat(candidatId) ); }
    // Supprimer une compétence d'un candidat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {

        candidatSkillService.supprimer(id);

        return ResponseEntity.noContent().build();
    }
}
