package com.app.bloodbank.security; // Change to your package
import com.app.bloodbank.model.Users;
import com.app.bloodbank.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "uM9mIXvN+eZh7MbG2oPxz+WZ0z5QZcx+EeOZ6hG8e2Y="; // base64-encoded 32 bytes

    @Autowired
    private UserRepository userRepository;

    // Extract username (subject) from JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Generic method to extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate JWT from user details, including extra user info as claims
    public String generateToken(UserDetails userDetails) {
        // Fetch user entity to get extra details
        Users user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Map<String, Object> extraClaims = new HashMap<>();
        if (user != null) {
            extraClaims.put("fullName", user.getFullName());
            extraClaims.put("role", user.getRole() != null ? user.getRole().name() : null);
            extraClaims.put("email", user.getEmail());
            extraClaims.put("id", user.getId());
            extraClaims.put("expiration", System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 24h
            // Add more user details as needed
        }
        return generateToken(extraClaims, userDetails);
    }

    // Generate JWT with extra claims
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate JWT
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Parse JWT claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get signing key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}