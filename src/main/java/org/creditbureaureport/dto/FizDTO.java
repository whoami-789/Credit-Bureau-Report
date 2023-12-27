package org.creditbureaureport.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FizDTO {
    private String kodchlen;
    private LocalDate datsIzm;
    private String name;
    private String fam;
    private String imya;
    private String otch;
    private Byte fsobst;
    private LocalDate datsRojd;
    private String adres;
    private String kodRayon;
    private String kodObl;
    private String kodPension;
    private String inn;
    private String tipDok;
    private String serNumPasp;
    private LocalDate vidanPasp;
    private LocalDate paspdo;
    private String telmobil;
    private String telhome;
    private Byte indpred;

    // Геттеры и сеттеры
}
