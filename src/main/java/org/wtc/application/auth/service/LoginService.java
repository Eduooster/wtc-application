package org.wtc.application.auth.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.dto.LoginRequestDto;
import org.wtc.application.auth.dto.TokenResponseDto;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.auth.repository.AuthenticableUserRepository;
import org.wtc.application.config.security.TokenService;



@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final  TokenService tokenService;


    public TokenResponseDto authenticate(@Valid LoginRequestDto dto) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            UserDetails userDetails = (UserDetails)authenticationManager.authenticate(authenticationToken).getPrincipal();
            String tokenJwt = tokenService.gerarToken(userDetails);
            return new TokenResponseDto(tokenJwt, userDetails.getUsername());

        }catch (AuthenticationException ex){
            throw new EntityNotFoundException("Login failed");
        }

    }
}
