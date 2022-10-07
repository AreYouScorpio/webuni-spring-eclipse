package hu.webuni.airport.config;

import hu.webuni.airport.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //airportcontroller @PutMappingjának a megkérése
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // dao authentication providernek injektálni

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtAuthFilter jwtAuthFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // hogy kódolja is jelszót
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        /* ehelyett újat írunk - DaoAuthenticationProvider-t gyártunk lejjebb

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("user").authorities("user").password(passwordEncoder().encode("pass"))
                .and()
                .withUser("admin").authorities("user", "admin").password(passwordEncoder().encode("pass"));
        // törölni: super.configure(auth);

         */

        // ha megírtuk lent, jöhet ide:

        auth.authenticationProvider(authenticationProvider());


    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //   .httpBasic()    --- offoltuk, ha jwt token van, ne lehessen basic-kel belépni már
                // .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //itt lehet átállítani sessiont, így minden kérésben mennie kell username password párosnak
                .and()
                .authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/**").permitAll() //security kikapcsolása minden rest apira . ha nem kell, ezt a sort törölni
                .antMatchers(HttpMethod.POST, "/api/airports/**").hasAuthority("admin")
                .antMatchers(HttpMethod.PUT, "/api/airports/**").hasAnyAuthority("user", "admin")
                //ha nem akarom különválasztani post get stb szabályokat: .antMatchers("/api/airports/**")
                .anyRequest().authenticated();
        // törölni: super.configure(http);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // kell hozzá autowired JwtAuthFilter jwtAuthFilter fent, a UsernamePasswordAuthenticationFilter elé akarom berakni ezt a filteremet
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService); // ehhez injektálni userDetailsService-t
        return daoAuthenticationProvider;
    }

    ;

    @Override
    @Bean // ezzel kipublikálom az alkalmazásom többi részének, h használhassák
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
