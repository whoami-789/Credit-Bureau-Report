package org.creditbureaureport.repository;

import org.creditbureaureport.model.Zalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZalogRepository extends JpaRepository<Zalog, Byte> {

    @Query(value = "select ls, sums, kod_cb from zalog where numdog like ?", nativeQuery = true)
    List<Zalog> findFirstByNumdog(String numdog);
}
