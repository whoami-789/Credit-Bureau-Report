package org.creditbureaureport.repository;

import org.creditbureaureport.model.Kredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface KreditRepository extends JpaRepository<Kredit, String> {
    Optional<Kredit> findByNumdog(String kod);

//    List<Kredit> findAllByDokuments_LscorIn(Set<String> lscorList);

    List<Kredit> findAllByLskredIn(Set<String> lsprocList);

    List<Kredit> findByDatsIzmBetweenOrStatus(LocalDate start, LocalDate end, byte status);
    List<Kredit> findByDatadogBetween(LocalDate start, LocalDate end);

    @Query(value = "select * from SpisokProsrochennixKreditov(:numdog, :endate)", nativeQuery = true)
    List<Object> SpisProsrKred(String numdog, LocalDate endate);
}
