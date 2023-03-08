package tn.esprit.realestate.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.UserRepository;

@Service
public class JwtService {
private UserRepository userRepository;
@Autowired public JwtService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }



    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(),user);
    }

    public String generateToken(
            Map<String, Object> extraClaims,User user
    ) {
        extraClaims.put("roles", user.getAuthorities());
        extraClaims.put("phone", user.getPhone());
        extraClaims.put("email", user.getEmail());



        return Jwts
                .builder()

                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
       /* if ((username.equals(user.getUsername()) && !isTokenExpired(token))) {

            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        } else if ((username.equals(user.getEmail()) && !isTokenExpired(token))) {
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        } else if ((username.equals(user.getPhone()) && !isTokenExpired(token))) {
            return (username.equals(user.getPhone()) && !isTokenExpired(token));
        } */return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

