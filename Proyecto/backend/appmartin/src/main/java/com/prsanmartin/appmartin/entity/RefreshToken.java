package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "RefreshTokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRefreshToken")
    private Integer idRefreshToken;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "Token", nullable = false, unique = true, length = 255)
    private String token;
    
    @Column(name = "IssuedAt")
    private LocalDateTime issuedAt;
    
    @Column(name = "ExpiresAt", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "Revoked")
    private Boolean revoked = false;

    public RefreshToken(int id, String token, Usuario usuario, Instant fechaCreacion, Instant fechaExpiracion, boolean usado) {
        this.idRefreshToken = id;
        this.token = token;
        this.usuario = usuario;
        this.issuedAt = LocalDateTime.ofInstant(fechaCreacion, java.time.ZoneId.systemDefault());
        this.expiresAt = LocalDateTime.ofInstant(fechaExpiracion, java.time.ZoneId.systemDefault());
        this.revoked = !usado;
    }
}
