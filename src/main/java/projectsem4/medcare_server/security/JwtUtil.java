package projectsem4.medcare_server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import projectsem4.medcare_server.domain.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateTokenUser(User u) {

        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id", u.getId())
                .claim("avatar", u.getUserDetail().getAvatar())
                .claim("firstname", u.getUserDetail().getFirstName())
                .claim("lastname", u.getUserDetail().getLastName())
                .claim("role", u.getUserDetail().getRole())
                .claim("phone", u.getUserDetail().getPhone())
                .claim("balance", u.getUserDetail().getBalance())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10000)) // 10000 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateToken(User u) {
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id", u.getId())
                .claim("avatar", u.getUserDetail().getAvatar())
                .claim("firstname", u.getUserDetail().getFirstName())
                .claim("lastname", u.getUserDetail().getLastName())
                .claim("role", u.getUserDetail().getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateTokenHospital(User u) {
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id", u.getId())
                .claim("avatar", u.getUserDetail().getAvatar())
                .claim("firstname", u.getUserDetail().getFirstName())
                .claim("lastname", u.getUserDetail().getLastName())
                .claim("role", u.getUserDetail().getRole())
                .claim("phone", u.getUserDetail().getPhone())
                .claim("hospital", u.getUserDetail().getHospital().getCode())
                .claim("hospitalId", u.getUserDetail().getHospital().getId())
                .claim("hospitalName", u.getUserDetail().getHospital().getName())
                .claim("hospitalLogo", u.getUserDetail().getHospital().getLogo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, String email) {
        String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}
