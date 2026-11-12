package SmartRecruiter.example.SmartRecruiter.service;

import SmartRecruiter.example.SmartRecruiter.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;

@Service
public class CvStorageService {
    private final Path dossierCV;

    public CvStorageService(
            @Value("${app.cv.storage:./uploads/cv}") String dossier
    ) {
            this.dossierCV = Paths.get(dossier)
                    .toAbsolutePath()
                    .normalize();
        }

        public String enregistrer(MultipartFile fichier) {
            if (fichier == null || fichier.isEmpty()) {
                throw new IllegalArgumentException("Le fichier CV est obligatoire.");
            }

            if (fichier.getSize() > 10L * 1024 * 1024) {
                throw new IllegalArgumentException(
                        "Le CV ne doit pas dépasser 10 Mo."
                );
            }

            try {
                byte[] contenu = fichier.getBytes();

                // Vérifier la signature PDF, pas seulement le nom du fichier.
                if (contenu.length < 5
                        || contenu[0] != '%'
                        || contenu[1] != 'P'
                        || contenu[2] != 'D'
                        || contenu[3] != 'F'
                        || contenu[4] != '-') {
                    throw new IllegalArgumentException(
                            "Le fichier fourni n'est pas un PDF valide."
                    );
                }

                Files.createDirectories(dossierCV);

                String nomFichier = UUID.randomUUID() + ".pdf";
                Path destination = dossierCV.resolve(nomFichier).normalize();

                if (!destination.startsWith(dossierCV)) {
                    throw new IllegalArgumentException("Chemin de fichier invalide.");
                }

                Files.write(destination, contenu);

                return nomFichier;

            } catch (IOException e) {
                throw new IllegalStateException(
                        "Impossible d'enregistrer le fichier CV.", e
                );
            }
        }

        public Path obtenirChemin(String nomFichier) {
            if (nomFichier == null
                    || !nomFichier.matches(
                    "[a-fA-F0-9-]{36}\\.pdf"
            )) {
                throw new IllegalArgumentException("Nom de fichier invalide.");
            }

            Path chemin = dossierCV.resolve(nomFichier).normalize();

            if (!chemin.startsWith(dossierCV)) {
                throw new IllegalArgumentException("Chemin de fichier invalide.");
            }

            return chemin;
        }
}
