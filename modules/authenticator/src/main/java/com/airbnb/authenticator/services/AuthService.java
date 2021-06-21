package com.airbnb.authenticator.services;

import com.airbnb.authenticator.domains.privilege.models.entities.Privilege;
import com.airbnb.authenticator.domains.users.models.entities.User;
import com.airbnb.authenticator.domains.users.repositories.UserRepository;
import com.airbnb.common.exceptions.notfound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mir00r on 21/6/21
 * @project IntelliJ IDEA
 */
@Service
public class AuthService {

    private final UserRepository userRepo;
    private final EntityManager entityManager;

    @Autowired
    public AuthService(UserRepository userRepo, EntityManager entityManager) {
        this.userRepo = userRepo;
        this.entityManager = entityManager;
    }

    public boolean existsByUsername(String username) throws UserNotFoundException {
        return this.findAuthUser(username) != null;
    }

    public User findAuthUser(String username) throws UserNotFoundException {
        return this.userRepo.find(username).orElseThrow(() -> new UserNotFoundException("Could not find user with username: " + username));
    }

    public List<Privilege> getAuthorities() {
        String sql = "SELECT a FROM Privilege a  WHERE a.deleted=false";
        Query query = this.entityManager.createQuery(sql);

        List<Privilege> authorities;
        try {
            authorities = query.getResultList();
        } catch (Exception e) {
            authorities = new ArrayList<>();
        }
        return authorities;
    }
}
