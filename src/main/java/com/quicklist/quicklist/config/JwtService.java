package com.quicklist.quicklist.config;

import com.quicklist.quicklist.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret.key}") String secretFilePath) {
        try {
            String secret = new String(Files.readAllBytes(Paths.get(secretFilePath))).trim();
            if (secret.isEmpty()) {
                throw new IllegalArgumentException("La clé secrète ne peut pas être vide");
            }
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création de la clé secrète", e);
        } catch (Exception e) {
            System.out.println("Autre erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création de la clé secrète", e);
        }
    }

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 5;

    /**
     * Génère un token JWT pour un utilisateur donné.
     * @param user Le nom d'utilisateur pour lequel le token est généré.
     * @return Le token JWT généré.
     */
    public String generateToken(User user) {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name()) // Ajouter le rôle de l'utilisateur dans le JWT
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .id(uuidStr)
                .compact();
    }

    /**
     * Valide le token JWT en vérifiant sa signature et sa date d'expiration.
     * @param token Le token JWT à valider.
     * @return true si le token est valide, false sinon.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrait le nom d'utilisateur du token JWT.
     * @param token Le token JWT dont on souhaite extraire le nom d'utilisateur.
     * @return Le nom d'utilisateur extrait du token.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();  // Récupère le sujet (ici le nom d'utilisateur)
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class); // Extraire le rôle de l'utilisateur du JWT
    }

    /**
     * Extrait la date d'expiration du token JWT.
     * @param token Le token JWT dont on souhaite extraire la date d'expiration.
     * @return La date d'expiration du token.
     */
    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();  // Récupère la date d'expiration du token
    }

    /**
     * Vérifie si le token JWT est expiré.
     * @param token Le token JWT à vérifier.
     * @return true si le token est expiré, false sinon.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}