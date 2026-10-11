package SmartRecruiter.example.SmartRecruiter.service;

import SmartRecruiter.example.SmartRecruiter.Dto.OffreRequestDTO; import SmartRecruiter.example.SmartRecruiter.Dto.OffreResponseDTO; import SmartRecruiter.example.SmartRecruiter.Entity.Offre; import SmartRecruiter.example.SmartRecruiter.Exception.ResourceNotFoundException; import SmartRecruiter.example.SmartRecruiter.Repostitory.OffreRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service
public class OffreService {
    private final OffreRepository offreRepository;
    public OffreService(OffreRepository offreRepository) { this.offreRepository = offreRepository; }
    // Ajouter une offre
    public OffreResponseDTO ajouter(OffreRequestDTO dto) { validerOffre(dto); Offre offre = new Offre(); offre.setTitre(dto.getTitre().trim()); offre.setDescription(dto.getDescription()); offre.setDomaine(dto.getDomaine().trim()); offre.setExperienceMin(dto.getExperienceMin()); return convertirEnResponse(offreRepository.save(offre)); }
    // Récupérer toutes les offres
    @Transactional(readOnly = true) public List<OffreResponseDTO> getToutesLesOffres() { return offreRepository.findAll() .stream() .map(this::convertirEnResponse) .toList(); }
    // Récupérer une offre par ID
    @Transactional(readOnly = true) public OffreResponseDTO getOffreById(Long id) { return convertirEnResponse(trouverOffre(id)); }
    // Modifier une offre
    public OffreResponseDTO modifier( Long id, OffreRequestDTO dto) { validerOffre(dto); Offre offre = trouverOffre(id); offre.setTitre(dto.getTitre().trim()); offre.setDescription(dto.getDescription()); offre.setDomaine(dto.getDomaine().trim()); offre.setExperienceMin(dto.getExperienceMin()); return convertirEnResponse(offreRepository.save(offre)); }
    // Supprimer une offre
    public void supprimer(Long id) { Offre offre = trouverOffre(id); offreRepository.delete(offre); }
    // Rechercher par titre ou domaine
    @Transactional(readOnly = true) public List<OffreResponseDTO> rechercher(String motCle) { if (motCle == null || motCle.isBlank()) { throw new IllegalArgumentException( "Le mot-clé de recherche est obligatoire." ); } String recherche = motCle.trim(); return offreRepository .findByTitreContainingIgnoreCaseOrDomaineContainingIgnoreCase( recherche, recherche ) .stream() .map(this::convertirEnResponse) .toList(); }
    // Vérifier les données reçues
    private void validerOffre(OffreRequestDTO dto) { if (dto == null) { throw new IllegalArgumentException( "Les données de l'offre sont obligatoires." ); } if (dto.getTitre() == null || dto.getTitre().isBlank()) { throw new IllegalArgumentException( "Le titre de l'offre est obligatoire." ); } if (dto.getDomaine() == null || dto.getDomaine().isBlank()) { throw new IllegalArgumentException( "Le domaine de l'offre est obligatoire." ); } if (dto.getExperienceMin() != null && dto.getExperienceMin() < 0) { throw new IllegalArgumentException( "L'expérience minimale ne peut pas être négative." ); } if (dto.getDescription() != null && dto.getDescription().length() > 5000) { throw new IllegalArgumentException( "La description ne doit pas dépasser 5000 caractères." ); } }
    // Rechercher une offre ou signaler son absence
    private Offre trouverOffre(Long id) { return offreRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException( "Offre introuvable avec l'id : " + id ) ); }
    // Conversion Entity vers ResponseDTO
    private OffreResponseDTO convertirEnResponse(Offre offre) { OffreResponseDTO dto = new OffreResponseDTO(); dto.setId(offre.getId()); dto.setTitre(offre.getTitre()); dto.setDescription(offre.getDescription()); dto.setDomaine(offre.getDomaine()); dto.setExperienceMin(offre.getExperienceMin()); return dto; }
}
