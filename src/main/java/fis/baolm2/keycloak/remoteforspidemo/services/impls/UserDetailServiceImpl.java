package fis.baolm2.keycloak.remoteforspidemo.services.impls;

import fis.baolm2.keycloak.remoteforspidemo.entities.User;
import fis.baolm2.keycloak.remoteforspidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = ur.findByUserName(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException(username + " not found");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getUserName())
                .password(user.get().getPassword())
                .disabled(!user.get().isEnabled())
                .roles(user.get().getRoles().split(","))
                .build();
    }
}
