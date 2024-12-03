package com.noverlin.cafeapi.service;

//import com.noverlin.cafeapi.entity.OAuth2Token;
//import com.noverlin.cafeapi.repository.OAuth2TokenRepository;
//import com.noverlin.cafeapi.util.ApplicationContextHolder;
//import com.noverlin.cafeapi.util.EncryptionUtil;
//import jakarta.annotation.PostConstruct;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2RefreshToken;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Optional;

//public class DatabaseOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {
//
//    private final OAuth2TokenRepository tokenRepository;
//    private final ClientRegistrationRepository clientRegistrationRepository;
//
//    public DatabaseOAuth2AuthorizedClientService(OAuth2TokenRepository tokenRepository, ClientRegistrationRepository clientRegistrationRepository) {
//        this.tokenRepository = tokenRepository;
//        this.clientRegistrationRepository = clientRegistrationRepository;
//        System.out.println("CustomOAuth2AuthorizedClientService initialized in context: " + ApplicationContextHolder.getContext());
//    }
//
//    @Override
//    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
//        Optional<OAuth2Token> tokenOptional = tokenRepository.findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);
//        System.out.println("Custom OAuth2AuthorizedClient loaded");
//        return tokenOptional.map(token -> {
//            try {
//                String accessToken = EncryptionUtil.decrypt(token.getEncryptedAccessToken());
//                String refreshToken = EncryptionUtil.decrypt(token.getEncryptedRefreshToken());
//
//                OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
//                        OAuth2AccessToken.TokenType.BEARER,
//                        accessToken,
//                        token.getAccessTokenExpiresAt(),
//                        token.getAccessTokenExpiresAt().plus(Duration.ofHours(1))
//                );
//
//                OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(refreshToken, token.getAccessTokenExpiresAt());
//
//                // Получаем объект ClientRegistration
//                ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
//
//                return new OAuth2AuthorizedClient(
//                        clientRegistration,
//                        principalName,
//                        oAuth2AccessToken,
//                        oAuth2RefreshToken
//                );
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to decrypt token", e);
//            }
//        }).orElse(null);
//    }
//
//    @Override
//    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
//        try {
//            String encryptedAccessToken = EncryptionUtil.encrypt(authorizedClient.getAccessToken().getTokenValue());
//
//            String encryptedRefreshToken = null;
//            if (authorizedClient.getRefreshToken() != null) {
//                encryptedRefreshToken = EncryptionUtil.encrypt(authorizedClient.getRefreshToken().getTokenValue());
//            }
//
//            // Проверка на существование записи
//            Optional<OAuth2Token> existingTokenOptional = tokenRepository.findByClientRegistrationIdAndPrincipalName(
//                    authorizedClient.getClientRegistration().getRegistrationId(),
//                    principal.getName()
//            );
//
//            OAuth2Token token;
//            if (existingTokenOptional.isPresent()) {
//                // Обновление существующей записи
//                token = existingTokenOptional.get();
//                token.setEncryptedAccessToken(encryptedAccessToken);
//                token.setEncryptedRefreshToken(encryptedRefreshToken);
//                token.setAccessTokenExpiresAt(authorizedClient.getAccessToken().getExpiresAt());
//            } else {
//                // Создание новой записи
//                token = new OAuth2Token();
//                token.setClientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId());
//                token.setPrincipalName(principal.getName());
//                token.setEncryptedAccessToken(encryptedAccessToken);
//                token.setEncryptedRefreshToken(encryptedRefreshToken);
//                token.setAccessTokenExpiresAt(authorizedClient.getAccessToken().getExpiresAt());
//            }
//
//            tokenRepository.save(token);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to encrypt token", e);
//        }
//    }
//
//    @Override
//    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
//        tokenRepository.deleteByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);
//    }
//}

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

