package org.creditbureaureport.repository;

import org.creditbureaureport.model.Dokument;
import org.creditbureaureport.model.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    @Query("select s from Saldo s inner join Dokument d on s.ls = d.lscor where d.lscor like :ls")
    List<Saldo> findByDokumentLscor(String ls);
}
