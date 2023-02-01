package com.rectle.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.TokenVerifier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

import static com.google.auth.http.AuthHttpConstants.BEARER;

@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private void verifyToken(String token) throws TokenVerifier.VerificationException {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();
        tokenVerifier.verify(token.replace(BEARER, ""));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            throw new BadCredentialsException("Authorization token is not present");
        }
        try {
            verifyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUserAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (TokenVerifier.VerificationException e) {
            log.warn("There was a problem with token verification", e);
            throw new RuntimeException(e);
        }
    }

    private UsernamePasswordAuthenticationToken getUserAuthentication(HttpServletRequest request) throws JsonProcessingException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            throw new BadCredentialsException("Authorization token is not present");
        }

        DecodedJWT jwt = JWT.decode(token.replaceFirst(BEARER, ""));
        String payloadRaw = new String(Base64.getUrlDecoder().decode(jwt.getPayload()));
        if (payloadRaw.isBlank()) {
            throw new IllegalStateException("Wrong JWT token format");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode payload = objectMapper.readTree(payloadRaw);

        return new UsernamePasswordAuthenticationToken(payload.path("email").textValue(), null, null);
    }
}
