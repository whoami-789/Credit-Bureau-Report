package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrafikDTO {
    private LocalDate dats;
    private BigDecimal pogKred;
    private BigDecimal pogProc;
    private BigDecimal ostatok;
    private Byte mes;
    private Byte sost;
    // Другие поля по необходимости
}
