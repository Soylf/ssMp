package testSaveMp.testSaveMp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/index.html",
                                        "/", "/search/**", "/css/**", "/js/**").permitAll()
                                .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin(form -> form
                        .loginPage("/login").loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/download.html", true)
                        .permitAll()
                ).csrf(csrf -> csrf.ignoringRequestMatchers("/download"));


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        UserDetails user = User.withUsername("User")
                .password("{noop}2876")
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("Admin")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}