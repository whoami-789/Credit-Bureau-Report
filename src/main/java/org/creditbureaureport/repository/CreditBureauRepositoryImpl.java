package org.creditbureaureport.repository;

import org.creditbureaureport.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CreditBureauRepositoryImpl implements CreditBureauRepository {

    private final AzolikFizRepository azolikFizRepository;
    private final AzolikYurRepository azolikYurRepository;
    private final DocumentRepository dokumentRepository;
    private final GrafikRepository grafikRepository;
    private final InformRepository informRepository;
    private final KreditRepository kreditRepository;
    private final SaldoRepository saldoRepository;
    private final ZalogRepository zalogRepository;

    @Autowired
    public CreditBureauRepositoryImpl(
            AzolikFizRepository azolikFizRepository,
            AzolikYurRepository azolikYurRepository,
            DocumentRepository documentRepository,
            GrafikRepository grafikRepository,
            InformRepository informRepository,
            KreditRepository kreditRepository,
            SaldoRepository saldoRepository,
            ZalogRepository zalogRepository) {
        this.azolikFizRepository = azolikFizRepository;
        this.azolikYurRepository = azolikYurRepository;
        this.dokumentRepository = documentRepository;
        this.grafikRepository = grafikRepository;
        this.informRepository = informRepository;
        this.kreditRepository = kreditRepository;
        this.saldoRepository = saldoRepository;
        this.zalogRepository = zalogRepository;
    }

    @Override
    public JpaRepository<AzolikFiz, String> getAzolikFizRepository() {
        return azolikFizRepository;
    }

    @Override
    public JpaRepository<AzolikYur, String> getAzolikYurRepository() {
        return azolikYurRepository;
    }

    @Override
    public JpaRepository<Dokument, Long> getDokumentRepository() {
        return dokumentRepository;
    }

    @Override
    public JpaRepository<Grafik, Integer> getGrafikRepository() {
        return grafikRepository;
    }

    @Override
    public JpaRepository<Inform, String> getInformRepository() {
        return informRepository;
    }

    @Override
    public JpaRepository<Kredit, String> getKreditRepository() {
        return kreditRepository;
    }

    @Override
    public JpaRepository<Saldo, Long> getSaldoRepository() {
        return saldoRepository;
    }

    @Override
    public JpaRepository<Zalog, Byte> getZalogRepository() {
        return zalogRepository;
    }
}
