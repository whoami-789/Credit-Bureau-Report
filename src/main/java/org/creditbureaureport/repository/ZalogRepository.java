package org.creditbureaureport.repository;

import org.creditbureaureport.model.Zalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZalogRepository extends JpaRepository<Zalog, Byte> {

    List<Zalog> findFirstByNumdog(String numdog);
}
