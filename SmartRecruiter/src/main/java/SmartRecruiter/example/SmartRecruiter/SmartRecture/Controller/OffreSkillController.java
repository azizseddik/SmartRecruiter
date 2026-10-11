package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import java.util.List;
import SmartRecruiter.example.SmartRecruiter.Entity.OffreSkill;
import SmartRecruiter.example.SmartRecruiter.service.OffreSkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/offre-skills")
@CrossOrigin(origins = "*")
public class OffreSkillController {
    private  OffreSkillService offreSkillService;
    public OffreSkillController( OffreSkillService offreSkillService) { this.offreSkillService = offreSkillService; }
    // Ajouter une compétence à une offre
    @PostMapping
    public ResponseEntity<OffreSkill> ajouter( @RequestParam Long offreId, @RequestParam Long skillId, @RequestParam Integer niveauRequis, @RequestParam Boolean obligatoire) { OffreSkill offreSkill = offreSkillService.ajouter( offreId, skillId, niveauRequis, obligatoire ); return new ResponseEntity<>( offreSkill, HttpStatus.CREATED ); }
    // Récupérer les compétences d'une offre
    @GetMapping("/offre/{offreId}") public ResponseEntity<List<OffreSkill>> getSkillsByOffre( @PathVariable Long offreId) { return ResponseEntity.ok( offreSkillService.getSkillsByOffre(offreId) ); }
    // Supprimer une compétence d'une offre
    @DeleteMapping("/{id}") public ResponseEntity<Void> supprimer( @PathVariable Long id) { offreSkillService.supprimer(id); return ResponseEntity.noContent().build(); }
}
