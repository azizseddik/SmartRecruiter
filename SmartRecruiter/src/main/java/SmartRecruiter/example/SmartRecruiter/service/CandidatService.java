package SmartRecruiter.example.SmartRecruiter.service;

import SmartRecruiter.example.SmartRecruiter.Dto.CandidatRequestDTO;
import SmartRecruiter.example.SmartRecruiter.Dto.CandidatResponseDTO;
import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Exception.ResourceNotFoundException;
import SmartRecruiter.example.SmartRecruiter.Repostitory.CandidatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class CandidatService {
    private final CandidatRepository candidatRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile( "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$" ); public CandidatService(CandidatRepository candidatRepository) { this.candidatRepository = candidatRepository; }
    // Ajouter un candidat
    public CandidatResponseDTO ajouter(CandidatRequestDTO dto) { validerCandidat(dto); String email = dto.getEmail().trim(); if (candidatRepository.existsByEmailIgnoreCase(email)) { throw new IllegalArgumentException( "Un candidat utilise déjà cet email." ); }
        Candidat candidat = new Candidat(); candidat.setNom(dto.getNom().trim()); candidat.setEmail(email); candidat.setTelephone(dto.getTelephone()); candidat.setDomaine(dto.getDomaine()); candidat.setTexteCV(dto.getTexteCV()); candidat.setExperience(dto.getExperience()); return convertirEnResponse( candidatRepository.save(candidat) ); }
    // Récupérer tous les candidats
    @Transactional(readOnly = true)
    public List<CandidatResponseDTO> getTousLesCandidats() { return candidatRepository.findAll() .stream() .map(this::convertirEnResponse) .toList(); }
    // Récupérer un candidat par son ID
    @Transactional(readOnly = true)
    public CandidatResponseDTO getCandidatById(Long id) { return convertirEnResponse(trouverCandidat(id)); }
    // Modifier un candidat
    public CandidatResponseDTO modifier( Long id, CandidatRequestDTO dto) { validerCandidat(dto); Candidat candidat = trouverCandidat(id); String email = dto.getEmail().trim(); if (candidatRepository .existsByEmailIgnoreCaseAndIdNot(email, id)) { throw new IllegalArgumentException( "Cet email est déjà utilisé par un autre candidat." ); } candidat.setNom(dto.getNom().trim()); candidat.setEmail(email); candidat.setTelephone(dto.getTelephone()); candidat.setDomaine(dto.getDomaine()); candidat.setTexteCV(dto.getTexteCV()); candidat.setExperience(dto.getExperience()); return convertirEnResponse( candidatRepository.save(candidat) ); }
    // Supprimer un candidat
    public void supprimer(Long id) { Candidat candidat = trouverCandidat(id); candidatRepository.delete(candidat); }
    // Rechercher par nom, email ou domaine
    @Transactional(readOnly = true) public List<CandidatResponseDTO> rechercher(String motCle) { if (motCle == null || motCle.isBlank()) { throw new IllegalArgumentException( "Le mot-clé de recherche est obligatoire." ); } String recherche = motCle.trim(); return candidatRepository .findByNomContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDomaineContainingIgnoreCase( recherche, recherche, recherche ) .stream() .map(this::convertirEnResponse) .toList(); }
    // Valider les données reçues
    private void validerCandidat(CandidatRequestDTO dto) { if (dto == null) { throw new IllegalArgumentException( "Les données du candidat sont obligatoires." ); } if (dto.getNom() == null || dto.getNom().isBlank()) { throw new IllegalArgumentException( "Le nom du candidat est obligatoire." ); } if (dto.getEmail() == null || dto.getEmail().isBlank()) { throw new IllegalArgumentException( "L'email du candidat est obligatoire." ); } if (!EMAIL_PATTERN.matcher(dto.getEmail().trim()).matches()) { throw new IllegalArgumentException( "Le format de l'email est invalide." ); } }
    // Chercher un candidat ou signaler son absence
    private Candidat trouverCandidat(Long id) { return candidatRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException( "Candidat introuvable avec l'id : " + id ) ); }
    // Conversion Entity vers ResponseDTO
    private CandidatResponseDTO convertirEnResponse( Candidat candidat) { CandidatResponseDTO dto = new CandidatResponseDTO(); dto.setId(candidat.getId()); dto.setNom(candidat.getNom()); dto.setEmail(candidat.getEmail()); dto.setTelephone(candidat.getTelephone()); dto.setDomaine(candidat.getDomaine()); dto.setTexteCV(candidat.getTexteCV()); dto.setExperience(candidat.getExperience()); return dto; }
}
