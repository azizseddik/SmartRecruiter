
        package SmartRecruiter.example.SmartRecruiter.Dto;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidature;
import SmartRecruiter.example.SmartRecruiter.Entity.Formation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private List<FormationDTO> formations = new ArrayList<>();
    private List<String> competences = new ArrayList<>();

    public CandidatureResponseDTO(Candidature candidature) {
        this.id = candidature.getId();

        Candidat candidat = candidature.getCandidat();

        if (candidat != null) {
            this.candidatId = candidat.getId();
            this.nomCandidat = candidat.getNom();
            this.emailCandidat = candidat.getEmail();

            // Récupérer les formations du candidat
            if (candidat.getFormations() != null) {
                this.formations = candidat.getFormations()
                        .stream()
                        .map(FormationDTO::new)
                        .toList();
            }

            // Récupérer les compétences du candidat
            if (candidat.getSkills() != null) {
                this.competences = candidat.getSkills()
                        .stream()
                        .filter(cs -> cs != null && cs.getSkill() != null)
                        .map(cs -> cs.getSkill().getNom())
                        .filter(nom -> nom != null && !nom.isBlank())
                        .distinct()
                        .toList();
            }
        }

        if (candidature.getOffre() != null) {
            this.offreId = candidature.getOffre().getId();
            this.titreOffre = candidature.getOffre().getTitre();
        }

        this.statut = candidature.getStatut() != null
                ? candidature.getStatut().name()
                : null;

        this.scoreMatching = candidature.getScoreMatching();
        this.cheminCV = candidature.getCheminCV();
        this.dateCandidature = candidature.getDateCandidature();
    }

    // DTO des formations
    public static class FormationDTO {

        private String diplome;
        private String etablissement;
        private String anneeDebut;
        private String anneeFin;

        public FormationDTO(Formation formation) {
            this.diplome = formation.getDiplome();
            this.etablissement = formation.getEtablissement();
            this.anneeDebut = formation.getAnneeDebut();
            this.anneeFin = formation.getAnneeFin();
        }

        public String getDiplome() {
            return diplome;
        }

        public String getEtablissement() {
            return etablissement;
        }

        public String getAnneeDebut() {
            return anneeDebut;
        }

        public String getAnneeFin() {
            return anneeFin;
        }
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
    public List<FormationDTO> getFormations() { return formations; }
    public List<String> getCompetences() { return competences; }
}