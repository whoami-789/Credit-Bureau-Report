package org.creditbureaureport.repository;

import org.creditbureaureport.model.Zalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZalogRepository extends JpaRepository<Zalog, Byte> {
    // Дополнительные методы, если необходимо
}
