package org.creditbureaureport.repository;

import org.creditbureaureport.model.AzolikFiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public interface AzolikFizRepository extends JpaRepository<AzolikFiz, String> {
    @Query(value = "SELECT distinct af.kodchlen," +
            "                af.name," +
            "                k.numdog," +
            "                k.vidkred," +
            "                MAX(k.dats_izm)                                              AS dats_izm," +
            "                MAX(k.sost)                                                  AS sost," +
            "                MAX(k.status)                                                AS status," +
            "                MAX(k.datadog)                                               AS datadog," +
            "                k.dats_zakr                                                  AS dats_zakr," +
            "                MAX(k.summa)                                                 AS summa," +
            "                MAX(k.graf)                                                  AS graf," +
            "                MAX(k.nalbeznal)                                             AS nalbeznal," +
            "                MAX(k.xatar)                                                 AS xatar," +
            "                MAX(k.tipkred)                                               AS tipkred," +
            "                MAX(k.prosent)                                               AS prosent," +
            "                MAX(d.dats)                                                  AS dats_d," +
            "                MAX(g.dats)                                                  AS dats_g," +
            "                cast(max(k.summa / k.srokkred) as INT)                       AS pod," +
            "                (SELECT MIN(g_inner.dats)" +
            "                 FROM grafik g_inner" +
            "                 WHERE g_inner.numdog = k.numdog)                            AS first_dats_g_before_condition," +
            "                COUNT(CASE WHEN g.dats > k.dats_izm THEN 1 END)              AS sum_prosr," +
            "                MIN(CASE WHEN g.dats > k.dats_izm THEN g.dats ELSE NULL END) AS first_dats_g_after_condition," +
            "                MAX(z.sums)                                                  AS sums_z," +
            "                MAX(z.kod_cb)                                                AS kod_cb," +
            "                (SELECT SUM(s_inner.sums)" +
            "                 FROM saldo s_inner" +
            "                 WHERE s_inner.ls IN (k.lspeni, k.lsproc, k.lskred)" +
            "                   AND s_inner.activate = 1)                                 AS total_sums," +
            "                (SELECT SUM(s_inner.sums)" +
            "                 FROM saldo s_inner" +
            "                 WHERE s_inner.ls = k.lsprosr_proc" +
            "                   AND s_inner.activate = 1)                                 AS total_sums_prosr_proc," +
            "                (SELECT SUM(s_inner.sums)" +
            "                 FROM saldo s_inner" +
            "                 WHERE s_inner.ls = k.lsprosr_kred" +
            "                   AND s_inner.activate = 1)                                 AS total_sums_prosr_kred," +
            "                (SELECT count(g_inner.pog_kred)" +
            "                 FROM grafik g_inner" +
            "                 WHERE g_inner.numdog = k.numdog" +
            "                   and g_inner.pog_kred > 0)                                 AS sum_vznos," +
            "                (SELECT count(g_inner.pog_kred)" +
            "                 FROM grafik g_inner" +
            "                 WHERE g_inner.numdog = k.numdog)                            AS sum_vznos_all," +
            "                MIN(CASE" +
            "                        WHEN g.dats > k.dats_izm and k.dats_zakr is null THEN g.pog_proc + g.pog_kred" +
            "                        ELSE NULL END)                                       AS next_summ," +
            "                (SELECT count(g_inner.pog_kred)" +
            "                 FROM grafik g_inner" +
            "                 WHERE g_inner.numdog = k.numdog" +
            "                   and k.dats_zakr is null)                                  AS counted_payments," +
            "                (SELECT count(d_inner.lscor)" +
            "                 FROM dok d_inner" +
            "                 WHERE d_inner.lscor = k.lsprosr_proc" +
            "                   and d_inner.tipdoc = 6)                                   AS count_sums_prosr_proc," +
            "                (SELECT count(d_inner.lscor)" +
            "                 FROM dok d_inner" +
            "                 WHERE d_inner.lscor = k.lsprosr_kred" +
            "                   and d_inner.tipdoc = 6)                                   AS count_sums_prosr_kred," +
            "                max(z.ls)                                                    as z_ls," +
            "                max(zx.data_priem)                                           as zx_priem," +
            "                max(zx.data_vozvrat)                                         as zx_vozvrat," +
            "                (SELECT OverduePeriod" +
            "                 FROM dbo.SpisokProsrochennixKreditov(k.numdog, :EndDate)) as overdue," +
            "                   MAX(k.klass) as class" +
            " FROM kredit k" +
            "         INNER JOIN" +
            "     azolik_fiz af ON af.kodchlen = k.kod" +
            "         LEFT JOIN" +
            "     (SELECT kod_dog, ls, lscor, MAX(dats) AS dats" +
            "      FROM dok" +
            "      GROUP BY kod_dog, ls, lscor) d ON k.kod_dog = d.kod_dog" +
            "         LEFT JOIN" +
            "     (SELECT numdog, pog_kred, pog_proc, MAX(dats) AS dats" +
            "      FROM grafik" +
            "      GROUP BY numdog, pog_kred, pog_proc) g ON k.numdog = g.numdog" +
            "         LEFT JOIN" +
            "     (SELECT ls, activate, MAX(sums) AS sums" +
            "      FROM saldo" +
            "      GROUP BY ls, activate) s ON d.ls = s.ls" +
            "         LEFT JOIN" +
            "     (SELECT numdog, ls, MAX(sums) AS sums, MAX(kod_cb) AS kod_cb" +
            "      FROM zalog" +
            "      GROUP BY numdog, ls) z ON g.numdog = z.numdog" +
            "         Left JOIN" +
            "     (select data_priem, data_vozvrat, kod_dog" +
            "      from zalog_xranenie) zx on k.kod_dog = zx.kod_dog" +
            " WHERE (k.dats_izm between :StartDate and :EndDate" +
            "    OR d.dats between :StartDate and :EndDate)" +
            "  AND EXISTS (SELECT 1" +
            "              FROM saldo s" +
            "              WHERE (s.ls = k.lspeni OR s.ls = k.lsproc OR s.ls = k.lskred" +
            "                  OR s.ls = k.lsprosr_proc OR s.ls = k.lsprosr_kred)" +
            "                AND s.activate = 1)" +
            "  AND k.numdog = g.numdog" +
            " GROUP BY af.kodchlen, af.name, k.numdog, k.vidkred, k.lspeni, k.lsproc, k.lskred, k.lsprosr_proc, k.lsprosr_kred," +
            "         k.dats_zakr, s.activate, k.kod;", nativeQuery = true)
    List<Object[]> findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie(@Param("StartDate") LocalDate StartDate, @Param("EndDate") LocalDate EndDate);

    @Query("FROM AzolikFiz WHERE (datsIzm) between :StartDate and :EndDate")
    List<AzolikFiz> findByMonthAndYear(@Param("StartDate") LocalDate StartDate, @Param("EndDate") LocalDate EndDate);

    @Query("SELECT s.nameu FROM SprRayon s WHERE s.kod = :kod")
    Optional<String> findNameuByKod(String kod);
}
