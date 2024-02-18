package sori.jakku.kkunkkyu.memore.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import sori.jakku.kkunkkyu.memore.common.config.security.CustomUserDetailsService;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

}
