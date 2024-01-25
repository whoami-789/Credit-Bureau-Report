package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.creditbureaureport.model.Saldo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DokumentDTO {
    private String numdok;
    private LocalDate dats;
    private BigDecimal sums;
    private String ls;
    private String lscor;
    private List<SaldoDTO> saldos;

    // Другие поля по необходимости
}
