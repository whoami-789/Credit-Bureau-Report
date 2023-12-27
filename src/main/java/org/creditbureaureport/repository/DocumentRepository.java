package org.creditbureaureport.repository;

import org.creditbureaureport.model.Dokument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Dokument, Long> {

    @Query(value = "SELECT " +
            "d.numdok, d.dats, d.tipdoc, d.ls, d.lscor, d.nazn, d.sost, d.users, d.dtime, d.sums, d.dat_prov, d.kod, d.kod_dog, d.datsproc, d.koduch, d.prixgroup, d.groupstr, " +
            "k.numdog, k.datadog, k.dats, k.summa, k.vidvalut, k.vidzalog, k.vidsrok, k.prosent, k.maqsad, k.sost, k.status, k.prim, k.yurfiz, k.tipkred, k.srokkred, k.users, k.lskred, k.lsproc, " +
            "s.ls, s.dats, s.sums, s.activate, s.id, " +
            "g.dats, g.pog_kred, g.pog_proc, g.ostatok, g.mes, g.sost, g.numdog, g.id, " +
            "z.kod, z.kod_zalog, z.sums, z.prim, z.dtime, z.numdog, z.sost, z.dats, z.dats_snyat, z.ls, z.kod_cb, z.dopinfo, z.numdog_kredit, " +
            "zd.kod, zd.kod_zalog, zd.stroka, zd.detal, zd.sums, zd.zalog_kod, " +
            "zx.num_dog, zx.num_korobki, zx.status, zx.numdog_zalog, " +
            "af.kodchlen, af.dats, af.name, af.numknigki, af.inn, af.adres, af.telmobil, af.telhome, af.vznos, af.sost, af.ser_num_pasp, af.vidan_pasp, af.kem_pasp, af.obrazovan, af.dats_rojd, af.dats_zakr, " +
            "af.svyaz, af.fsobst, af.users, af.prim, af.mwork, af.ad_email, af.status, af.filial, af.lsvznos, af.dtime, af.indpred, af.photo, af.kod_subject, af.ser_pasp, af.num_pasp, af.kod_obl, af.kod_rayon, " +
            "af.fam, af.imya, af.otch, af.tip_zayom, af.kod_pension, af.tip_dok, af.sost_registr, af.sys_number, af.dats_izm, af.uchred, af.dats_izm_uchred, af.paspdo, af.id " +
            "FROM " +
            "dok d " +
            "LEFT JOIN kredit k ON d.kod_dog = k.kod_dog " +
            "LEFT JOIN saldo s ON d.ls = s.ls " +
            "LEFT JOIN grafik g ON d.numdok = g.numdog " +
            "LEFT JOIN zalog z ON d.numdok = z.numdog " +
            "LEFT JOIN zalog_detal zd ON z.kod = zd.kod_zalog " +
            "LEFT JOIN zalog_xranenie zx ON z.numdog = zx.numdog_zalog " +
            "LEFT JOIN azolik_fiz af ON d.koduch = af.kodchlen", nativeQuery = true)
    List<Dokument> getCustomResult();


}
