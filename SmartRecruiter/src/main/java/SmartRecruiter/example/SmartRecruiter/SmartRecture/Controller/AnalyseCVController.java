package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import SmartRecruiter.example.SmartRecruiter.Dto.AnalyseCVResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Dto.CandidatImportResponseDTO;
import SmartRecruiter.example.SmartRecruiter.service.AnalyseCVService;
import SmartRecruiter.example.SmartRecruiter.Dto.ImportCandidatureResponseDTO;
import SmartRecruiter.example.SmartRecruiter.service.CandidatImportService;
import SmartRecruiter.example.SmartRecruiter.service.CvStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
@RestController
@RequestMapping("/api/cv")
public class AnalyseCVController {
    private final AnalyseCVService analyseCVService;
    private  final  CandidatImportService candidatImportService;
    private final CvStorageService cvStorageService;

    public AnalyseCVController(
            AnalyseCVService analyseCVService,
            CandidatImportService candidatImportService , CvStorageService cvStorageService ) {

        this.analyseCVService = analyseCVService;
        this.candidatImportService = candidatImportService;
        this.cvStorageService=cvStorageService;
    }

    @PostMapping(
            value = "/importer-candidature",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ImportCandidatureResponseDTO>
    importerCVPourOffre(
            @RequestParam("file") MultipartFile fichier,
            @RequestParam("offreId") Long offreId) {

        ImportCandidatureResponseDTO resultat =
                candidatImportService.importerCVPourOffre(
                        fichier,
                        offreId
                );

        return ResponseEntity.ok(resultat);
    }
    @GetMapping("/fichiers/{nomFichier:.+}")
    public ResponseEntity<Resource> consulterCV(
            @PathVariable String nomFichier
    ) throws java.io.IOException {

        Path chemin = cvStorageService.obtenirChemin(nomFichier);

        if (!Files.isRegularFile(chemin)) {
            return ResponseEntity.notFound().build();
        }

        Resource fichier = new UrlResource(chemin.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"cv.pdf\""
                )
                .body(fichier);
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
