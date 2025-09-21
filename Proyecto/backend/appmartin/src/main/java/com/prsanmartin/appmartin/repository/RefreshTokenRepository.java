package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    
    Optional<RefreshToken> findByToken(String token);
    
    List<RefreshToken> findByUsuarioIdUsuario(Integer idUsuario);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.usuario.nombreUsuario = :nombreUsuario AND rt.revoked = false")
    List<RefreshToken> findValidByUsuarioNombreUsuario(String nombreUsuario);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.usuario.idUsuario = :idUsuario")
    void revokeAllByUsuarioIdUsuario(Integer idUsuario);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(LocalDateTime now);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.revoked = false AND rt.expiresAt > :now")
    List<RefreshToken> findValidTokens(LocalDateTime now);
}
