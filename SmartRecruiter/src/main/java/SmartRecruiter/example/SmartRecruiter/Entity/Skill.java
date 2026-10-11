package SmartRecruiter.example.SmartRecruiter.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill { @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    public Skill() {
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
}
