package example.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import example.model.AppUser;

/**
 * {@AuditorAware} to set @CreatedBy and @LastModifiedBy.
 */
public class AppUserAuditorAware implements AuditorAware<AppUser> {
    @Override
    public Optional<AppUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority instanceof AppUserGrantedAuthority appUserGrantedAuthority) {
                    AppUser appUser = new AppUser();
                    appUser.setId(appUserGrantedAuthority.getId());
                    appUser.setUsername(authentication.getName());
                    return Optional.of(appUser);
                }
            }
        }
        return Optional.empty();
    }

}
