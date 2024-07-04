package example.models;

import java.util.Optional;
import java.util.UUID;

import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.annotation.LifeCycleHookBinding.Operation;
import com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase;
import com.yahoo.elide.annotation.ReadPermission;
import com.yahoo.elide.annotation.UpdatePermission;
import com.yahoo.elide.core.lifecycle.LifeCycleHook;
import com.yahoo.elide.core.security.ChangeSpec;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.prefab.Role;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Mail model for performing the mail operation.
 * <p>
 * The permissions are set to only allow POST.
 * 
 * @see example.datastore.OperationDataStore
 */
@Include(name = "mail", description = "Mail.", friendlyName = "Mail")
@Data
@ReadPermission(expression = Role.NONE_ROLE)
@UpdatePermission(expression = Role.NONE_ROLE)
@DeletePermission(expression = Role.NONE_ROLE)
@LifeCycleHookBinding(operation = LifeCycleHookBinding.Operation.CREATE, phase = LifeCycleHookBinding.TransactionPhase.PRECOMMIT, hook = example.models.Mail.MailHook.class)
public class Mail {
    @Id
    @GeneratedValue
    private String id;

    @NotBlank
    private String to = "";

    @NotBlank
    private String from = "";

    @NotBlank
    private String content = "";

    private String replyTo = "";

    @Slf4j
    public static class MailHook implements LifeCycleHook<Mail> {
        @Override
        public void execute(Operation operation, TransactionPhase phase, Mail elideEntity, RequestScope requestScope,
                Optional<ChangeSpec> changes) {
            // Perform mailing
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            log.info("Sending mail from {} to {} with content {} with id {}", elideEntity.getFrom(), elideEntity.getTo(), elideEntity.getContent(), id);
            elideEntity.setId(id);
        }
    }
}
