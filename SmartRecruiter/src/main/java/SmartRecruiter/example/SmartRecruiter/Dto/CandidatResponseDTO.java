package SmartRecruiter.example.SmartRecruiter.Dto;

public class CandidatResponseDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String domaine;
    private String texteCV;
    private Integer experience;

    public CandidatResponseDTO() {
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
}
