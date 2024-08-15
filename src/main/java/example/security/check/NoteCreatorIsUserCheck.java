package example.security.check;

import java.util.Collections;
import java.util.List;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.Path;
import com.yahoo.elide.core.filter.Operator;
import com.yahoo.elide.core.filter.expression.FilterExpression;
import com.yahoo.elide.core.filter.predicates.FilterPredicate;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.FilterExpressionCheck;
import com.yahoo.elide.core.type.Type;

import example.config.AppSecurityProperties;
import example.model.AppUser;
import example.model.Note;

/**
 * {@link FilterExpressionCheck} that the Note createdBy is the user.
 */
@SecurityCheck(NoteCreatorIsUserCheck.NOTE_CREATOR_IS_USER)
public class NoteCreatorIsUserCheck extends FilterExpressionCheck<Note> {
    public static final String NOTE_CREATOR_IS_USER = "Note Creator is User";

    private final AppSecurityProperties appSecurityProperties;

    public NoteCreatorIsUserCheck(AppSecurityProperties appSecurityProperties) {
        this.appSecurityProperties = appSecurityProperties;
    }

    @Override
    public FilterExpression getFilterExpression(Type<?> entityClass, RequestScope requestScope) {
        if (this.appSecurityProperties.isEnabled()) {
            String username = requestScope.getUser().getName();
            Path.PathElement notePath = new Path.PathElement(Note.class, AppUser.class, "createdBy");
            Path.PathElement userPath = new Path.PathElement(AppUser.class, String.class, "username");
            List<Path.PathElement> pathList = List.of(notePath, userPath);
            Path path = new Path(pathList);
            return new FilterPredicate(path, Operator.IN, List.of(username));
        } else {
            Path.PathElement notePath = new Path.PathElement(Note.class, String.class, "title");
            Path path = new Path(List.of(notePath));
            return new FilterPredicate(path, Operator.NOTNULL, Collections.emptyList());
        }
    }
}
