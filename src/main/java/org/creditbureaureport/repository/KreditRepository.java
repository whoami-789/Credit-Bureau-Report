package org.creditbureaureport.repository;

import org.creditbureaureport.model.Kredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface KreditRepository extends JpaRepository<Kredit, String> {
    Optional<Kredit> findByNumdog(String kod);

    List<Kredit> findAllByDokuments_LscorIn(Set<String> lscorList);

    List<Kredit> findAllByLskredIn(Set<String> lsprocList);

    List<Kredit> findByDatsIzmBetween(LocalDate start, LocalDate end);


}
