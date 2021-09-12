package help.sausage.security;

import static org.springframework.http.HttpMethod.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
@Profile("local")
public class SecurityConfig {

    public  static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Order(1)
    @Configuration
    public static class RestSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers(POST, "/api/user").permitAll()
                    .anyRequest().authenticated()
//                    .antMatchers(PATCH,
//                            "/api/review/*",
//                            "/api/review/*/like")
//                    .hasAuthority("USER")
//                    .antMatchers("/api/**","/login", "/").permitAll()
//                    .anyRequest().authenticated();
            ;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(jdbcUserDetailsManager()).passwordEncoder(passwordEncoder())
                    .and()
                    .jdbcAuthentication().dataSource(dataSource);
        }

        @Bean
        public JdbcUserDetailsManager jdbcUserDetailsManager() {
            return new JdbcUserDetailsManager(dataSource);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Order(2)
    @Configuration
    public static class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
//                        .requestCache().requestCache(new CustomRequestCache())
//                    .and()
                        .authorizeRequests()
                        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                        .anyRequest().authenticated()
                    .and()
                        .formLogin()
                        .loginPage(LOGIN_URL).permitAll()
                        .loginProcessingUrl(LOGIN_PROCESSING_URL)
                        .failureUrl(LOGIN_FAILURE_URL)
                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                    .and()
                        .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(
                    // Push
                    "/VaadiServlet/**",

                    // Vaadin Flow static resources
                    "/VAADIN/**",

                    // the standard favicon URI
                    "/favicon.ico",

                    // the robots exclusion standard
                    "/robots.txt",

                    // web application manifest
                    "/manifest.webmanifest",
                    "/sw.js",
                    "/offline-page.html",

                    // icons and images
                    "/icons/**",
                    "/images/**",

                    // (development mode) static resources
                    "/frontend/**",

                    // (development mode) webjars
                    "/webjars/**",

                    // (development mode) H2 debugging console
                    "/h2-console/**",

                    // (production mode) static resources
                    "/frontend-es5/**", "/frontend-es6/**");
        }
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//        .csrf().disable()
//        .requestCache().requestCache(new CustomRequestCache()).and()
//            .authorizeRequests()
//            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
//            .antMatchers(PATCH,
//        "/api/review/*",
//                    "/api/review/*/like")
//                .hasAuthority("USER")
//            .antMatchers("/api/**", "/", "/login").permitAll()
//            .anyRequest().authenticated()
//        .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//            .accessDeniedHandler(backendAuthenticationFailureHandler())
//        .and()
//            .formLogin()
//            .loginPage(LOGIN_URL).permitAll()
//            .loginProcessingUrl(LOGIN_PROCESSING_URL)
//            .failureUrl(LOGIN_FAILURE_URL)
//        .and()
//            .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
////        .and().httpBasic()
//        ;
//    }

    @Bean
    public ObjectMapper securityObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AccessDeniedHandler backendAuthenticationFailureHandler() {
        return new BackendAuthenticationFailureHandler(securityObjectMapper());
    }

}
