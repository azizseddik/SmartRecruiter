package SmartRecruiter.example.SmartRecruiter.source;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.Offre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkedInProfilSource implements ProfilSource {

    @Override
    public List<Candidat> rechercher(Offre offre) {

        // L'appel réel à LinkedIn sera ajouté après
        // la configuration OAuth 2.0 et l'accès API LinkedIn.

        return List.of();
    }
}
