package org.creditbureaureport.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class YurDTO {
    private LocalDate datsIzm;
    private String kodchlen;
    private String name;
    private String shotname;
    private String adres;
    private String kodRayon;
    private String kodObl;
    private String telmobil;
    private String telhome;
}
