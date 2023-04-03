package io.github.jbgwese.AuthenticationApp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SECRET_KEY = "77397A244326452948404D635166546A576E5A7234753778214125442A472D4A";


    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public<T> T extractClaim(String token, Function<Claims,T>claimResolver){
        var claims = extractAllClaims(token);
                return claimResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);

    }


    public String generateToken(
            Map<String,Object>extraclaims , UserDetails userDetails

    ){
        return  Jwts
                .builder()
                .setClaims(extraclaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*1000*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);

    }

    public boolean isTokenValid(String token, UserDetails userDetails){
    var userEmail =   userDetails.getUsername();
    return (userEmail.equals(extractUsername(token)))&& !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractTokenExpiration(token).before(new Date());
    }

    private Date extractTokenExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }


    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }







}
