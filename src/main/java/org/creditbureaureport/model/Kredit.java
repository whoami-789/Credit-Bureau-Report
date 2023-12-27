package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "kredit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kredit {

    @Id
    @Column(name = "kod", nullable = false, columnDefinition = "nchar(8)")
    private String kod;

    @Column(name = "numdog", nullable = false, columnDefinition = "nchar(12)")
    private String numdog;

    @Column(name = "datadog", nullable = false)
    private LocalDate datadog;

    @Column(name = "dats")
    private LocalDate dats;

    @Column(name = "summa")
    private BigDecimal summa;

    @Column(name = "vidvalut", nullable = false, columnDefinition = "nchar(3)")
    private String vidvalut;

    @Column(name = "vidzalog", nullable = false)
    private Byte vidzalog;

    @Column(name = "vidsrok")
    private Byte vidsrok;

    @Column(name = "prosent", nullable = false)
    private BigDecimal prosent;

    @Column(name = "maqsad")
    private Byte maqsad;

    @Column(name = "sost", nullable = false)
    private Byte sost;

    @Column(name = "status")
    private Byte status;

    @Column(name = "prim")
    private String prim;

    @Transient
    private String yurfiz;

    @Column(name = "tipkred")
    private Short tipkred;

    @Column(name = "srokkred")
    private Byte srokkred;

    @Column(name = "users")
    private Short users;

    @Column(name = "lskred", nullable = false, columnDefinition = "nchar(20)")
    private String lskred;

    @Column(name = "lsproc", nullable = false, columnDefinition = "nchar(20)")
    private String lsproc;

    @Column(name = "lsprosr_kred")
    private String lsprosrKred;

    @Column(name = "dtime")
    private Date dtime;

    @Column(name = "sms")
    private Byte sms;

    @Column(name = "tel")
    private Byte tel;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kod_dog", nullable = false)
    private Integer kodDog;

    @Transient
    private Boolean indpred;

    @Column(name = "sost_ls")
    private Byte sostLs;

    @Column(name = "dopsogl")
    private String dopsogl;

    @Column(name = "lssud_kred")
    private String lssudKred;

    @Column(name = "nalbeznal")
    private Byte nalbeznal;

    @Column(name = "vazvnalbeznal")
    private Byte vazvnalbeznal;

    @Column(name = "sost_proc")
    private Byte sostProc;

    @Column(name = "rejnach")
    private Byte rejnach;

    @Column(name = "autoe")
    private Byte autoe;

    @Column(name = "autos")
    private Byte autos;

    @Column(name = "autokred")
    private Byte autokred;

    @Column(name = "autoproc")
    private Byte autoproc;

    @Column(name = "dney")
    private Byte dney;

    @Column(name = "chas")
    private Byte chas;

    @Transient
    private String lsDox;

    @Column(name = "lsprocvne")
    private String lsprocvne;

    @Column(name = "vidkred")
    private Integer vidkred;

    @Column(name = "lsrezerv")
    private String lsrezerv;

    @Transient
    private Integer tip;

    @Column(name = "minvznos")
    private BigDecimal minvznos;

    @Column(name = "komissy")
    private BigDecimal komissy;

    @Column(name = "lgot")
    private Byte lgot;

    @Column(name = "progress")
    private Byte progress;

    @Column(name = "lizpredmet", columnDefinition = "TEXT")
    private String lizpredmet;

    @Column(name = "lizprodovec", columnDefinition = "TEXT")
    private String lizprodovec;

    @Column(name = "dats_zakr")
    private Date datsZakr;

    @Column(name = "dats_prosr")
    private Date datsProsr;

    @Column(name = "dopsogl_dats")
    private Date dopsoglDats;

    @Column(name = "spec")
    private Byte spec;

    @Column(name = "tipliz")
    private Byte tipliz;

    @Column(name = "ls_peres")
    private String lsPeres;

    @Column(name = "graf")
    private Byte graf;

    @Column(name = "autop")
    private Byte autop;

    @Transient
    private String lsKontrvne;

    @Transient
    private String lsSpiskred;

    @Column(name = "dats_izm")
    private Date datsIzm;

    @Column(name = "dats_izm_grafik")
    private Date datsIzmGrafik;

    @Column(name = "dats_izm_zalog")
    private Date datsIzmZalog;

    @Column(name = "objekt")
    private String objekt;

    @Column(name = "valut")
    private Byte valut;

    @Column(name = "klass")
    private Byte klass;

    @Transient
    private String lsprosrProc;

    @Transient
    private String ls22812;

    @Column(name = "dats_izm_asoki")
    private Date datsIzmAsoki;

    @Column(name = "xatar")
    private Byte xatar;

    @Transient
    private String lspeni;
}

