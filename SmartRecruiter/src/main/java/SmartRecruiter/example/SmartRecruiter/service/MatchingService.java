package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.MatchingResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.CandidatSkill;
import SmartRecruiter.example.SmartRecruiter.Entity.Offre;
import SmartRecruiter.example.SmartRecruiter.Entity.OffreSkill;
import SmartRecruiter.example.SmartRecruiter.Exception.ResourceNotFoundException;
import SmartRecruiter.example.SmartRecruiter.Repostitory.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

@Service
public class MatchingService {

    private final CandidatRepository candidatRepository;
    private final OffreRepository offreRepository;

    public MatchingService(
            CandidatRepository candidatRepository,
            OffreRepository offreRepository) {
        this.candidatRepository = candidatRepository;
        this.offreRepository = offreRepository;
    }

    // Calculer le matching entre un candidat et une offre
    public MatchingResponseDTO calculerMatching(
            Long candidatId, Long offreId) {

        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Candidat introuvable avec l'id : " + candidatId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Offre introuvable avec l'id : " + offreId));

        MatchingResponseDTO response = new MatchingResponseDTO();
        response.setCandidatId(candidat.getId());
        response.setOffreId(offre.getId());

        double scoreSkills = analyserCompetences(candidat, offre, response);
        double scoreDomaine = calculerScoreDomaine(candidat, offre);
        double scoreExperience = calculerScoreExperience(candidat, offre);

        double score = scoreDomaine * 0.20
                + scoreExperience * 0.30
                + scoreSkills * 0.50;

        score = Math.round(score * 100.0) / 100.0;

        response.setScore(score);
        response.setResultat(determinerResultat(score));

        return response;
    }

    // Classer tous les candidats pour une offre
    public List<MatchingResponseDTO> classerCandidats(Long offreId) {

        if (!offreRepository.existsById(offreId)) {
            throw new ResourceNotFoundException(
                    "Offre introuvable avec l'id : " + offreId);
        }

        return candidatRepository.findAll().stream()
                .map(candidat ->
                        calculerMatching(candidat.getId(), offreId))
                .sorted(Comparator.comparing(
                        MatchingResponseDTO::getScore,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    private double analyserCompetences(
            Candidat candidat,
            Offre offre,
            MatchingResponseDTO response) {

        List<OffreSkill> competencesRequises = offre.getSkills();
        List<CandidatSkill> competencesCandidat = candidat.getSkills();

        if (competencesRequises == null || competencesRequises.isEmpty()) {
            response.setCompetencesSatisfaites(List.of());
            response.setCompetencesManquantes(List.of());
            return 100.0;
        }

        List<String> satisfaites = new java.util.ArrayList<>();
        List<String> manquantes = new java.util.ArrayList<>();

        for (OffreSkill offreSkill : competencesRequises) {

            if (offreSkill.getSkill() == null
                    || offreSkill.getSkill().getNom() == null) {
                continue;
            }

            String nomRequis = offreSkill.getSkill().getNom().trim();

            boolean trouvee = false;

            if (competencesCandidat != null) {
                for (CandidatSkill candidatSkill : competencesCandidat) {

                    if (candidatSkill.getSkill() == null
                            || candidatSkill.getSkill().getNom() == null) {
                        continue;
                    }

                    String nomCandidat =
                            candidatSkill.getSkill().getNom().trim();

                    if (!normaliser(nomRequis).equals(normaliser(nomCandidat))) {
                        continue;
                    }

                    Integer niveauRequis = offreSkill.getNiveauRequis();
                    Integer niveauCandidat = candidatSkill.getNiveau();

                    // Si aucun niveau n'est exigé, la compétence suffit.
                    // Sinon, le niveau du candidat doit être renseigné
                    // et supérieur ou égal au niveau requis.
                    if (niveauRequis == null
                            || (niveauCandidat != null
                            && niveauCandidat >= niveauRequis)) {
                        trouvee = true;
                        break;
                    }
                }
            }

            if (trouvee) {
                if (!contientIgnoreCase(satisfaites, nomRequis)) {
                    satisfaites.add(nomRequis);
                }
            } else {
                if (!contientIgnoreCase(manquantes, nomRequis)) {
                    manquantes.add(nomRequis);
                }
            }
        }

        response.setCompetencesSatisfaites(satisfaites);
        response.setCompetencesManquantes(manquantes);

        int total = satisfaites.size() + manquantes.size();

        if (total == 0) {
            return 100.0;
        }

        return (double) satisfaites.size() / total * 100.0;
    }

    private boolean contientIgnoreCase(List<String> liste, String nom) {
        return liste.stream()
                .anyMatch(element -> normaliser(element).equals(normaliser(nom)));
    }

    private String normaliser(String valeur) {
        return valeur.trim().toLowerCase(Locale.ROOT);
    }

    private double calculerScoreDomaine(Candidat candidat, Offre offre) {
        if (candidat.getDomaine() == null || offre.getDomaine() == null) {
            return 0.0;
        }

        return normaliser(candidat.getDomaine())
                .equals(normaliser(offre.getDomaine())) ? 100.0 : 0.0;
    }

    private double calculerScoreExperience(Candidat candidat, Offre offre) {
        if (candidat.getExperience() == null
                || offre.getExperienceMin() == null) {
            return 0.0;
        }

        int experience = candidat.getExperience();
        int minimum = offre.getExperienceMin();

        if (minimum <= 0) {
            return 100.0;
        }

        return Math.min((double) experience / minimum * 100.0, 100.0);
    }

    private String determinerResultat(double score) {
        if (score >= 90) {
            return "Excellent match";
        }
        if (score >= 75) {
            return "Très bon match";
        }
        if (score >= 60) {
            return "Match moyen";
        }
        return "Faible compatibilité";
    }
}
