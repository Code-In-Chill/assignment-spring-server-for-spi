package fis.baolm2.keycloak.remoteforspidemo.controllers;

import fis.baolm2.keycloak.remoteforspidemo.dtos.CredentialInput;
import fis.baolm2.keycloak.remoteforspidemo.dtos.CredentialResponse;
import fis.baolm2.keycloak.remoteforspidemo.entities.User;
import fis.baolm2.keycloak.remoteforspidemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("api/keycloak-remote-provider-demo")
@CrossOrigin(origins = {"http://localhost:8080"})
public class UserController {

    private final UserService us;
    private final PasswordEncoder pe;

    @Autowired
    public UserController(UserService us, PasswordEncoder pe) {
        this.us = us;
        this.pe = pe;
    }

    @PutMapping("/public/create")
    public ResponseEntity<?> createUserEndpoint(@RequestBody User user) {
        user.setPassword(pe.encode(user.getPassword()));
        User newUser = us.saveUser(user);
        return okResponse(newUser);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserEndpoint(
            @RequestParam String type,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        switch (type) {
            case "id": {
                if (id == null)
                    return badResponse("Missing parameter \"id=<?>\"");
                Optional<User> user = us.findById(id);

                if (user.isEmpty())
                    return badResponse("user with id " + id + " not found");

                return okResponse(user.get());
            }
            case "username": {
                if (username == null)
                    return badResponse("Missing parameter \"username=<?>\"");
                Optional<User> user = us.findByUserName(username);

                if (user.isEmpty())
                    return badResponse("user with username " + username + " not found");

                return okResponse(user.get());
            }
            case "email": {
                if (email == null)
                    return badResponse("Missing parameter \"email=<?>\"");
                Optional<User> user = us.findByEmail(email);

                if (user.isEmpty())
                    return badResponse("user with email " + email + " not found");

                return okResponse(user.get());
            }
            default: {
                return badResponse("Invalid type. One of this types are available: [id, username, email]");
            }
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUserEndpoint(@RequestBody CredentialInput cred) {
        boolean verifyResult = us.verifyUser(cred);
        return okResponse(new CredentialResponse(verifyResult));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserEndpoint(
            @RequestParam String method,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "11") int take,
            @RequestParam(name = "keycloak.session.realm.users.query.include_service_account") boolean includeServiceAccount,
            @RequestParam(name = "keycloak.session.realm.users.query.search", defaultValue = "") String keyword
    ) {
        if (keyword.equals("*"))
            keyword = "";
        switch (method) {
            case "user": {
                List<User> users = us.findByUserNameContainsIgnoreCase(keyword);
                if (users.isEmpty()) {
                    return okResponse(Collections.emptyList());
                }

                // Điều chỉnh giá trị skip và take
                skip = Math.max(skip, 0);
                take = Math.min(take, users.size());

                if (skip >= users.size()) {
                    return okResponse(Collections.emptyList());
                }

                return okResponse(users.subList(skip, take));
            }
            case "role": {
                if (role == null) {
                    return badResponse("Missing parameter \"role=<?>\"");
                }

                List<User> users = us.findByRolesContainsIgnoreCase(keyword);
                if (users.isEmpty()) {
                    return okResponse(Collections.emptyList());
                }

                // Điều chỉnh giá trị skip và take
                skip = Math.max(skip, 0);
                take = Math.min(take, users.size());

                if (skip >= users.size()) {
                    return okResponse(Collections.emptyList());
                }

                return okResponse(users.subList(skip, take));
            }
            default:
                return badResponse("Invalid method. One of this methods are available [user, role]");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countUserEndpoint(@RequestParam(required = false) Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("key: " + entry.getKey() + " | value: " + entry.getValue());
        }
        return okResponse(Map.of("total", us.count()));
    }

    private ResponseEntity<?> okResponse(Object body) {
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<?> badResponse(String message) {
        return ResponseEntity.badRequest().body(message);
    }
}
