package org.creditbureaureport.repository;

import org.creditbureaureport.model.Dokument;
import org.creditbureaureport.model.Kredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DokRepository extends JpaRepository<Dokument, Long> {
    @Query("SELECT d FROM Dokument d WHERE d.dats BETWEEN :startDate AND :endDate " +
            "AND (d.ls LIKE '10101%' OR d.ls LIKE '10503%')")
    List<Dokument> findDokumentsWithinDateRangeAndLsPattern(LocalDate startDate, LocalDate endDate);

    List<Dokument> findByKredit(Kredit kredit);

    List<Dokument> findByLscorAndDatsBetween(String lscor, LocalDate startDate, LocalDate endDate);

    @Query(value = "Select d from Dokument d inner join Kredit k on d.lscor = k.lsproc or d.lscor = k.lskred where k.lsproc like :lsproc and k.lskred like :lskred")
    List<Dokument> findByKreditLsproc(String lsproc, String lskred);
}
