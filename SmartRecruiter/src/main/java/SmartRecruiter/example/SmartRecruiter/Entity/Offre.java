package SmartRecruiter.example.SmartRecruiter.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offres")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 5000)
    private String description;

    private String domaine;

    private Integer experienceMin;

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<OffreSkill> skills = new ArrayList<>();

    public Offre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public Integer getExperienceMin() {
        return experienceMin;
    }

    public void setExperienceMin(Integer experienceMin) {
        this.experienceMin = experienceMin;
    }

    public List<OffreSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<OffreSkill> skills) {
        this.skills = skills;
    }
}
