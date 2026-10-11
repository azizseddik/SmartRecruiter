package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;


import SmartRecruiter.example.SmartRecruiter.Dto.*;
import SmartRecruiter.example.SmartRecruiter.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidats")
@CrossOrigin(origins = "*")
public class CandidatController {

    private final  CandidatService candidatService;

    public CandidatController(CandidatService candidatService) {
        this.candidatService = candidatService;
    }

    // Ajouter un candidat
    @PostMapping
    public ResponseEntity<CandidatResponseDTO> ajouter(
            @RequestBody CandidatRequestDTO dto) {

        CandidatResponseDTO candidat =
                candidatService.ajouter(dto);

        return new ResponseEntity<>(
                candidat,
                HttpStatus.CREATED
        );
    }

    // Récupérer tous les candidats
    @GetMapping
    public ResponseEntity<List<CandidatResponseDTO>> getTousLesCandidats() {

        return ResponseEntity.ok(
                candidatService.getTousLesCandidats()
        );
    }
    //Ajouter la recherche dans le contrôleur
    @GetMapping("/recherche")
    public ResponseEntity<List<CandidatResponseDTO>> rechercher(
            @RequestParam("q") String motCle) {

        return ResponseEntity.ok(
                candidatService.rechercher(motCle)
        );
    }

    // Récupérer un candidat par son ID
    @GetMapping("/{id}")
    public ResponseEntity<CandidatResponseDTO> getCandidatById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                candidatService.getCandidatById(id)
        );
    }

    // Modifier un candidat
    @PutMapping("/{id}")
    public ResponseEntity<CandidatResponseDTO> modifier(
            @PathVariable Long id,
            @RequestBody CandidatRequestDTO dto) {

        return ResponseEntity.ok(
                candidatService.modifier(id, dto)
        );
    }

    // Supprimer un candidat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id) {

        candidatService.supprimer(id);

        return ResponseEntity.noContent().build();

    }
}
