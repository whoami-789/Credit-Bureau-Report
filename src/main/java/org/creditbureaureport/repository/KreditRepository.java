package org.creditbureaureport.repository;

import org.creditbureaureport.model.Kredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KreditRepository extends JpaRepository<Kredit, String> {
    // Дополнительные методы, если необходимо
}
