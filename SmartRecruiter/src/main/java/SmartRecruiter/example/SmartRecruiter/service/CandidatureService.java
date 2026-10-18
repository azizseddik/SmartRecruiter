package SmartRecruiter.example.SmartRecruiter.service;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import SmartRecruiter.example.SmartRecruiter.Entity.Offre;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatureRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.OffreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CandidatureService {
    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final OffreRepository offreRepository;
    private final MatchingService matchingService;
    private static final Logger log = LoggerFactory.getLogger(CandidatureService.class);
    private final EmailService emailService;


    public CandidatureService(
            CandidatureRepository candidatureRepository,
            CandidatRepository candidatRepository,
            OffreRepository offreRepository,
            MatchingService matchingService, EmailService emailService) {

        this.candidatureRepository = candidatureRepository;
        this.candidatRepository = candidatRepository;
        this.offreRepository = offreRepository;
        this.matchingService = matchingService;
        this.emailService =emailService;
    }

    @Transactional
    public Candidature creerCandidature(Long candidatId, Long offreId) {
        var matching = matchingService.calculerMatching(
                candidatId,
                offreId
        );


        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() ->
                        new RuntimeException("Candidat introuvable : " + candidatId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() ->
                        new RuntimeException("Offre introuvable : " + offreId));

        boolean existe = candidatureRepository
                .findByCandidatIdAndOffreId(candidatId, offreId)
                .isPresent();

        if (existe) {
            throw new IllegalStateException(
                    "Ce candidat possède déjà une candidature pour cette offre.");
        }

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setStatut(Candidature.Statut.EN_ATTENTE);
        candidature.setScoreMatching(matching.getScore());
        candidature.setStatut(Candidature.Statut.EN_ATTENTE);


        return candidatureRepository.save(candidature);



    }

    public List<Candidature> obtenirToutes() {
        return candidatureRepository.findAll();
    }

    public List<Candidature> obtenirParOffre(Long offreId) {
        return candidatureRepository.findByOffreId(offreId);
    }

    @Transactional
    public Candidature modifierStatut(Long candidatureId, Candidature.Statut nouveauStatut) {

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable : " + candidatureId));

        Candidature.Statut ancienStatut = candidature.getStatut();
        candidature.setStatut(nouveauStatut);
        Candidature sauvegardee = candidatureRepository.save(candidature);

        // Mail uniquement si le statut a vraiment changé
        if (ancienStatut != nouveauStatut) {
            Candidat candidat = sauvegardee.getCandidat();
            String titreOffre = sauvegardee.getOffre().getTitre(); // adapte au nom réel du champ

            try {
                if (nouveauStatut == Candidature.Statut.ACCEPTEE) {
                    emailService.envoyerAcceptation(candidat.getEmail(), candidat.getNom(), titreOffre);
                } else if (nouveauStatut == Candidature.Statut.REJETEE) {
                    emailService.envoyerRefus(candidat.getEmail(), candidat.getNom(), titreOffre);
                }
            } catch (Exception e) {
                // Le statut reste enregistré même si le mail échoue
                log.error("Échec d'envoi du mail pour la candidature {} : {}", candidatureId, e.getMessage());
            }
        }

        return sauvegardee;
    }
}
