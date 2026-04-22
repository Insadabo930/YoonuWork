package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.domain.enumeration.TypePublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,Long> {
    Optional<Publication> findByType(TypePublication type);
}
