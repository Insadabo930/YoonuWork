package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourRepository extends JpaRepository<Cour,Long> {

}
