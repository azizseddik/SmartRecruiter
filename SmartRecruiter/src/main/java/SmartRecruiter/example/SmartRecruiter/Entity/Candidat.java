package SmartRecruiter.example.SmartRecruiter.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidats")

public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String email;

    private String telephone;

    private String domaine;

    @Column(length = 5000)
    private String texteCV;

    private Integer experience;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<CandidatSkill> skills = new ArrayList<>();

    public Candidat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getTexteCV() {
        return texteCV;
    }

    public void setTexteCV(String texteCV) {
        this.texteCV = texteCV;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<CandidatSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<CandidatSkill> skills) {
        this.skills = skills;
    }
}

