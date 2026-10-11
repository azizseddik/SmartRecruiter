package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Dto.*;
import SmartRecruiter.example.SmartRecruiter.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // Ajouter une compétence
    @PostMapping
    public ResponseEntity<SkillResponseDTO> ajouter(
            @RequestBody SkillRequestDTO dto) {

        SkillResponseDTO skill =
                skillService.ajouter(dto);

        return new ResponseEntity<>(
                skill,
                HttpStatus.CREATED
        );
    }

    // Récupérer toutes les compétences
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getToutesLesSkills() {

        return ResponseEntity.ok(
                skillService.getToutesLesSkills()
        );
    }

    // Récupérer une compétence par son ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                skillService.getSkillById(id)
        );
    }

    // Modifier une compétence
    @PutMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> modifier(
            @PathVariable Long id,
            @RequestBody SkillRequestDTO dto) {

        return ResponseEntity.ok(
                skillService.modifier(id, dto)
        );
    }

    // Supprimer une compétence
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id) {

        skillService.supprimer(id);

        return ResponseEntity.noContent().build();
    }
}
