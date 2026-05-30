package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
