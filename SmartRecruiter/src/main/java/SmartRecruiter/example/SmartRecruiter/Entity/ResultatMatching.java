package SmartRecruiter.example.SmartRecruiter.Entity;

import jakarta.persistence.*;

@Entity
public class ResultatMatching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
