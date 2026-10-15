package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.*;


import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class CandidatImportService {
    private final AnalyseCVService analyseCVService;
    private final CandidatPersistenceService persistenceService;
    private final CandidatureService candidatureService;

    public CandidatImportService(
            AnalyseCVService analyseCVService,
            CandidatPersistenceService persistenceService,CandidatureService candidatureService ) {

        this.analyseCVService = analyseCVService;
        this.persistenceService = persistenceService;
        this.candidatureService = candidatureService;
    }
    public ImportCandidatureResponseDTO importerCVPourOffre(
            MultipartFile fichier,
            Long offreId) {

        // 1. Analyser le PDF et créer ou mettre à jour le candidat.
        CandidatImportResponseDTO candidatImporte = importerCV(fichier);

        // 2. Créer la candidature et calculer son score.
        Candidature candidature = candidatureService.creerCandidature(
                candidatImporte.candidatId(),
                offreId
        );

        // 3. Retourner une réponse sans exposer les entités JPA.
        return new ImportCandidatureResponseDTO(
                candidatImporte,
                new CandidatureResponseDTO(candidature)
        );
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
