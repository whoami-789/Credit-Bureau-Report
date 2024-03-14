package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogDTO {
    private BigDecimal sums;
    private String ls;
    private String kodCb;
    private String numDog;
    // Другие поля по необходимости
}
