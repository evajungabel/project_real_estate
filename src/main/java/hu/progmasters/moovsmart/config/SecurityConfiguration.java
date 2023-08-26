package hu.progmasters.moovsmart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .cors()
            .and()
                .csrf().disable()
            .httpBasic()
                .and().authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true);
        //@formatter:on
    }

    @Override
    public UserDetailsService userDetailsService() {
        UserDetails defaultUser = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles(CustomUserRole.ROLE_ADMIN.getRole())
                .build();
        UserDetails adminUser = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(CustomUserRole.ROLE_ADMIN.getRole())
                .build();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(defaultUser);
        manager.createUser(adminUser);
        return manager;
    }

}
