package SmartRecruiter.example.SmartRecruiter.Dto;

public record CandidatImportResponseDTO( Long candidatId,
                                         String nom,
                                         String email,
                                         int nombreExperiences,
                                         int nombreCompetences,
                                         int nombreFormations,
                                         String message
){
}
