package org.wtc.application.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.message.enums.ParticipantType;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;



@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(UserDetails usuario) {

        try {

            AuthenticableUser auth = (AuthenticableUser) usuario;

            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API wtc")
                    .withSubject(auth.getUsername())



                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);

        } catch (Exception exception) {
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public DecodedJWT validateToken(String tokenJWT) {

        try {

            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("API wtc")
                    .build()
                    .verify(tokenJWT);

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    public String getSubject(String tokenJWT) {
        return validateToken(tokenJWT).getSubject();
    }

    public Long getParticipantId(String tokenJWT) {
        return validateToken(tokenJWT)
                .getClaim("participantId")
                .asLong();
    }

    public ParticipantType getParticipantType(String tokenJWT) {

        String type = validateToken(tokenJWT)
                .getClaim("participantType")
                .asString();

        return ParticipantType.valueOf(type);
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}