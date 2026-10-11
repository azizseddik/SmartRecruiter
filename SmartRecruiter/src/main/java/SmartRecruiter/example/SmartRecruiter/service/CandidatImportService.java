package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.*;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class CandidatImportService {
    private final AnalyseCVService analyseCVService;
    private final CandidatPersistenceService persistenceService;

    public CandidatImportService(
            AnalyseCVService analyseCVService,
            CandidatPersistenceService persistenceService) {

        this.analyseCVService = analyseCVService;
        this.persistenceService = persistenceService;
    }

    public CandidatImportResponseDTO importerCV(
            MultipartFile fichier) {

        // Étape 1 : envoyer le PDF à FastAPI.
        AnalyseCVResponseDTO analyse =
                analyseCVService.analyserCV(fichier);

        // Étape 2 : enregistrer les données dans MySQL.
        return persistenceService.enregistrer(analyse);
    }

}
