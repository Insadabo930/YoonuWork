package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur ,Long> {
    boolean existsByUsername(String username);
    boolean existsByTelephone(String telephone);
    Optional<Utilisateur> findByUsername(String username);
}
