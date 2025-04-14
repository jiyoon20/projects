package me.jooie.minicafe.service;

import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.UserRole;
import me.jooie.minicafe.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException{
        Optional<CafeUser> _cafeUser = this.userRepository.findByUsername(username);

        if(_cafeUser.isEmpty())
            throw new UsernameNotFoundException("Not Found Username");

        CafeUser cafeUser = _cafeUser.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority((cafeUser.getRole().getValue())));

        return new User(cafeUser.getUsername(), cafeUser.getPassword(), authorities);
    }
}
