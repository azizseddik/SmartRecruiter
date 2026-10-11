package SmartRecruiter.example.SmartRecruiter.source;

import SmartRecruiter.example.SmartRecruiter.Entity.Candidat;
import SmartRecruiter.example.SmartRecruiter.Entity.Offre;

import java.util.List;

public interface ProfilSource {

    List<Candidat> rechercher(Offre offre);
}