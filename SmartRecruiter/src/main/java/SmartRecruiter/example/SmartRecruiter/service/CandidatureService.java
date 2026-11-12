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

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class CandidatureService {

    private static final Logger log =
            LoggerFactory.getLogger(CandidatureService.class);

    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final OffreRepository offreRepository;
    private final MatchingService matchingService;
    private final EmailService emailService;

    public CandidatureService(
            CandidatureRepository candidatureRepository,
            CandidatRepository candidatRepository,
            OffreRepository offreRepository,
            MatchingService matchingService,
            EmailService emailService) {

        this.candidatureRepository = candidatureRepository;
        this.candidatRepository = candidatRepository;
        this.offreRepository = offreRepository;
        this.matchingService = matchingService;
        this.emailService = emailService;
    }

    @Transactional
    public Candidature creerCandidature(Long candidatId, Long offreId) {
        return creerCandidature(candidatId, offreId, null);
    }

    @Transactional
    public Candidature creerCandidature(
            Long candidatId,
            Long offreId,
            String cheminCV
    ) {
        var matching = matchingService.calculerMatching(candidatId, offreId);

        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() ->
                        new RuntimeException("Candidat introuvable : " + candidatId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() ->
                        new RuntimeException("Offre introuvable : " + offreId));

        if (candidatureRepository
                .findByCandidatIdAndOffreId(candidatId, offreId)
                .isPresent()) {
            throw new IllegalStateException(
                    "Ce candidat possède déjà une candidature pour cette offre."
            );
        }

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setStatut(Candidature.Statut.EN_ATTENTE);
        candidature.setScoreMatching(matching.getScore());
        candidature.setCheminCV(cheminCV);

        Candidature sauvegardee = candidatureRepository.save(candidature);

        try {
            emailService.envoyerAccuseReception(
                    candidat.getEmail(),
                    candidat.getNom(),
                    offre.getTitre()
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'accusé de réception.", e);
        }

        return sauvegardee;
    }

    @Transactional(readOnly = true)
    public List<Candidature> obtenirToutes() {
        return candidatureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Candidature> obtenirParOffre(Long offreId) {
        return candidatureRepository.findByOffreId(offreId);
    }

    @Transactional
    public Candidature modifierStatut(
            Long candidatureId,
            Candidature.Statut nouveauStatut,
            String roleUtilisateur) {

        if (nouveauStatut == null) {
            throw new IllegalArgumentException("Le nouveau statut est obligatoire.");
        }

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException(
                        "Candidature introuvable : " + candidatureId));

        Candidature.Statut ancienStatut = candidature.getStatut();

        if (ancienStatut == nouveauStatut) {
            return candidature;
        }

        // NOUVEAU : seul le bon rôle peut agir à cette étape
        String roleAttendu = ancienStatut.roleAutorise();
        if (roleAttendu == null) {
            throw new IllegalStateException("Décision déjà finale : " + ancienStatut);
        }
        if (!roleAttendu.equals(roleUtilisateur)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cette étape est réservée au rôle " + roleAttendu);
        }

        if (!ancienStatut.suivants().contains(nouveauStatut)) {
            throw new IllegalStateException(
                    "Transition interdite : " + ancienStatut + " vers " + nouveauStatut);
        }

        candidature.setStatut(nouveauStatut);
        Candidature sauvegardee =
                candidatureRepository.save(candidature);

        Candidat candidat = sauvegardee.getCandidat();
        Offre offre = sauvegardee.getOffre();

        String email = candidat.getEmail();
        String nom = candidat.getNom();
        String titre = offre.getTitre();

        try {
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException(
                        "Le candidat ne possède pas d'adresse e-mail.");
            }

            switch (nouveauStatut) {
                case ENTRETIEN ->
                        emailService.envoyerEntretien(email, nom, titre);

                case REJETEE ->
                        emailService.envoyerRefus(email, nom, titre);

                case ACCEPTEE ->
                        emailService.envoyerAcceptation(email, nom, titre);

                case REFUSE_ENTRETIEN ->
                        emailService.envoyerRefusApresEntretien(
                                email, nom, titre);

                default -> {
                    // Aucun e-mail spécifique pour les autres statuts
                }
            }

            log.info(
                    "E-mail envoyé pour la candidature {} avec le statut {}",
                    candidatureId, nouveauStatut);

        } catch (Exception e) {
            // Le statut reste enregistré si l'envoi échoue
            log.error(
                    "Échec d'envoi du mail pour la candidature {} : {}",
                    candidatureId, e.getMessage(), e);
        }

        return sauvegardee;
    }
}
