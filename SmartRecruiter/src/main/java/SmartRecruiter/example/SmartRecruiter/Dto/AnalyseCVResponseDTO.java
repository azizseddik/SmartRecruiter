package SmartRecruiter.example.SmartRecruiter.Dto;
import java.util.List;

public record AnalyseCVResponseDTO(
        String nom_fichier,
        int nombre_pages,
        InformationsDTO informations
) {

    public record InformationsDTO(
            String nom,
            String email,
            String telephone,
            String localisation,
            List<String> competences,
            List<String> langues,
            List<ExperienceDTO> experiences,
            List<FormationDTO> formations
    ) {}

    public record ExperienceDTO(
            String poste,
            String entreprise,
            String date_debut,
            String date_fin,
            Boolean en_cours,
            String description
    ) {}

    public record FormationDTO(
            String diplome,
            String etablissement,
            String annee_debut,
            String annee_fin
    ) {}
}