package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZalogXranenieDTO {
    private LocalDate data_priem;
    private LocalDate data_vozvrat;
    // Другие поля по необходимости
}
