package hu.progmasters.moovsmart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
// A jsr250 azért kell, mert a @Secured az Spring specifikus
// de van egy JavaEE-s párja, a @RoleAllowed, és így az is használható
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
