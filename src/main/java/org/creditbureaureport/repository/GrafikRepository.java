package org.creditbureaureport.repository;

import org.creditbureaureport.model.Grafik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrafikRepository extends JpaRepository<Grafik, Integer> {
    // Дополнительные методы, если необходимо
}
