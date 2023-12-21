package sg.edu.ntu.nutrimate.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private WebApplicationContext applicationContext;

    // @Autowired
    // private AuthenticationSuccessHandlerImpl successHandler;

    // @Autowired
    // private DataSource dataSource;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // @Autowired
    // private AppBasicAuthenticationEntryPoint authenticationEntryPoint;

    // @Bean
    // public UserDetailsManager users(HttpSecurity http) throws Exception {
    //     AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class)
    //         .userDetailsService(userDetailsService)
    //         .passwordEncoder(passwordEncoder())
    //         .and()
    //         .authenticationProvider(authenticationProvider())
    //         .build();
    // }

    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(CustomUserDetailsService.class);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/nutrimate/public/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/nutrimate/customers/**")
            .hasRole("user")
            .and()
            .authorizeRequests()
            .antMatchers("/nutrimate/admin/**")
            .hasRole("admin")
            .and()
            .authorizeRequests()
            .antMatchers("/login*")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/index")
            .hasAnyRole("user","admin")             
            .anyRequest()
            .authenticated()           
            .and()
            .formLogin()
            // .loginPage("/login.html")
            // .loginProcessingUrl("/perform_login")
            .defaultSuccessUrl("/index", true)
            // .failureUrl("/auth-error?error=true")
            .failureUrl("/login?error=true")
            .failureHandler(authenticationFailureHandler())
            .and()
            .logout()
            // .logoutUrl("/perform_logout")
            .deleteCookies("JSESSIONID");
            // .logoutSuccessHandler(logoutSuccessHandler());
        // .authenticationEntryPoint(authenticationEntryPoint);

        // http.logout((logout) -> logout.logoutUrl("/nutrimate/customers/logout"));
               
        return http.build();
    }

    // @Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    // UserDetails user = User
    // .withUsername("user")
    // .password(passwordEncoder().encode("password"))
    // .roles("user")
    // .build();

    // UserDetails admin = User
    // .withUsername("admin")
    // .password(passwordEncoder().encode("administrator"))
    // .roles("admin")
    // .build();
    // return new InMemoryUserDetailsManager(user, admin);
    // }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}