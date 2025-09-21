package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.entity.RefreshToken;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.RefreshTokenRepository;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    public RefreshToken crearRefreshToken(String nombreUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado: " + nombreUsuario);
        }

        Usuario usuario = usuarioOpt.get();
        
        // Revocar tokens existentes del usuario
        refreshTokenRepository.revokeAllByUsuarioIdUsuario(usuario.getIdUsuario());

        // Crear nuevo token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setIssuedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verificarRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        
        if (refreshTokenOpt.isEmpty()) {
            throw new RuntimeException("Refresh token no encontrado");
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        
        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token revocado");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return refreshToken;
    }

    public void revocarToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenOpt.get();
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    public void revocarTodosLosTokens(String nombreUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuarioOpt.isPresent()) {
            refreshTokenRepository.revokeAllByUsuarioIdUsuario(usuarioOpt.get().getIdUsuario());
        }
    }

    public void limpiarTokensExpirados() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

    public List<RefreshToken> obtenerTokensValidos(String nombreUsuario) {
        return refreshTokenRepository.findValidByUsuarioNombreUsuario(nombreUsuario);
    }
}
