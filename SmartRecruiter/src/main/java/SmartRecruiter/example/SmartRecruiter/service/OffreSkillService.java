package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Entity.*;
import SmartRecruiter.example.SmartRecruiter.Repostitory.*;

import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class OffreSkillService {
    private  OffreSkillRepository offreSkillRepository;
    private  OffreRepository offreRepository;
    private  SkillRepository skillRepository;
    public OffreSkillService( OffreSkillRepository offreSkillRepository, OffreRepository offreRepository, SkillRepository skillRepository) { this.offreSkillRepository = offreSkillRepository; this.offreRepository = offreRepository; this.skillRepository = skillRepository; }
    // Ajouter une compétence à une offre
    public OffreSkill ajouter( Long offreId, Long skillId, Integer niveauRequis, Boolean obligatoire) { Offre offre = offreRepository.findById(offreId) .orElseThrow(() -> new RuntimeException( "Offre introuvable avec l'id : " + offreId)); Skill skill = skillRepository.findById(skillId) .orElseThrow(() -> new RuntimeException( "Skill introuvable avec l'id : " + skillId)); OffreSkill offreSkill = new OffreSkill(); offreSkill.setOffre(offre); offreSkill.setSkill(skill); offreSkill.setNiveauRequis(niveauRequis); offreSkill.setObligatoire(obligatoire); return offreSkillRepository.save(offreSkill); }
    // Récupérer les compétences d'une offre
    public List<OffreSkill> getSkillsByOffre(Long offreId) { return offreSkillRepository.findAll() .stream() .filter(os -> os.getOffre().getId().equals(offreId)) .toList(); }
    // Supprimer une compétence d'une offre
    public void supprimer(Long id) { if (!offreSkillRepository.existsById(id)) { throw new RuntimeException( "OffreSkill introuvable avec l'id : " + id); } offreSkillRepository.deleteById(id); }
}
