package fis.baolm2.keycloak.remoteforspidemo.services.impls;

import fis.baolm2.keycloak.remoteforspidemo.dtos.CredentialInput;
import fis.baolm2.keycloak.remoteforspidemo.entities.User;
import fis.baolm2.keycloak.remoteforspidemo.repositories.UserRepository;
import fis.baolm2.keycloak.remoteforspidemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository ur;
    private final PasswordEncoder pe;

    @Autowired
    public UserServiceImpl(UserRepository ur, PasswordEncoder pe) {
        this.ur = ur;
        this.pe = pe;
    }

    @Override
    public User saveUser(User user) {
        return ur.save(user);
    }

    @Override
    public List<User> findByUserNameContainsIgnoreCase(String userName) {
        return ur.findByUserNameContainsIgnoreCase(userName);
    }

    @Override
    public List<User> findByRolesContainsIgnoreCase(String roles) {
        return ur.findByRolesContainsIgnoreCase(roles);
    }

    @Override
    public Optional<User> findById(String id) {
        return ur.findById(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return ur.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return ur.findByEmail(email);
    }

    @Override
    public long count() {
        return ur.count();
    }

    @Override
    public boolean verifyUser(CredentialInput cred) {

        Optional<User> user = ur.findByUserName(cred.username());

        if (user.isEmpty()) return false;

        return pe.matches(cred.password(), user.get().getPassword());
    }
}
