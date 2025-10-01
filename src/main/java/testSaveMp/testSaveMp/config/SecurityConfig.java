package testSaveMp.testSaveMp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/index.html",
                                        "/css/**", "/js/**").permitAll()
                                .anyRequest().hasRole("USER"))
                .formLogin(form -> form
                        .loginPage("/login").loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/download.html", true)
                        .failureUrl("/login?error=true").permitAll()
                ).csrf(csrf -> csrf.ignoringRequestMatchers("/download"));


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        UserDetails user = User.withUsername("u")
                .password("{noop}2876")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}