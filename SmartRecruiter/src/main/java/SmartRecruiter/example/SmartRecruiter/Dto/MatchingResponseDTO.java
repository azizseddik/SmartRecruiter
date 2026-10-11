package SmartRecruiter.example.SmartRecruiter.Dto;

import java.util.ArrayList;
import java.util.List;

public class MatchingResponseDTO {
    private Long candidatId;
    private Long offreId;
    private Double score;
    private String resultat;
    private List<String> competencesSatisfaites = new ArrayList<>();
    private List<String> competencesManquantes = new ArrayList<>();
    public MatchingResponseDTO() {
    }

    public Long getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }
    public List<String> getCompetencesSatisfaites() {
        return competencesSatisfaites;
    }

    public void setCompetencesSatisfaites(
            List<String> competencesSatisfaites) {
        this.competencesSatisfaites = competencesSatisfaites;
    }

    public List<String> getCompetencesManquantes() {
        return competencesManquantes;
    }

    public void setCompetencesManquantes(
            List<String> competencesManquantes) {
        this.competencesManquantes = competencesManquantes;
    }

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }
}
