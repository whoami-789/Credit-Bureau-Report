package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.creditbureaureport.model.Dokument;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaldoDTO {
    private LocalDate dats;
    private BigDecimal sums;
    private Byte activate;
    private String ls;
    // Другие поля по необходимости
}
