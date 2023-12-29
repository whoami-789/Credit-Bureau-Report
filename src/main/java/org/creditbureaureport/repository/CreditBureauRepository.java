package org.creditbureaureport.repository;

import org.creditbureaureport.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditBureauRepository {
    JpaRepository<AzolikFiz, String> getAzolikFizRepository();
    JpaRepository<AzolikYur, String> getAzolikYurRepository();
    JpaRepository<Dokument, Long> getDokumentRepository();
    JpaRepository<Grafik, Integer> getGrafikRepository();
    JpaRepository<Inform, String> getInformRepository();
    JpaRepository<Kredit, String> getKreditRepository();
    JpaRepository<Saldo, Long> getSaldoRepository();
    JpaRepository<Zalog, Byte> getZalogRepository();
}
