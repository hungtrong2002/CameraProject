package com.camera.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.camera.entity.RefreshToken;
import com.camera.repo.RefreshTokenRepository;
import com.camera.repo.UserRepository;
import com.camera.service.TokenRefreshException;

@Service
public class RefreshTokenService {
	@Value("${bezkoder.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(String userName) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findByUserName(userName));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId));
	}
}
