package com.prsanmartin.appmartin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas en milisegundos
    private Long expiration;

    public String generateToken(String username, Integer idAdministrador, String nombreCompleto, Integer nivelPrivilegio) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idAdministrador", idAdministrador);
        claims.put("nombreCompleto", nombreCompleto);
        claims.put("nivelPrivilegio", nivelPrivilegio);
        claims.put("tipo", "ADMINISTRADOR");
        claims.put("sub", username);
        claims.put("iat", System.currentTimeMillis());
        claims.put("exp", System.currentTimeMillis() + expiration);
        
        return createSimpleToken(claims);
    }

    private String createSimpleToken(Map<String, Object> claims) {
        // Crear un token simple en formato Base64
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + claims.get("sub") + 
                        "\",\"idAdministrador\":" + claims.get("idAdministrador") +
                        ",\"nombreCompleto\":\"" + claims.get("nombreCompleto") +
                        "\",\"nivelPrivilegio\":" + claims.get("nivelPrivilegio") +
                        ",\"tipo\":\"" + claims.get("tipo") +
                        "\",\"iat\":" + claims.get("iat") +
                        ",\"exp\":" + claims.get("exp") + "}";
        
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(secret.getBytes());
        
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Parsear JSON simple para extraer el subject
            int subIndex = payload.indexOf("\"sub\":\"");
            if (subIndex == -1) return null;
            
            int start = subIndex + 7;
            int end = payload.indexOf("\"", start);
            return payload.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer extractIdAdministrador(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int idIndex = payload.indexOf("\"idAdministrador\":");
            if (idIndex == -1) return null;
            
            int start = idIndex + 18;
            int end = payload.indexOf(",", start);
            if (end == -1) end = payload.indexOf("}", start);
            
            return Integer.parseInt(payload.substring(start, end));
        } catch (Exception e) {
            return null;
        }
    }

    public Integer extractNivelPrivilegio(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int nivelIndex = payload.indexOf("\"nivelPrivilegio\":");
            if (nivelIndex == -1) return null;
            
            int start = nivelIndex + 18;
            int end = payload.indexOf(",", start);
            if (end == -1) end = payload.indexOf("}", start);
            
            return Integer.parseInt(payload.substring(start, end));
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            if (extractedUsername == null || !extractedUsername.equals(username)) {
                return false;
            }
            
            // Verificar expiración
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int expIndex = payload.indexOf("\"exp\":");
            if (expIndex == -1) return false;
            
            int start = expIndex + 6;
            int end = payload.indexOf("}", start);
            long exp = Long.parseLong(payload.substring(start, end));
            
            return exp > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }
}
