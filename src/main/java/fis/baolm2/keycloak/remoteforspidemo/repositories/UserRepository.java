package fis.baolm2.keycloak.remoteforspidemo.repositories;

import fis.baolm2.keycloak.remoteforspidemo.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByUserNameContainsIgnoreCase(String userName);

    List<User> findByRolesContainsIgnoreCase(String roles);

    // This method is defined by super class.
    // Optional<User> findById(String id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    boolean existsByUserNameAndPassword(String userName, String password);

    // This method is defined by super class.
    // long count();
}
