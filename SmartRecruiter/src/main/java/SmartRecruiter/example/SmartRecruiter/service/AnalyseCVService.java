package SmartRecruiter.example.SmartRecruiter.service;
import SmartRecruiter.example.SmartRecruiter.Dto.AnalyseCVResponseDTO;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
@Service
public class AnalyseCVService {
    private final RestTemplate restTemplate;

    @Value("${service.ai.url:http://localhost:8000}")
    private String urlServicePython;

    public AnalyseCVService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AnalyseCVResponseDTO analyserCV(MultipartFile fichier) {

        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException(
                    "Veuillez sélectionner un fichier PDF."
            );
        }

        String nomFichier = fichier.getOriginalFilename();

        if (nomFichier == null || nomFichier.isBlank()) {
            nomFichier = "cv.pdf";
        }

        final String nomFichierFinal = nomFichier;

        try {
            // 1. Préparer le fichier PDF
            ByteArrayResource fichierResource =
                    new ByteArrayResource(fichier.getBytes()) {
                        @Override
                        public String getFilename() {
                            return nomFichierFinal;
                        }
                    };

// 2. Construire le corps multipart
            MultiValueMap<String, Object> corps =
                    new LinkedMultiValueMap<>();

// IMPORTANT : FastAPI attend "fichier", pas "file"
            corps.add("fichier", fichierResource);

// 3. Préparer les en-têtes de la requête
            HttpHeaders headersRequete = new HttpHeaders();
            headersRequete.setContentType(
                    MediaType.MULTIPART_FORM_DATA
            );

            HttpEntity<MultiValueMap<String, Object>> requete =
                    new HttpEntity<>(corps, headersRequete);

// 4. Envoyer le PDF à FastAPI
            String url = urlServicePython + "/api/cv/analyze";

            AnalyseCVResponseDTO resultat =
                    restTemplate.postForObject(
                            url,
                            requete,
                            AnalyseCVResponseDTO.class
                    );

            if (resultat == null) {
                throw new IllegalStateException(
                        "Le service Python a retourné une réponse vide."
                );
            }

            return resultat;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossible de lire le fichier PDF.",
                    e
            );

        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "Échec de l'appel à FastAPI. Vérifiez que le service "
                            + "Python fonctionne sur " + urlServicePython
                            + " et que son endpoint accepte le champ 'file'. "
                            + "Détail : " + e.getMessage(),
                    e
            );
        }
    }
}
