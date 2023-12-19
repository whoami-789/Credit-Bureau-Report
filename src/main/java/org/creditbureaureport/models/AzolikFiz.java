package org.creditbureaureport.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "azolik_fiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AzolikFiz {
    
    @Column(name = "kodchlen", nullable = false, columnDefinition = "nchar(8)")
    private String kodchlen;

    @Column(name = "dats")
    private LocalDate dats;

    @Column(name = "name")
    private String name;

    @Column(name = "numknigki")
    private String numknigki;

    @Column(name = "inn")
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

    @Column(name = "ser_num_pasp")
    private String serNumPasp;

    @Column(name = "vidan_pasp")
    private Date vidanPasp;

    @Column(name = "kem_pasp")
    private String kemPasp;

    @Column(name = "obrazovan")
    private Byte obrazovan;

    @Column(name = "dats_rojd")
    private Date datsRojd;

    @Column(name = "dats_zakr")
    private Date datsZakr;

    @Column(name = "svyaz")
    private Byte svyaz;

    @Column(name = "fsobst")
    private Byte fsobst;

    @Column(name = "users")
    private Short users;

    @Column(name = "prim")
    private String prim;

    @Column(name = "mwork")
    private String mwork;

    @Column(name = "ad_email")
    private String adEmail;

    @Transient
    private Byte status;

    @Column(name = "filial")
    private Byte filial;

    @Column(name = "lsvznos")
    private String lsvznos;

    @Column(name = "dtime")
    private Date dtime;

    @Column(name = "indpred")
    private Byte indpred;

    @Column(name = "photo")
    private byte[] photo;

    @Transient
    private Integer kodSubject;

    @Column(name = "serPasp", nullable = false, columnDefinition = "nchar(8)")
    private String serPasp;

    @Column(name = "numPasp", nullable = false, columnDefinition = "nchar(8)")
    private String numPasp;

    @Column(name = "kod_obl")
    private String kodObl;

    @Column(name = "kod_rayon")
    private String kodRayon;

    @Column(name = "fam")
    private String fam;

    @Column(name = "imya")
    private String imya;

    @Column(name = "otch")
    private String otch;

    @Column(name = "tip_zayom")
    private String tipZayom;

    @Column(name = "kod_pension")
    private String kodPension;

    @Column(name = "tip_dok")
    private String tipDok;

    @Column(name = "sost_registr")
    private Byte sostRegistr;

    @Transient
    private Integer sysNumber;

    @Column(name = "dats_izm")
    private LocalDate datsIzm;

    @Column(name = "uchred")
    private Byte uchred;

    @Column(name = "dats_izm_uchred")
    private Date datsIzmUchred;

    @Column(name = "paspdo")
    private Date paspdo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
}

