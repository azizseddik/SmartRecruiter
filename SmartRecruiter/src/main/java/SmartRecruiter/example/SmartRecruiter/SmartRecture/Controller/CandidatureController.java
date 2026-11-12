package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import SmartRecruiter.example.SmartRecruiter.service.CandidatureService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SmartRecruiter.example.SmartRecruiter.Dto.CandidatureResponseDTO;
import org.springframework.web.server.ResponseStatusException;

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
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "X-Role", required = false) String role) {

        try {
            // 1. Rôle obligatoire (provisoire, en attendant JWT)
            if (role == null || role.isBlank()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Rôle manquant."));
            }

            // 2. Statut obligatoire
            String statutTexte = body.get("statut");
            if (statutTexte == null || statutTexte.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Le statut est obligatoire."));
            }

            Candidature.Statut statut =
                    Candidature.Statut.valueOf(statutTexte.trim().toUpperCase());

            // 3. Le service contrôle le rôle et la transition
            Candidature candidature =
                    candidatureService.modifierStatut(id, statut, role.trim().toUpperCase());

            return ResponseEntity.ok(new CandidatureResponseDTO(candidature));

        } catch (ResponseStatusException e) {
            // 403 levé par le service : DOIT être avant RuntimeException
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("message", e.getReason()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message",
                            "Statut invalide. Utilisez ENTRETIEN, VALIDATION_RH, ACCEPTEE, "
                                    + "REJETEE ou REFUSE_ENTRETIEN."));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }}
