package org.creditbureaureport.service;

import org.creditbureaureport.dto.GrafikDTO;
import org.creditbureaureport.dto.KreditDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverdueCalculator {

    public Map<String, BigDecimal> calculateOverdue(List<KreditDTO> kreditDTOList, LocalDate calculationDate) {
        Map<String, BigDecimal> overdueAmounts = new HashMap<>();

        for (KreditDTO kreditDTO : kreditDTOList) {
            for (GrafikDTO grafik : kreditDTO.getGrafiks()) {
                if (grafik.getDats().isBefore(calculationDate) && !isPaymentMade(kreditDTO, grafik.getDats())) {
                    long daysOverdue = ChronoUnit.DAYS.between(grafik.getDats(), calculationDate);
                    String overdueBucket = determineOverdueBucket(daysOverdue);
                    overdueAmounts.merge(overdueBucket, grafik.getPogKred(), BigDecimal::add);
                }
            }
        }
        return overdueAmounts;
    }

    private boolean isPaymentMade(KreditDTO kreditDTO, LocalDate paymentDate) {
        // Реализация логики определения, был ли платеж осуществлен
        return false;
    }

    private String determineOverdueBucket(long daysOverdue) {
        if (daysOverdue <= 30) return "1-30 days";
        if (daysOverdue <= 60) return "31-60 days";
        if (daysOverdue <= 90) return "61-90 days";
        return "Over 90 days";
    }
}

