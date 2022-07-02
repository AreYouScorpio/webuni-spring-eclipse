package hu.webuni.airport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public static final String ISSUER = "AirportApp";
    public static final String AUTH = "auth";
    private Algorithm alg = Algorithm.HMAC256("mysecret");

    public String createJwtToken(UserDetails principal) {

        return JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim(AUTH,
                        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(2)))
                .withIssuer(ISSUER)
                .sign(alg);
    }

    public UserDetails parseJwt(String jwtToken) {

        DecodedJWT decodedJwt = JWT.require(alg)
                .withIssuer(ISSUER)
                .build()
                .verify(jwtToken);
        return new User(decodedJwt.getSubject(), "dummy_barmi_lehet", decodedJwt.getClaim(AUTH).asList(String.class).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())); // user jelszó kéne, de mivel most végezzük az autentikációt, később úgysem használjuk, de am később innét tudná
    }
}
