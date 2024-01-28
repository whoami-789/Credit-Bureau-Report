package org.creditbureaureport.repository;

import org.creditbureaureport.model.Dokument;
import org.creditbureaureport.model.Kredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.List;

public interface DokRepository extends JpaRepository<Dokument, Long> {
    @Query("SELECT d FROM Dokument d WHERE d.dats BETWEEN :startDate AND :endDate " +
            "AND (d.ls LIKE '10101%' OR d.ls LIKE '10503%')")
    List<Dokument> findDokumentsWithinDateRangeAndLsPattern(LocalDate startDate, LocalDate endDate);

//    List<Dokument> findByKredit(Kredit kredit);

    List<Dokument> findByLscorAndDatsBetween(String lscor, LocalDate startDate, LocalDate endDate);

//    @Query(value = "Select d from Dokument d inner join Kredit k on d.ls = k.lskred " +
//            "or d.ls = k.lsproc or d.ls = k.lsprosrProc or d.ls = k.lsprosrKred or d.ls = k.lspeni " +
//            "where d.lscor like '10101%' or d.lscor like '10503%'")
//    List<Dokument> findByKredit();

    @Query(value = "Select d from Dokument d inner join Kredit k on d.ls = k.lskred " +
            "or d.ls = k.lsproc or d.ls = k.lsprosrProc or d.ls = k.lsprosrKred or d.ls = k.lspeni")
    List<Dokument> findByKredit();

    @Query(value = "Select d" +
            " from Dokument d left join Kredit k on d.ls = k.lsproc or d.ls = k.lskred or d.ls = k.lsprosrKred or d.ls = k.lsprosrProc" +
            " or d.ls = k.lspeni " +
            "where d.ls like '10101%' or d.lscor like '12401%' or " +
            "d.lscor like '10503%' and  d.lscor = k.lskred and " +
            "d.dats between :start_date and :end_date")
    List<Dokument> findByKredit(LocalDate start_date, LocalDate end_date);


    @Query("SELECT d FROM Dokument d WHERE d.lscor LIKE '12401%' AND d.lscor = :lskred")
    List<Dokument> findByLscorAndKreditLskred(@Param("lskred") String lskred);

    @Query("SELECT d FROM Dokument d " +
            "WHERE (d.ls = :lskred OR d.ls = :lsproc OR d.ls = :lsprosrKred OR d.ls = :lsprosrProc OR d.ls = :lspeni) " +
            "AND (d.lscor LIKE '10101%' OR d.lscor LIKE '10503%' or d.lscor like '10509%') or" +
            " ((d.ls = :lsproc AND d.lscor = :lsprosrProc) OR (d.ls = :lskred AND d.lscor = :lsprosrKred))")
    List<Dokument> findByKreditComplexConditions(@Param("lskred") String lskred,
                                                 @Param("lsproc") String lsproc,
                                                 @Param("lsprosrKred") String lsprosrKred,
                                                 @Param("lsprosrProc") String lsprosrProc,
                                                 @Param("lspeni") String lspeni);


    List<Dokument> findByDatsBetween(LocalDate start, LocalDate end);


//    Select d from Dokument d inner join Kredit k on " +
//            "(d.ls = k.lsproc and d.lscor like '10101%' or d.lscor like '10503%' or d.lscor = k.lsprosrProc) " +
//            "or d.lscor = k.lskred or (d.ls = k.lskred or d.ls = k.lsprosrKred and d.lscor like '10101%' or d.lscor like '10503%') " +
//            "or (d.ls = k.lsprosrProc and d.lscor like '10101%' or d.lscor like '10503%') " +
//            "or (d.ls = k.lspeni and d.lscor like '10101%' or d.lscor like '10503%')
}
