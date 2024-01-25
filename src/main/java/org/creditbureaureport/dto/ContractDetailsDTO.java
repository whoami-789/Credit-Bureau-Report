package org.creditbureaureport.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ContractDetailsDTO {
    // Поля из AzolikFiz
    private String kodchlen;
    private String name;

    // Поля из Dokument
    private List<DokumentDTO> dokuments;


    // Поля из Kredit
    private String numdog;
    private LocalDate datadog;
    private BigDecimal summa;
    private BigDecimal prosent;
    private LocalDate datsIzm;

    // поля из [grafik]
    private List<GrafikDTO> grafiks;
    private List<SaldoDTO> saldos;
    private List<ZalogDTO> zalogs;
    private List<ZalogXranenieDTO> zalogXranenieList;
}
