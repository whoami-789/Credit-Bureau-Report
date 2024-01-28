package org.creditbureaureport.repository;

import org.creditbureaureport.model.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    @Query("select distinct s from Saldo s WHERE (s.ls = :lskred OR s.ls = :lsproc OR s.ls = :lsprosrKred OR s.ls = :lsprosrProc OR s.ls = :lspeni)")
    List<Saldo> findByDokumentLscor(@Param("lskred") String lskred,
                                    @Param("lsproc") String lsproc,
                                    @Param("lsprosrKred") String lsprosrKred,
                                    @Param("lsprosrProc") String lsprosrProc,
                                    @Param("lspeni") String lspeni);
}
