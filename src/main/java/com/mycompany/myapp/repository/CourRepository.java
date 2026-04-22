package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourRepository extends JpaRepository<Cour,Long> {
    List<Cour> findByFormateurId(Long formateurId);

}
