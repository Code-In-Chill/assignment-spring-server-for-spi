package fis.baolm2.keycloak.remoteforspidemo.services;

import fis.baolm2.keycloak.remoteforspidemo.dtos.CredentialInput;
import fis.baolm2.keycloak.remoteforspidemo.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    List<User> findByUserNameContainsIgnoreCase(String userName);

    List<User> findByRolesContainsIgnoreCase(String roles);

    Optional<User> findById(String id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);


    long count();

    boolean verifyUser(CredentialInput cred);
}
