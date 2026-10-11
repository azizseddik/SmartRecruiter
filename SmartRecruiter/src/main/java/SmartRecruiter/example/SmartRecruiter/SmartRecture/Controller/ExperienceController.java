package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Dto.*;
import SmartRecruiter.example.SmartRecruiter.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@CrossOrigin(origins = "*")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    // Ajouter une expérience
    @PostMapping
    public ResponseEntity<ExperienceResponseDTO> ajouter(
            @RequestBody ExperienceRequestDTO dto) {

        ExperienceResponseDTO experience =
                experienceService.ajouter(dto);

        return new ResponseEntity<>(
                experience,
                HttpStatus.CREATED
        );
    }

    // Récupérer toutes les expériences
    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getToutesLesExperiences() {

        return ResponseEntity.ok(
                experienceService.getToutesLesExperiences()
        );
    }

    // Récupérer une expérience par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponseDTO> getExperienceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                experienceService.getExperienceById(id)
        );
    }

    // Modifier une expérience
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponseDTO> modifier(
            @PathVariable Long id,
            @RequestBody ExperienceRequestDTO dto) {

        return ResponseEntity.ok(
                experienceService.modifier(id, dto)
        );
    }

    // Supprimer une expérience
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id) {

        experienceService.supprimer(id);

        return ResponseEntity.noContent().build();
    }
}
