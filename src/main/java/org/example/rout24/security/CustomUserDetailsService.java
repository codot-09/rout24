package org.example.rout24.security;

import lombok.RequiredArgsConstructor;
import org.example.rout24.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhone(username)
                .or(() -> userRepository.findByChatId(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
