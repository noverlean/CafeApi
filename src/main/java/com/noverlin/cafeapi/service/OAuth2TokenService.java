package com.noverlin.cafeapi.service;

import com.noverlin.cafeapi.entity.OAuth2Token;
import com.noverlin.cafeapi.repository.OAuth2TokenRepository;
import com.noverlin.cafeapi.util.EncryptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

public class DatabaseOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final OAuth2TokenRepository tokenRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public DatabaseOAuth2AuthorizedClientService(OAuth2TokenRepository tokenRepository, ClientRegistrationRepository clientRegistrationRepository) {
        this.tokenRepository = tokenRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();

        OAuth2Token tokenEntity = new OAuth2Token();
        tokenEntity.setClientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId());
        tokenEntity.setPrincipalName(principal.getName());
        try {
            tokenEntity.setEncryptedAccessToken(EncryptionUtil.encrypt(accessToken.getTokenValue()));
            tokenEntity.setEncryptedRefreshToken(refreshToken != null ? EncryptionUtil.encrypt(refreshToken.getTokenValue()) : null);
            tokenEntity.setAccessTokenExpiresAt(accessToken.getExpiresAt().plus(Duration.ofHours(5)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt token", e);
        }

        tokenRepository.save(tokenEntity);
    }

    @Override
    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
        Optional<OAuth2Token> tokenOptional = tokenRepository.findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);

        if (tokenOptional.isPresent()) {
            OAuth2Token token = tokenOptional.get();
            try {
                String accessTokenValue = EncryptionUtil.decrypt(token.getEncryptedAccessToken());
                String refreshTokenValue = token.getEncryptedRefreshToken() != null ? EncryptionUtil.decrypt(token.getEncryptedRefreshToken()) : null;

                OAuth2AccessToken accessToken = new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        accessTokenValue,
                        token.getAccessTokenExpiresAt(), // Correctly use the stored expiration time
                        token.getAccessTokenExpiresAt().plus(Duration.ofHours(1)), // Adjust if necessary
                        Collections.emptySet() // Set of scopes, adjust if necessary
                );

                OAuth2RefreshToken refreshToken = refreshTokenValue != null ?
                        new OAuth2RefreshToken(refreshTokenValue, Instant.now()) :
                        null;

                return new OAuth2AuthorizedClient(
                        clientRegistrationRepository.findByRegistrationId(clientRegistrationId),
                        principalName,
                        accessToken,
                        refreshToken
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to decrypt token", e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        tokenRepository.deleteByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);
    }
}

