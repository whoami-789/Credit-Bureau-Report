package org.creditbureaureport.repository;

import org.creditbureaureport.model.Inform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformRepository extends JpaRepository<Inform, String> {
    // Дополнительные методы, если необходимо
}
