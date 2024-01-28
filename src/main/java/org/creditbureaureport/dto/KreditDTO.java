package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KreditDTO {

    private String numdog;
    private LocalDate datadog;
    private BigDecimal summa;
    private BigDecimal prosent;
    private LocalDate datsIzm;
    private String lsprosrProc;
    private String lspeni;
    private Byte status;
    private LocalDate datsZakr;
    private Integer vidKred;
    private String kod;
    private String lsKred;
    private String lsProc;
    private String name;

    // Другие поля, соответствующие вашей модели Kredit

    // Списки DTO для связанных данных
    private List<OverdueDTO> overdue;
    private List<DokumentDTO> dokuments;
    private List<SaldoDTO> saldo;
    private List<GrafikDTO> grafiks;
    private List<ZalogDTO> zalogs;
    private List<ZalogXranenieDTO> zalogXranenieList;
}
