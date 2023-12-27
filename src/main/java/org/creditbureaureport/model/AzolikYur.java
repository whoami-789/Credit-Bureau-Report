package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "azolik_yur")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AzolikYur {

    @Id
    @Column(name = "kodchlen", nullable = false, columnDefinition = "nchar(8)")
    private String kodchlen;

    @Column(name = "dats")
    private LocalDate dats;

    @Column(name = "name")
    private String name;

    @Column(name = "shotname")
    private String shotname;

    @Column(name = "inn", nullable = false, columnDefinition = "nchar()")
    private String inn;

    @Column(name = "adres")
    private String adres;

    @Column(name = "telmobil")
    private String telmobil;

    @Column(name = "telhome")
    private String telhome;

    @Column(name = "vznos")
    private BigDecimal vznos;

    @Column(name = "sost")
    private Byte sost;

    @Transient
    private Byte status;

    @Column(name = "ls")
    private String ls;

    @Column(name = "mfo")
    private String mfo;

    @Column(name = "fsobst")
    private Byte fsobst;

    @Column(name = "min_vo")
    private Short minVo;

    @Column(name = "ad_email")
    private String adEmail;

    @Column(name = "fio_director")
    private String fioDirector;

    @Column(name = "obraz_director")
    private Byte obrazDirector;

    @Column(name = "dats_rojd")
    private LocalDate datsRojd;

    @Column(name = "svyaz")
    private Byte svyaz;

    @Column(name = "dats_zakr")
    private LocalDate datsZakr;

    @Column(name = "users")
    private Short users;

    @Column(name = "kod_organ")
    private Byte kodOrgan;

    @Column(name = "num_registr")
    private Integer numRegistr;

    @Column(name = "dats_registr")
    private LocalDate datsRegistr;

    @Column(name = "prim")
    private String prim;

    @Column(name = "tec_vznos")
    private BigDecimal tecVznos;

    @Column(name = "lsvznos", nullable = false, columnDefinition = "nchar()")
    private String lsvznos;

    @Column(name = "dtime")
    private Date dtime;

    @Column(name = "indpred")
    private Byte indpred;

    @Column(name = "photo")
    private byte[] photo;

    @Transient
    private Integer kodSubject;

    @Column(name = "fio_buxg")
    private String fioBuxg;

    @Column(name = "tipZayom", nullable = false, columnDefinition = "nchar()")
    private String tipZayom;

    @Column(name = "sost_registr")
    private Byte sostRegistr;

    @Transient
    private Integer sysNumber;

    @Column(name = "dats_izm")
    private LocalDate datsIzm;

    @Column(name = "uchred")
    private Byte uchred;

    @Column(name = "kodObl", nullable = false, columnDefinition = "nchar()")
    private String kodObl;

    @Column(name = "kodRayon", nullable = false, columnDefinition = "nchar()")
    private String kodRayon;
}

