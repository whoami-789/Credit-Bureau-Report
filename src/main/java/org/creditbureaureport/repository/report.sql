SELECT distinct af.kodchlen,
                af.name,
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
                MAX(d.dats)                                                  AS dats_d,
                MAX(g.dats)                                                  AS dats_g,
                cast(max(k.summa / k.srokkred) as INT)                       AS pod,
                (SELECT MIN(g_inner.dats)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog)                            AS first_dats_g_before_condition,
                COUNT(CASE WHEN g.dats > k.dats_izm THEN 1 END)              AS sum_prosr,
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
                   AND s_inner.activate = 1)                                 AS total_sums_prosr_kred,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog
                   and g_inner.pog_kred > 0)                                 AS sum_vznos,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog)                            AS sum_vznos_all,
                MIN(CASE
                        WHEN g.dats > k.dats_izm and k.dats_zakr is null THEN g.pog_proc + g.pog_kred
                        ELSE NULL END)                                       AS next_summ,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog
                   and k.dats_zakr is null)                                  AS counted_payments,
                (SELECT count(d_inner.lscor)
                 FROM dok d_inner
                 WHERE d_inner.lscor = k.lsprosr_proc
                   and d_inner.tipdoc = 6)                                   AS count_sums_prosr_proc,
                (SELECT count(d_inner.lscor)
                 FROM dok d_inner
                 WHERE d_inner.lscor = k.lsprosr_kred
                   and d_inner.tipdoc = 6)                                   AS count_sums_prosr_kred,
                max(z.ls)                                                    as z_ls,
                max(zx.data_priem)                                           as zx_priem,
                max(zx.data_vozvrat)                                         as zx_vozvrat,
                (SELECT OverduePeriod
                 FROM dbo.SpisokProsrochennixKreditov(k.numdog, '2023-12-02'))   as overdue,
                MAX(k.klass)                                                 as class
FROM kredit k
         INNER JOIN
     azolik_fiz af ON af.kodchlen = k.kod
         LEFT JOIN
     (SELECT kod_dog, ls, lscor, MAX(dats) AS dats
      FROM dok
      GROUP BY kod_dog, ls, lscor) d ON k.kod_dog = d.kod_dog
         LEFT JOIN
     (SELECT numdog, pog_kred, pog_proc, MAX(dats) AS dats
      FROM grafik
      GROUP BY numdog, pog_kred, pog_proc) g ON k.numdog = g.numdog
         LEFT JOIN
     (SELECT ls, activate, MAX(sums) AS sums
      FROM saldo
      GROUP BY ls, activate) s ON d.ls = s.ls
         LEFT JOIN
     (SELECT numdog, ls, MAX(sums) AS sums, MAX(kod_cb) AS kod_cb
      FROM zalog
      GROUP BY numdog, ls) z ON g.numdog = z.numdog
         Left JOIN
     (select data_priem, data_vozvrat, kod_dog
      from zalog_xranenie) zx on k.kod_dog = zx.kod_dog
WHERE (k.dats_izm between '2023-12-01' and '2023-12-02'
    OR d.dats between '2023-12-01' and '2023-12-02')
  AND EXISTS (SELECT 1
              FROM saldo s
              WHERE (s.ls = k.lspeni OR s.ls = k.lsproc OR s.ls = k.lskred
                  OR s.ls = k.lsprosr_proc OR s.ls = k.lsprosr_kred)
                AND s.activate = 1)
  AND k.numdog = g.numdog
GROUP BY af.kodchlen, af.name, k.numdog, k.vidkred, k.lspeni, k.lsproc, k.lskred, k.lsprosr_proc, k.lsprosr_kred,
         k.dats_zakr, s.activate, k.kod;

-- union all
--
-- SELECT distinct af.kodchlen,
--                 af.name,
--                 k.numdog,
--                 k.vidkred,
--                 d.dats                                                   AS dats_izm,
--                 MAX(k.sost)                                                  AS sost,
--                 MAX(k.status)                                                AS status,
--                 MAX(k.datadog)                                               AS datadog,
--                 k.dats_zakr                                                  AS dats_zakr,
--                 MAX(k.summa)                                                 AS summa,
--                 MAX(k.graf)                                                  AS graf,
--                 MAX(k.nalbeznal)                                             AS nalbeznal,
--                 MAX(k.xatar)                                                 AS xatar,
--                 MAX(k.tipkred)                                               AS tipkred,
--                 MAX(k.prosent)                                               AS prosent,
--                 MAX(d.dats)                                                  AS dats_d,
--                 MAX(g.dats)                                                  AS dats_g,
--                 cast(max(k.summa / k.srokkred) as INT)                       AS pod,
--                 (SELECT MIN(g_inner.dats)
--                  FROM grafik g_inner
--                  WHERE g_inner.numdog = k.numdog)                            AS first_dats_g_before_condition,
--                 COUNT(CASE WHEN g.dats > k.dats_izm THEN 1 END)              AS sum_prosr,
--                 MIN(CASE WHEN g.dats > k.dats_izm THEN g.dats ELSE NULL END) AS first_dats_g_after_condition,
--                 MAX(z.sums)                                                  AS sums_z,
--                 MAX(z.kod_cb)                                                AS kod_cb,
--                 (SELECT SUM(s_inner.sums)
--                  FROM saldo s_inner
--                  WHERE s_inner.ls IN (k.lspeni, k.lsproc, k.lskred)
--                    AND s_inner.activate = 1)                                 AS total_sums,
--                 (SELECT SUM(s_inner.sums)
--                  FROM saldo s_inner
--                  WHERE s_inner.ls = k.lsprosr_proc
--                    AND s_inner.activate = 1)                                 AS total_sums_prosr_proc,
--                 (SELECT SUM(s_inner.sums)
--                  FROM saldo s_inner
--                  WHERE s_inner.ls = k.lsprosr_kred
--                    AND s_inner.activate = 1)                                 AS total_sums_prosr_kred,
--                 (SELECT count(g_inner.pog_kred)
--                  FROM grafik g_inner
--                  WHERE g_inner.numdog = k.numdog
--                    and g_inner.pog_kred > 0)                                 AS sum_vznos,
--                 (SELECT count(g_inner.pog_kred)
--                  FROM grafik g_inner
--                  WHERE g_inner.numdog = k.numdog)                            AS sum_vznos_all,
--                 MIN(CASE
--                         WHEN g.dats > k.dats_izm and k.dats_zakr is null THEN g.pog_proc + g.pog_kred
--                         ELSE NULL END)                                       AS next_summ,
--                 (SELECT count(g_inner.pog_kred)
--                  FROM grafik g_inner
--                  WHERE g_inner.numdog = k.numdog
--                    and k.dats_zakr is null)                                  AS counted_payments,
--                 (SELECT count(d_inner.lscor)
--                  FROM dok d_inner
--                  WHERE d_inner.lscor = k.lsprosr_proc
--                    and d_inner.tipdoc = 6)                                   AS count_sums_prosr_proc,
--                 (SELECT count(d_inner.lscor)
--                  FROM dok d_inner
--                  WHERE d_inner.lscor = k.lsprosr_kred
--                    and d_inner.tipdoc = 6)                                   AS count_sums_prosr_kred,
--                 max(z.ls)                                                    as z_ls,
--                 max(zx.data_priem)                                           as zx_priem,
--                 max(zx.data_vozvrat)                                         as zx_vozvrat,
--                 (SELECT OverduePeriod
--                  FROM dbo.SpisokProsrochennixKreditov(k.numdog, '2023-11-03'))
--
-- FROM kredit k
--          INNER JOIN
--      azolik_fiz af ON af.kodchlen = k.kod
--          LEFT JOIN
--      (SELECT kod_dog, ls, lscor, MAX(dats) AS dats
--       FROM dok
--       GROUP BY kod_dog, ls, lscor) d ON k.kod_dog = d.kod_dog
--          LEFT JOIN
--      (SELECT numdog, pog_kred, pog_proc, MAX(dats) AS dats
--       FROM grafik
--       GROUP BY numdog, pog_kred, pog_proc) g ON k.numdog = g.numdog
--          LEFT JOIN
--      (SELECT ls, activate, MAX(sums) AS sums
--       FROM saldo
--       GROUP BY ls, activate) s ON d.ls = s.ls
--          LEFT JOIN
--      (SELECT numdog, ls, MAX(sums) AS sums, MAX(kod_cb) AS kod_cb
--       FROM zalog
--       GROUP BY numdog, ls) z ON g.numdog = z.numdog
--          Left JOIN
--      (select data_priem, data_vozvrat, kod_dog
--       from zalog_xranenie) zx on k.kod_dog = zx.kod_dog
-- WHERE ((d.dats between '2023-11-01' and '2023-11-30')
--     and (d.lscor like '10101%' or d.lscor like '10105%')
--     and (d.ls like k.lskred or d.ls like k.lsproc)
--     or (d.ls like k.lsprosr_kred or d.ls like k.lsprosr_proc))
-- GROUP BY af.kodchlen, af.name, k.numdog, k.vidkred, k.lspeni, k.lsproc, k.lskred, k.lsprosr_proc, k.lsprosr_kred,
--          k.dats_zakr, s.activate, k.kod, k.dats_izm, d.dats;


SELECT distinct af.kodchlen,
                af.name,
                k.numdog,
                k.vidkred,
                d.dats                                                   AS dats_izm,
                k.sost                                                   AS sost,
                k.status                                                 AS status,
                k.datadog                                                AS datadog,
                k.dats_zakr                                              AS dats_zakr,
                k.summa                                                  AS summa,
                k.graf                                                   AS graf,
                k.nalbeznal                                              AS nalbeznal,
                k.xatar                                                  AS xatar,
                k.tipkred                                                AS tipkred,
                k.prosent                                                AS prosent,
                d.dats                                                   AS dats_d,
                g.dats                                                   AS dats_g,
                cast(max(k.summa / k.srokkred) as INT)                   AS pod,
                (SELECT MIN(g_inner.dats)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog)                        AS first_dats_g_before_condition,
                COUNT(CASE WHEN g.dats > d.dats THEN 1 END)              AS sum_prosr,
                MIN(CASE WHEN g.dats > d.dats THEN g.dats ELSE NULL END) AS first_dats_g_after_condition,
                MAX(z.sums)                                              AS sums_z,
                MAX(z.kod_cb)                                            AS kod_cb,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls IN (k.lspeni, k.lsproc, k.lskred))     AS total_sums,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls = k.lsprosr_proc)                      AS total_sums_prosr_proc,
                (SELECT SUM(s_inner.sums)
                 FROM saldo s_inner
                 WHERE s_inner.ls = k.lsprosr_kred)                      AS total_sums_prosr_kred,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog
                   and g_inner.pog_kred > 0)                             AS sum_vznos,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog)                        AS sum_vznos_all,
                MIN(CASE
                        WHEN g.dats > d.dats and k.dats_zakr is null THEN g.pog_proc + g.pog_kred
                        ELSE NULL END)                                   AS next_summ,
                (SELECT count(g_inner.pog_kred)
                 FROM grafik g_inner
                 WHERE g_inner.numdog = k.numdog
                   and k.dats_zakr is null)                              AS counted_payments,
                (SELECT count(d_inner.lscor)
                 FROM dok d_inner
                 WHERE d_inner.lscor = k.lsprosr_proc
                   and d_inner.tipdoc = 6)                               AS count_sums_prosr_proc,
                (SELECT count(d_inner.lscor)
                 FROM dok d_inner
                 WHERE d_inner.lscor = k.lsprosr_kred
                   and d_inner.tipdoc = 6)                               AS count_sums_prosr_kred,
                max(z.ls)                                                as z_ls,
                max(zx.data_priem)                                       as zx_priem,
                max(zx.data_vozvrat)                                     as zx_vozvrat,
                (SELECT OverduePeriod
                 FROM dbo.SpisokProsrochennixKreditov(k.numdog, '2023-11-30'))

FROM kredit k
         INNER JOIN
     azolik_fiz af ON af.kodchlen = k.kod
         LEFT JOIN
     (SELECT kod_dog, ls, lscor, MAX(dats) AS dats
      FROM dok
      GROUP BY kod_dog, ls, lscor) d ON k.kod_dog = d.kod_dog
         LEFT JOIN
     (SELECT numdog, pog_kred, pog_proc, MAX(dats) AS dats
      FROM grafik
      GROUP BY numdog, pog_kred, pog_proc) g ON k.numdog = g.numdog
         LEFT JOIN
     (SELECT ls, activate, MAX(sums) AS sums
      FROM saldo
      GROUP BY ls, activate) s ON d.ls = s.ls
         LEFT JOIN
     (SELECT numdog, ls, MAX(sums) AS sums, MAX(kod_cb) AS kod_cb
      FROM zalog
      GROUP BY numdog, ls) z ON g.numdog = z.numdog
         Left JOIN
     (select data_priem, data_vozvrat, kod_dog
      from zalog_xranenie) zx on k.kod_dog = zx.kod_dog
WHERE d.dats BETWEEN '2022-11-01' AND '2023-11-30'
  AND d.ls IN (k.lskred, k.lsproc, k.lsprosr_proc, k.lsprosr_kred)
  AND (d.lscor LIKE '10101%' OR d.lscor LIKE '10105%')
  and k.numdog like '99004863-2  '
  AND EXISTS (SELECT 1
              FROM saldo s
              WHERE (s.ls = k.lspeni
                  OR s.ls = k.lsproc
                  OR s.ls = k.lskred
                  OR s.ls = k.lsprosr_proc
                  OR s.ls = k.lsprosr_kred))
GROUP BY af.kodchlen, af.name, k.numdog, k.vidkred, k.lspeni, k.lsproc, k.lskred, k.lsprosr_proc, k.lsprosr_kred,
         k.dats_zakr, s.activate, k.kod, d.dats, g.dats, k.sost, k.[status], k.datadog, k.summa, k.graf, k.nalbeznal,
         k.xatar, k.tipkred, k.prosent;

Select * from dok d inner join kredit k on k.lsproc = d.lscor where d.lscor like :koddog

SELECT * from SpisProsrKred ('99002678-16', '2023-11-30')
SELECT * from SpisokProsrochennixKreditov ('99002678-16', '2023-12-05')