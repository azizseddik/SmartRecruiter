package SmartRecruiter.example.SmartRecruiter.SmartRecture.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv")
@CrossOrigin(origins = "*")
public class CVController {
    @PostMapping("/test")
    public ResponseEntity<String> testerCV() {

        return ResponseEntity.ok(
                "CV Controller fonctionne correctement"
        );
    }

}
