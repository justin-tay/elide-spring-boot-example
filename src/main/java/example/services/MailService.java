package example.services;

import org.springframework.stereotype.Service;

import example.models.Mail;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending mail.
 * <p>
 * This only logs the mail for demonstration purposes. An actual implementation
 * will use either MailSender or JavaMailSender.
 * <p>
 * This is injected into the LifeCycleHook for Mail.
 *
 * @see example.models.Mail
 */
@Service
@Slf4j
public class MailService {
    /**
     * Sends a mail.
     *
     * @param mail the mail to send
     */
    public void send(Mail mail) {
        log.info("Sending mail from {} to {} with content {} with id {}", mail.getFrom(), mail.getTo(),
                mail.getContent(), mail.getId());
    }
}
