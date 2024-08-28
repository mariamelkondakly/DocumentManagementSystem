package com.AtosReady.DocumentManagementSystem.Configurations;

import com.AtosReady.DocumentManagementSystem.Exceptions.InvalidSignatureException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWTUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token){

        try{
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (SignatureException e){
            throw new InvalidSignatureException("Invalid JWT signature");
        }
        catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token has expired");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT claims string is empty");
        }
    }

    public Long extractNid(String token){
        return extractAllClaims(token).get("nid",Long.class);
    }
    public Long extractFirstName(String token){
        return extractAllClaims(token).get("firstName",Long.class);
    }
}
