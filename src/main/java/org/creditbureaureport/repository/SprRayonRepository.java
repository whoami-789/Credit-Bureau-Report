package org.creditbureaureport.repository;


import org.creditbureaureport.model.SprRayon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SprRayonRepository extends JpaRepository<SprRayon, String> {
    @Query("SELECT s.nameu FROM SprRayon s WHERE s.kod = :kod")
    Optional<String> findNameuByKod(String kod);
}
