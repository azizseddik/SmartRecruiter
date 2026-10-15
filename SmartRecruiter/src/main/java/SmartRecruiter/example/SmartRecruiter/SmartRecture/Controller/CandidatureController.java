package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import SmartRecruiter.example.SmartRecruiter.service.CandidatureService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SmartRecruiter.example.SmartRecruiter.Dto.CandidatureResponseDTO;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/candidatures")

public class CandidatureController {
    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    // 1. Afficher toutes les candidatures
    @GetMapping
    public ResponseEntity<List<CandidatureResponseDTO>> obtenirToutes() {
        List<CandidatureResponseDTO> resultats =
                candidatureService.obtenirToutes()
                        .stream()
                        .map(CandidatureResponseDTO::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(resultats);
    }

    // 2. Afficher les candidatures d'une offre
    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<CandidatureResponseDTO>> obtenirParOffre(
            @PathVariable Long offreId) {

        List<CandidatureResponseDTO> resultats =
                candidatureService.obtenirParOffre(offreId)
                        .stream()
                        .map(CandidatureResponseDTO::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(resultats);
    }

    // 3. Créer une candidature pour un candidat et une offre
    @PostMapping
    public ResponseEntity<?> creerCandidature(
            @RequestParam Long candidatId,
            @RequestParam Long offreId) {

        try {
            Candidature candidature =
                    candidatureService.creerCandidature(candidatId, offreId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CandidatureResponseDTO(candidature));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 4. Modifier le statut d'une candidature
    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> modifierStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        try {
            String statutTexte = body.get("statut");

            if (statutTexte == null || statutTexte.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Le statut est obligatoire."));
            }

            Candidature.Statut statut =
                    Candidature.Statut.valueOf(statutTexte.toUpperCase());

            Candidature candidature =
                    candidatureService.modifierStatut(id, statut);

            return ResponseEntity.ok(new CandidatureResponseDTO(candidature));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Statut invalide. Utilisez EN_ATTENTE, ACCEPTEE ou REJETEE."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
