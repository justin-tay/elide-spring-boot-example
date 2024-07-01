package example.security;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.security.User;
import com.yahoo.elide.core.security.checks.UserCheck;

/**
 * Admin check.
 */
@SecurityCheck(AdminCheck.USER_IS_ADMIN)
public class AdminCheck extends UserCheck {
    public static final String USER_IS_ADMIN = "User is Admin";

    @Override
    public boolean ok(User user) {
        return true;
    }
}
