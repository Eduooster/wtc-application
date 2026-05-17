package org.wtc.application.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.repository.AuthenticableUserRepository;
import org.wtc.application.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticableUserRepository authenticableUserRepository;



    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails)this.authenticableUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
