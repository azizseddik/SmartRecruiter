package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.ExperienceRequestDTO;
import SmartRecruiter.example.SmartRecruiter.Dto.ExperienceResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.Experience;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.ExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final CandidatRepository candidatRepository;

    public ExperienceService(
            ExperienceRepository experienceRepository,
            CandidatRepository candidatRepository) {

        this.experienceRepository = experienceRepository;
        this.candidatRepository = candidatRepository;
    }

    // Ajouter une expérience
    public ExperienceResponseDTO ajouter(ExperienceRequestDTO dto) {

        Candidat candidat = candidatRepository.findById(dto.getCandidatId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Candidat introuvable avec l'id : "
                                        + dto.getCandidatId()
                        )
                );

        Experience experience = new Experience();

        experience.setPoste(dto.getPoste());
        experience.setEntreprise(dto.getEntreprise());
        experience.setDateDebut(dto.getDateDebut());
        experience.setDateFin(dto.getDateFin());
        experience.setDescription(dto.getDescription());

        // Associer l'expérience au candidat
        experience.setCandidat(candidat);

        Experience experienceSauvegardee =
                experienceRepository.save(experience);

        return convertirEnResponse(experienceSauvegardee);
    }

    // Récupérer toutes les expériences
    public List<ExperienceResponseDTO> getToutesLesExperiences() {

        return experienceRepository.findAll()
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }

    // Récupérer une expérience par ID
    public ExperienceResponseDTO getExperienceById(Long id) {

        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Experience introuvable avec l'id : " + id
                        )
                );

        return convertirEnResponse(experience);
    }

    // Modifier une expérience
    public ExperienceResponseDTO modifier(
            Long id,
            ExperienceRequestDTO dto) {

        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Experience introuvable avec l'id : " + id
                        )
                );

        Candidat candidat = candidatRepository.findById(dto.getCandidatId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Candidat introuvable avec l'id : "
                                        + dto.getCandidatId()
                        )
                );

        experience.setPoste(dto.getPoste());
        experience.setEntreprise(dto.getEntreprise());
        experience.setDateDebut(dto.getDateDebut());
        experience.setDateFin(dto.getDateFin());
        experience.setDescription(dto.getDescription());
        experience.setCandidat(candidat);

        Experience experienceModifiee =
                experienceRepository.save(experience);

        return convertirEnResponse(experienceModifiee);
    }

    // Supprimer une expérience
    public void supprimer(Long id) {

        if (!experienceRepository.existsById(id)) {
            throw new RuntimeException(
                    "Experience introuvable avec l'id : " + id
            );
        }

        experienceRepository.deleteById(id);
    }

    // Conversion Entity → ResponseDTO
    private ExperienceResponseDTO convertirEnResponse(
            Experience experience) {

        ExperienceResponseDTO dto = new ExperienceResponseDTO();

        dto.setId(experience.getId());
        dto.setPoste(experience.getPoste());
        dto.setEntreprise(experience.getEntreprise());
        dto.setDateDebut(experience.getDateDebut());
        dto.setDateFin(experience.getDateFin());
        dto.setDescription(experience.getDescription());

        if (experience.getCandidat() != null) {
            dto.setCandidatId(experience.getCandidat().getId());
        }

        return dto;
    }
}
