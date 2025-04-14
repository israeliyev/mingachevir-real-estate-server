package io.mingachevir.mingachevirrealestateserver.service.impl;

import io.mingachevir.mingachevirrealestateserver.model.entity.User;
import io.mingachevir.mingachevirrealestateserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.warn("User not found: {}", username);
                return new UsernameNotFoundException("User not found: " + username);
            });
        logger.info("User found: {} with roles: {}", username, user.getAuthorities());
        return user;
    }
}
