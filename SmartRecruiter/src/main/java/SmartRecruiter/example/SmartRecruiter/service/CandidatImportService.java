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
    private final CvStorageService cvStorageService;

    public CandidatImportService(
            AnalyseCVService analyseCVService,
            CandidatPersistenceService persistenceService,CandidatureService candidatureService , CvStorageService cvStorageService ) {

        this.analyseCVService = analyseCVService;
        this.persistenceService = persistenceService;
        this.candidatureService = candidatureService;
        this.cvStorageService = cvStorageService;
    }
    public ImportCandidatureResponseDTO importerCVPourOffre(
            MultipartFile fichier,
            Long offreId
    ) {
        // Enregistrer le PDF original.
        String nomFichier = cvStorageService.enregistrer(fichier);

        try {
            // Analyser le CV et enregistrer les informations du candidat.
            CandidatImportResponseDTO candidatImporte = importerCV(fichier);

            // Enregistrer le chemin du PDF dans la candidature.
            Candidature candidature = candidatureService.creerCandidature(
                    candidatImporte.candidatId(),
                    offreId,
                    nomFichier
            );

            return new ImportCandidatureResponseDTO(
                    candidatImporte,
                    new CandidatureResponseDTO(candidature)
            );

        } catch (RuntimeException e) {
            // Éviter de conserver le fichier si l'import échoue.
            try {
                java.nio.file.Files.deleteIfExists(
                        cvStorageService.obtenirChemin(nomFichier)
                );
            } catch (java.io.IOException suppressionErreur) {
                // L'erreur initiale reste prioritaire.
            }

            throw e;
        }
    }
    public CandidatImportResponseDTO importerCV(MultipartFile fichier) {
        AnalyseCVResponseDTO analyse = analyseCVService.analyserCV(fichier);
        return persistenceService.enregistrer(analyse);
    }
}
