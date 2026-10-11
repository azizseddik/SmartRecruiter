package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Dto.AnalyseCVResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Dto.CandidatImportResponseDTO;
import SmartRecruiter.example.SmartRecruiter.service.AnalyseCVService;

import SmartRecruiter.example.SmartRecruiter.service.CandidatImportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/cv")
public class AnalyseCVController {
    private final AnalyseCVService analyseCVService;
    private  final  CandidatImportService candidatImportService;

    public AnalyseCVController(
            AnalyseCVService analyseCVService,
            CandidatImportService candidatImportService) {

        this.analyseCVService = analyseCVService;
        this.candidatImportService = candidatImportService;
    }

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AnalyseCVResponseDTO> analyserCV(
            @RequestParam("file") MultipartFile fichier) {

        AnalyseCVResponseDTO resultat =
                analyseCVService.analyserCV(fichier);

        return ResponseEntity.ok(resultat);
    }
    @PostMapping(
            value = "/importer",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CandidatImportResponseDTO> importerCV(
            @RequestParam("file") MultipartFile fichier) {

        CandidatImportResponseDTO resultat =
                candidatImportService.importerCV(fichier);

        return ResponseEntity.ok(resultat);
    }
}
