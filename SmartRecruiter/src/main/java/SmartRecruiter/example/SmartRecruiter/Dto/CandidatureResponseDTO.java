package SmartRecruiter.example.SmartRecruiter.Dto;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import java.time.LocalDateTime;

public class CandidatureResponseDTO {
    private Long id;
    private Long candidatId;
    private String nomCandidat;
    private String emailCandidat;
    private Long offreId;
    private String titreOffre;
    private String statut;
    private Double scoreMatching;
    private String cheminCV;
    private LocalDateTime dateCandidature;

    public CandidatureResponseDTO(Candidature candidature) {
        this.id = candidature.getId();

        if (candidature.getCandidat() != null) {
            this.candidatId = candidature.getCandidat().getId();
            this.nomCandidat = candidature.getCandidat().getNom();
            this.emailCandidat = candidature.getCandidat().getEmail();
        }

        if (candidature.getOffre() != null) {
            this.offreId = candidature.getOffre().getId();
            this.titreOffre = candidature.getOffre().getTitre();
        }

        this.statut = candidature.getStatut().name();
        this.scoreMatching = candidature.getScoreMatching();
        this.cheminCV = candidature.getCheminCV();
        this.dateCandidature = candidature.getDateCandidature();
    }

    public Long getId() { return id; }
    public Long getCandidatId() { return candidatId; }
    public String getNomCandidat() { return nomCandidat; }
    public String getEmailCandidat() { return emailCandidat; }
    public Long getOffreId() { return offreId; }
    public String getTitreOffre() { return titreOffre; }
    public String getStatut() { return statut; }
    public Double getScoreMatching() { return scoreMatching; }
    public String getCheminCV() { return cheminCV; }
    public LocalDateTime getDateCandidature() { return dateCandidature; }
}
