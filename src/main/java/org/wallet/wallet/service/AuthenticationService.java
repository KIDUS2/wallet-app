package org.wallet.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.wallet.dto.requestDto.LoginDTO;
import org.wallet.wallet.dto.responsesDto.LoginResposeDto;
import org.wallet.wallet.entity.User;
import org.wallet.wallet.repository.UserRepository;
import org.wallet.wallet.security.config.JwtService;
import org.wallet.wallet.security.token.Token;
import org.wallet.wallet.security.token.TokenRepository;
import org.wallet.wallet.security.token.TokenType;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public LoginResposeDto authenticate(LoginDTO request) {
        String principal = request.getPrincipal();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        principal,
                        request.getPassword()
                )
        );
        var user = repository.findByEmailOrPhoneNumber(principal)
                .orElseThrow();

        if (user.getPassword() == null) {
            return new LoginResposeDto("Invalid  password", HttpStatus.BAD_REQUEST.value(), null, null);
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return LoginResposeDto.builder()
                .message("successfully login")
                .code(HttpStatus.OK.value())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {

            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
