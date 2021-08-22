package ua.cafe.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.cafe.services.UserService;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private UserService userDetailsService;

    @Autowired
    public void setService(UserService service) {
        this.userDetailsService = service;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and()
//                .csrf().disable()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests()
                .antMatchers("static/**", "/DishImages/**").permitAll()
                .antMatchers("/","/User/**").permitAll()

                .antMatchers(HttpMethod.GET, "/api/menu").permitAll()
                .antMatchers(HttpMethod.GET, "/api/dish").permitAll()
                .antMatchers(HttpMethod.POST, "/api/dish").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/dish").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/dish").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("ADMIN", "WAITER", "COOK")
                .antMatchers(HttpMethod.GET, "/api/order").hasAnyRole("ADMIN", "WAITER", "COOK")
                .antMatchers(HttpMethod.POST, "/api/order").hasAnyRole("ADMIN", "WAITER")
                .antMatchers(HttpMethod.PUT, "/api/order").hasAnyRole("ADMIN", "WAITER")
                .antMatchers(HttpMethod.DELETE, "/api/order").hasAnyRole("ADMIN", "WAITER")

                .antMatchers(HttpMethod.GET, "/api/detail").hasAnyRole("ADMIN", "WAITER", "COOK")
                .antMatchers(HttpMethod.POST, "/api/detail").hasAnyRole("ADMIN", "WAITER")
                .antMatchers(HttpMethod.PUT, "/api/detail").hasAnyRole("ADMIN", "WAITER")
                .antMatchers(HttpMethod.DELETE, "/api/detail").hasAnyRole("ADMIN", "WAITER")

                .antMatchers("/Director/**").hasRole("ADMIN")
                .antMatchers("/dish_edit","/add_dish","/dish_remove").hasRole("ADMIN")
                .antMatchers("/Waiter/**").hasAnyRole("ADMIN", "WAITER")
                .antMatchers("/Cook/**").hasAnyRole("ADMIN", "COOK")
                .and().formLogin().loginPage("/entrance")
                .successHandler(myAuthenticationSuccessHandler())
                .failureForwardUrl("/entrance").and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        ;

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
