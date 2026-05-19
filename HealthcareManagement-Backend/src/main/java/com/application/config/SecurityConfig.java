package com.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.application.filter.JwtFilter;
import com.application.service.UserRegistrationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRegistrationService registrationService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(registrationService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/authenticate", "/", "/loginuser", "/logindoctor", "/registeruser", "/registerdoctor", "/addDoctor", "/gettotalusers",
                                "/doctorlist", "/gettotaldoctors", "/gettotalslots", "/acceptstatus/{email}", "/rejectstatus/{email}", "/acceptpatient/{slot}", "/rejectpatient/{slot}",
                                "/addBookingSlots", "/doctorlistbyemail/{email}", "/slotDetails/{email}", "/slotDetails", "/slotDetailsWithUniqueDoctors", "/slotDetailsWithUniqueSpecializations", "/patientlistbydoctoremail/{email}",
                                "/addPrescription", "/doctorProfileDetails/{email}", "/updatedoctor", "/patientlistbydoctoremailanddate/{email}", "/userlist", "/getprescriptionbyname/{patientname}", "/patientlistbyemail/{email}",
                                "/patientlist", "/gettotalpatients", "/gettotalappointments", "/gettotalprescriptions", "/profileDetails/{email}", "/updateuser", "/bookNewAppointment", "/document-vault", "/uploadDocument", "/feedback", "/feedbacks")
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception.accessDeniedHandler(new AccessDeniedHandlerImpl()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
