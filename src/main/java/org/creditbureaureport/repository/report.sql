SELECT distinct af.kodchlen,
                af.name,
                k.kod,
                k.numdog,
                k.vidkred,
                MAX(k.dats_izm)                                              AS dats_izm,
                MAX(k.sost)                                                  AS sost,
                MAX(k.status)                                                AS status,
                MAX(k.datadog)                                               AS datadog,
                k.dats_zakr                                                  AS dats_zakr,
                MAX(k.summa)                                                 AS summa,
                MAX(k.graf)                                                  AS graf,
                MAX(k.nalbeznal)                                             AS nalbeznal,
                MAX(k.xatar)                                                 AS xatar,
                MAX(k.tipkred)                                               AS tipkred,
                MAX(k.prosent)                                               AS prosent,
                MAX(k.kod_dog)                                               AS kod_dog,
                MAX(k.dats)                                                  AS dats,
                MAX(d.dats)                                                  AS dats_d,
                MAX(g.dats)                                                  AS dats_g,
                min(g.pog_kred + g.pog_proc)                                 AS pod,
                (SELECT MIN(g_inner.dats)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog)                            AS first_dats_g_before_condition,
                COUNT(CASE WHEN g.dats > k.dats_izm THEN 1 END) AS sum_prosr,
                MIN(CASE WHEN g.dats > k.dats_izm THEN g.dats ELSE NULL END) AS first_dats_g_after_condition,
                MAX(z.sums)                                                  AS sums_z,
                MAX(z.kod_cb)                                                AS kod_cb,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls IN (k.lspeni, k.lsproc, k.lskred)
                   AND s_inner.activate = 1)                                 AS total_sums,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls = k.lsprosr_proc
                   AND s_inner.activate = 1)                                 AS total_sums_prosr_proc,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls = k.lsprosr_kred
                   AND s_inner.activate = 1)                                 AS total_sums_prosr_kred
FROM kredit k
         INNER JOIN
     azolik_fiz af ON af.kodchlen = k.kod
         LEFT JOIN
     (SELECT kod_dog, ls, MAX(dats) AS dats
      FROM dok
      GROUP BY kod_dog, ls) d ON k.kod_dog = d.kod_dog
         LEFT JOIN
     (SELECT numdog, pog_kred, pog_proc, MAX(dats) AS dats
      FROM grafik
      GROUP BY numdog, pog_kred, pog_proc) g ON k.numdog = g.numdog
         LEFT JOIN
     (SELECT ls, activate, MAX(sums) AS sums
      FROM saldo
      GROUP BY ls, activate) s ON d.ls = s.ls
         LEFT JOIN
     (SELECT numdog, MAX(sums) AS sums, MAX(kod_cb) AS kod_cb
      FROM zalog
      GROUP BY numdog) z ON g.numdog = z.numdog
WHERE MONTH(k.dats_izm) = 11
  AND YEAR(k.dats_izm) = 2023
  AND EXISTS (SELECT 1
              FROM saldo s
              WHERE (s.ls = k.lspeni OR s.ls = k.lsproc OR s.ls = k.lskred
                  OR s.ls = k.lsprosr_proc OR s.ls = k.lsprosr_kred)
                AND s.activate = 1)
  and k.numdog = g.numdog
GROUP BY af.kodchlen, af.name, k.numdog, k.vidkred, k.lspeni, k.lsproc, k.lskred, k.lsprosr_proc, k.lsprosr_kred,
         k.dats_zakr, s.activate, k.kod;

SELECT O.kod, SUM(S.sums * pm) / 1000 as sums
FROM otch_spr AS O
         INNER JOIN ls AS LS ON LEFT(LS.ls, 5) = O.bal
         LEFT OUTER JOIN saldo AS S ON S.ls = LS.ls AND
                                       S.dats = (SELECT MAX(Dats) FROM saldo WHERE Ls = LS.ls AND Dats <= '01.12.2023')
GROUP BY O.kod

SELECT otch_spr_282.kod,
       Replace(CONVERT(nvarchar, CONVERT(Decimal(16, 3), ISNULL(SUM(saldo.sums * pm) / 1000, 0))), ',', ',') as sums
FROM otch_spr_282
         INNER JOIN ls ON LEFT(ls.ls, 5) = otch_spr_282.bal
         LEFT OUTER JOIN saldo ON saldo.ls = ls.ls AND
                                  saldo.dats = (SELECT MAX(dats) FROM saldo WHERE Ls = ls.ls AND dats <= '01.12.2023')
GROUP BY otch_spr_282.kod


