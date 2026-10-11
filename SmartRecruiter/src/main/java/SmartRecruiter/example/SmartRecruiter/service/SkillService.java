package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.SkillRequestDTO;
import SmartRecruiter.example.SmartRecruiter.Dto.SkillResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Entity.Skill;
import SmartRecruiter.example.SmartRecruiter.Repostitory.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // Ajouter une compétence
    public SkillResponseDTO ajouter(SkillRequestDTO dto) {

        Skill skill = new Skill();

        skill.setNom(dto.getNom());

        Skill skillSauvegardee = skillRepository.save(skill);

        return convertirEnResponse(skillSauvegardee);
    }

    // Récupérer toutes les compétences
    public List<SkillResponseDTO> getToutesLesSkills() {

        return skillRepository.findAll()
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }

    // Récupérer une compétence par ID
    public SkillResponseDTO getSkillById(Long id) {

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Skill introuvable avec l'id : " + id)
                );

        return convertirEnResponse(skill);
    }

    // Modifier une compétence
    public SkillResponseDTO modifier(Long id, SkillRequestDTO dto) {

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Skill introuvable avec l'id : " + id)
                );

        skill.setNom(dto.getNom());

        Skill skillModifiee = skillRepository.save(skill);

        return convertirEnResponse(skillModifiee);
    }

    // Supprimer une compétence
    public void supprimer(Long id) {

        if (!skillRepository.existsById(id)) {
            throw new RuntimeException(
                    "Skill introuvable avec l'id : " + id
            );
        }

        skillRepository.deleteById(id);
    }

    // Conversion Entity → ResponseDTO
    private SkillResponseDTO convertirEnResponse(Skill skill) {

        SkillResponseDTO dto = new SkillResponseDTO();

        dto.setId(skill.getId());
        dto.setNom(skill.getNom());

        return dto;
    }
}
