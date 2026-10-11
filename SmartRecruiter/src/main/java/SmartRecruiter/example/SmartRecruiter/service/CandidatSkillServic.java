package SmartRecruiter.example.SmartRecruiter.service;

import SmartRecruiter.example.SmartRecruiter.Repostitory.*;
import org.springframework.stereotype.Service;
import SmartRecruiter.example.SmartRecruiter.Entity.*;
import java.util.List;
@Service
public class CandidatSkillServic {
    private  CandidatSkillRepository candidatSkillRepository;
    private  CandidatRepository candidatRepository;
    private  SkillRepository skillRepository;

    public CandidatSkillServic( CandidatSkillRepository candidatSkillRepository, CandidatRepository candidatRepository, SkillRepository skillRepository)
    { this.candidatSkillRepository = candidatSkillRepository;
        this.candidatRepository = candidatRepository;
        this.skillRepository = skillRepository; }
    // Ajouter une compétence à un candidat
    public CandidatSkill ajouter(
            Long candidatId,
            Long skillId,
            Integer niveau,
            Integer anneesExperience)
    { Candidat candidat = candidatRepository.findById(candidatId) .orElseThrow(() -> new RuntimeException( "Candidat introuvable avec l'id : " + candidatId));
        Skill skill = skillRepository.findById(skillId) .orElseThrow(() -> new RuntimeException( "Skill introuvable avec l'id : " + skillId));
        CandidatSkill candidatSkill = new CandidatSkill(); candidatSkill.setCandidat(candidat); candidatSkill.setSkill(skill);
        candidatSkill.setNiveau(niveau); candidatSkill.setAnneesExperience(anneesExperience); return candidatSkillRepository.save(candidatSkill); }
    // Récupérer les compétences d'un candidat
    public List<CandidatSkill> getSkillsByCandidat(Long candidatId) {
        return candidatSkillRepository.findAll() .stream() .filter(cs -> cs.getCandidat().getId().equals(candidatId)) .toList(); }
    public void supprimer(Long id) {

        if (!candidatSkillRepository.existsById(id)) {
            throw new RuntimeException(
                    "CandidatSkill introuvable avec l'id : " + id);
        }

        candidatSkillRepository.deleteById(id);
    }

}
