package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.*;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.CandidatSkill;
import SmartRecruiter.example.SmartRecruiter.Entity.Experience;
import SmartRecruiter.example.SmartRecruiter.Entity.Formation;
import SmartRecruiter.example.SmartRecruiter.Entity.Skill;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatSkillRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.ExperienceRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.FormationRepository;
import SmartRecruiter.example.SmartRecruiter.Repostitory.SkillRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
@Service
public class CandidatPersistenceService {
    private final CandidatRepository candidatRepository;
    private final ExperienceRepository experienceRepository;
    private final CandidatSkillRepository candidatSkillRepository;
    private final SkillRepository skillRepository;
    private final FormationRepository formationRepository;

    public CandidatPersistenceService(
            CandidatRepository candidatRepository,
            ExperienceRepository experienceRepository,
            CandidatSkillRepository candidatSkillRepository,
            SkillRepository skillRepository,
            FormationRepository formationRepository) {

        this.candidatRepository = candidatRepository;
        this.experienceRepository = experienceRepository;
        this.candidatSkillRepository = candidatSkillRepository;
        this.skillRepository = skillRepository;
        this.formationRepository = formationRepository;
    }

    @Transactional
    public CandidatImportResponseDTO enregistrer(
            AnalyseCVResponseDTO analyse) {

        if (analyse == null || analyse.informations() == null) {
            throw new IllegalArgumentException(
                    "Les informations du CV sont absentes."
            );
        }

        var infos = analyse.informations();

        // 1. Rechercher le candidat par email.
        String email = infos.email();

        Candidat candidat = null;

        if (email != null && !email.isBlank()) {
            candidat = candidatRepository
                    .findByEmailIgnoreCase(email.trim())
                    .orElse(null);
        }

        boolean nouveau = candidat == null;

        if (nouveau) {
            candidat = new Candidat();
        } else {
            // 2. Supprimer les anciennes données liées au candidat.
            Long id = candidat.getId();

            experienceRepository.deleteByCandidat_Id(id);
            candidatSkillRepository.deleteByCandidat_Id(id);
            formationRepository.deleteByCandidat_Id(id);

            candidat.getExperiences().clear();
            candidat.getSkills().clear();
        }

        // 3. Mettre à jour les informations personnelles.
        if (infos.nom() != null && !infos.nom().isBlank()) {
            candidat.setNom(infos.nom().trim());
        }

        if (email != null && !email.isBlank()) {
            candidat.setEmail(email.trim());
        }

        if (infos.telephone() != null
                && !infos.telephone().isBlank()) {
            candidat.setTelephone(infos.telephone().trim());
        }

        // Domaine, texte CV et expérience totale ne sont pas
        // fournis par le JSON actuel : on ne les invente pas.
        candidat = candidatRepository.saveAndFlush(candidat);

        int nombreExperiences = 0;
        int nombreCompetences = 0;
        int nombreFormations = 0;

        // 4. Enregistrer les expériences.
        List<AnalyseCVResponseDTO.ExperienceDTO> experiences =
                infos.experiences() == null
                        ? List.of()
                        : infos.experiences();

        for (var expDTO : experiences) {
            Experience experience = new Experience();

            experience.setPoste(expDTO.poste());
            experience.setEntreprise(expDTO.entreprise());
            experience.setDateDebut(expDTO.date_debut());
            experience.setDateFin(expDTO.date_fin());
            experience.setEnCours(expDTO.en_cours());
            experience.setDescription(expDTO.description());
            experience.setCandidat(candidat);

            experienceRepository.save(experience);
            candidat.getExperiences().add(experience);

            nombreExperiences++;
        }

        // 5. Enregistrer les compétences sans créer de doublons.
        Set<String> competencesTraitees =
                new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        List<String> competences =
                infos.competences() == null
                        ? List.of()
                        : infos.competences();

        for (String nomCompetence : competences) {

            if (nomCompetence == null
                    || nomCompetence.isBlank()) {
                continue;
            }

            String nom = nomCompetence.trim();

            if (!competencesTraitees.add(nom)) {
                continue;
            }

            Skill skill = skillRepository
                    .findByNomIgnoreCase(nom)
                    .orElseGet(() -> {
                        Skill nouvelleSkill = new Skill();
                        nouvelleSkill.setNom(nom);
                        return skillRepository.save(nouvelleSkill);
                    });

            CandidatSkill association = new CandidatSkill();
            association.setCandidat(candidat);
            association.setSkill(skill);

            // Le parseur ne détermine pas le niveau réel
            // ni le nombre d'années par compétence.
            association.setNiveau(null);
            association.setAnneesExperience(null);

            candidatSkillRepository.save(association);
            candidat.getSkills().add(association);

            nombreCompetences++;
        }

        // 6. Enregistrer les formations.
        List<AnalyseCVResponseDTO.FormationDTO> formations =
                infos.formations() == null
                        ? List.of()
                        : infos.formations();

        for (var formationDTO : formations) {

            Formation formation = new Formation();

            formation.setDiplome(formationDTO.diplome());
            formation.setEtablissement(
                    formationDTO.etablissement()
            );
            formation.setAnneeDebut(
                    formationDTO.annee_debut()
            );
            formation.setAnneeFin(
                    formationDTO.annee_fin()
            );
            formation.setCandidat(candidat);

            formationRepository.save(formation);
            nombreFormations++;
        }

        return new CandidatImportResponseDTO(
                candidat.getId(),
                candidat.getNom(),
                candidat.getEmail(),
                nombreExperiences,
                nombreCompetences,
                nombreFormations,
                nouveau
                        ? "Candidat créé et CV importé avec succès."
                        : "Candidat mis à jour avec succès."
        );
    }
}
