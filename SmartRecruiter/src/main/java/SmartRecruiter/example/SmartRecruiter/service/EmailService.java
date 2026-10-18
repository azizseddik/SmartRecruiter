package SmartRecruiter.example.SmartRecruiter.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String expediteur;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerAcceptation(String destinataire, String nomCandidat, String titreOffre) {
        String sujet = "Votre candidature a été acceptée";
        String texte = "Bonjour " + nomCandidat + ",\n\n"
                + "Nous avons le plaisir de vous informer que votre candidature "
                + "pour le poste « " + titreOffre + " » a été acceptée.\n"
                + "Nous vous contacterons très prochainement pour la suite du processus.\n\n"
                + "Cordialement,\n"
                + "L'équipe SmartRecruiter";
        envoyer(destinataire, sujet, texte);
    }

    public void envoyerRefus(String destinataire, String nomCandidat, String titreOffre) {
        String sujet = "Réponse à votre candidature";
        String texte = "Bonjour " + nomCandidat + ",\n\n"
                + "Nous vous remercions de l'intérêt porté au poste « " + titreOffre + " ».\n"
                + "Après étude de votre dossier, nous sommes au regret de vous informer "
                + "que votre candidature n'a pas été retenue.\n"
                + "Nous vous souhaitons plein succès dans vos recherches.\n\n"
                + "Cordialement,\n"
                + "L'équipe SmartRecruiter";
        envoyer(destinataire, sujet, texte);
    }

    private void envoyer(String destinataire, String sujet, String texte) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(expediteur);
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(texte);
        mailSender.send(message);
    }
}
