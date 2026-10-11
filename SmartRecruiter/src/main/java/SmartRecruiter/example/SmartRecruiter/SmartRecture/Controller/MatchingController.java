package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Dto.*;
import SmartRecruiter.example.SmartRecruiter.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class MatchingController {
    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/{candidatId}/{offreId}")
    public ResponseEntity<MatchingResponseDTO> calculerMatching(
            @PathVariable Long candidatId,
            @PathVariable Long offreId) {

        MatchingResponseDTO resultat =
                matchingService.calculerMatching(candidatId, offreId);

        return ResponseEntity.ok(resultat);
    }
    @GetMapping("/offre/{offreId}/classement")
    public ResponseEntity<List<MatchingResponseDTO>> classerCandidats(
            @PathVariable Long offreId) {

        return ResponseEntity.ok(
                matchingService.classerCandidats(offreId)
        );
    }
}
