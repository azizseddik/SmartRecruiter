package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;

import SmartRecruiter.example.SmartRecruiter.Dto.*;

import SmartRecruiter.example.SmartRecruiter.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
@CrossOrigin(origins = "*")
public class OffreController {
    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }
    // Ajouter une offre
    @PostMapping
    public ResponseEntity<OffreResponseDTO> ajouter(
            @RequestBody OffreRequestDTO dto) {

        OffreResponseDTO offre = offreService.ajouter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(offre);
    }

    // Récupérer toutes les offres
    @GetMapping
    public ResponseEntity<List<OffreResponseDTO>> getToutesLesOffres() {
        return ResponseEntity.ok(offreService.getToutesLesOffres());
    }

    // Récupérer une offre par son ID
    @GetMapping("/{id}")
    public ResponseEntity<OffreResponseDTO> getOffreById(
            @PathVariable Long id) {

        return ResponseEntity.ok(offreService.getOffreById(id));
    }

    // Modifier une offre
    @PutMapping("/{id}")
    public ResponseEntity<OffreResponseDTO> modifier(
            @PathVariable Long id,
            @RequestBody OffreRequestDTO dto) {

        return ResponseEntity.ok(offreService.modifier(id, dto));
    }

    // Supprimer une offre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        offreService.supprimer(id);
        return ResponseEntity.noContent().build();
    }   
}
