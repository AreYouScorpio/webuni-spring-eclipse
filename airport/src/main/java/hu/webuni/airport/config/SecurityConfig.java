package hu.webuni.airport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // hogy kódolja is jelszót
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("user").authorities("user").password(passwordEncoder().encode("pass"))
                .and()
                .withUser("admin").authorities("user", "admin").password(passwordEncoder().encode("pass"));
        // törölni: super.configure(auth);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //itt lehet átállítani sessiont, így minden kérésben mennie kell username password párosnak
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/airports/**").hasAuthority("admin")
                .antMatchers(HttpMethod.PUT, "/api/airports/**").hasAnyAuthority("user", "admin")
                //ha nem akarom különválasztani post get stb szabályokat: .antMatchers("/api/airports/**")
                .anyRequest().authenticated();
        // törölni: super.configure(http);
    }
}
