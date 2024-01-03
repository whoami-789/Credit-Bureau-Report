package org.creditbureaureport.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CreditBureauReportDTO {

    // Поля из AzolikFiz
    private String kodchlen;
    private String name;
    private String inn;
    private String adres;
    private String telmobil;
    private String telhome;
    private String serNumPasp;
    private LocalDate vidanPasp;
    private LocalDate datsRojd;
    private Byte fsobst;
    private Short users;
    private Byte status;
    private String lsvznos;
    private Byte indpred;
    private String kodObl;
    private String kodRayon;
    private String fam;
    private String imya;
    private String otch;
    private String kodPension;
    private String tipDok;
    private LocalDate datsIzm;
    private LocalDate paspdo;
    private Integer id;

    // Поля из Dokument
    private Long kod;
    private String numdok;
    private LocalDate dats;
    private int tipdoc;
    private String ls;
    private String lscor;
    private String nazn;
    private int sost;
    private LocalDateTime dtime;
    private BigDecimal sums;
    private LocalDate datProv;
    private Integer kodDog;
    private LocalDate datsProc;
    private String kodUch;
    private Integer prixGroup;
    private Integer groupStr;

    // Поля из AzolikYur
    private String kodchlenYur; // Уточните имя поля для кода из AzolikYur
    private LocalDate datsYur; // Уточните имя поля для даты из AzolikYur
    // Добавьте остальные поля из AzolikYur

    // Поля из Grafik
    private Integer idGrafik;
    private LocalDate datsGrafik;
    private BigDecimal pogKred;
    private BigDecimal pogProc;
    private BigDecimal ostatok;
    private Byte mes;
    private Byte sostGrafik;
    private String numdogGrafik;

    // Поля из Inform
    private String numks;
    private String nameInform;
    private String shotname;
    private String lsInform;
    private String mfo;
    private String innInform;
    private String okonx;
    private String adresInform;
    private String tel;
    private String ademail;
    private String sistBank;
    private Byte sostOperDay;
    private Date operDay;
    private Short dneygod;
    private BigDecimal kapital;
    private BigDecimal aktiv;
    private BigDecimal aktiv30;
    private BigDecimal passiv;
    private String lskomiss;
    private String lspeni;
    // Добавьте остальные поля из Inform

    // Поля из Kredit
    private String kodKredit;
    private String numdogKredit;
    private LocalDate datadog;
    private LocalDate datsKredit;
    private BigDecimal summa;
    private String vidvalut;
    private Byte vidzalog;
    private Byte vidsrok;
    private BigDecimal prosent;
    private Byte maqsad;
    private Byte sostKredit;
    private Byte statusKredit;
    private String primKredit;
    private String yurfiz;
    private Short tipkred;
    private Byte srokkred;
    private Short usersKredit;
    private String lskred;
    private String lsproc;
    private String lsprosrKredit;
    private Date dtimeKredit;
    private Byte sms;
    private Integer kodDogKredit;
    private Boolean indpredKredit;
    // Добавьте остальные поля из Kredit

    // Поля из Saldo
    private Long idSaldo;
    private String lsSaldo;
    private LocalDate datsSaldo;
    private BigDecimal sumsSaldo;
    private Byte activateSaldo;

    // Добавьте геттеры и сеттеры для всех полей
}
