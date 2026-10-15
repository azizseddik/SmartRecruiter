package SmartRecruiter.example.SmartRecruiter.Entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(
        name = "candidatures",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_candidature_candidat_offre",
                        columnNames = {"candidat_id", "offre_id"}
                )
        }
)
public class Candidature {   public enum Statut {
    EN_ATTENTE,
    ACCEPTEE,
    REJETEE
}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut = Statut.EN_ATTENTE;

    private Double scoreMatching;

    @Column(length = 1000)
    private String cheminCV;

    @Column(nullable = false)
    private LocalDateTime dateCandidature = LocalDateTime.now();

    public Candidature() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Double getScoreMatching() {
        return scoreMatching;
    }

    public void setScoreMatching(Double scoreMatching) {
        this.scoreMatching = scoreMatching;
    }

    public String getCheminCV() {
        return cheminCV;
    }

    public void setCheminCV(String cheminCV) {
        this.cheminCV = cheminCV;
    }

    public LocalDateTime getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(LocalDateTime dateCandidature) {
        this.dateCandidature = dateCandidature;
    }
}
