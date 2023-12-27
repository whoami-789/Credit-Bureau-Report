package org.creditbureaureport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "inform")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inform {

    @Id
    @Column(name = "numks", nullable = false)
    private String numks;

    @Column(name = "name")
    private String name;

    @Column(name = "shotname")
    private String shotname;

    @Column(name = "ls")
    private String ls;

    @Column(name = "mfo")
    private String mfo;

    @Column(name = "inn")
    private String inn;

    @Column(name = "okonx")
    private String okonx;

    @Column(name = "adres")
    private String adres;

    @Column(name = "tel")
    private String tel;

    @Column(name = "ademail")
    private String ademail;

    @Column(name = "sist_bank")
    private String sistBank;

    @Column(name = "sost_oper_day")
    private Byte sostOperDay;

    @Column(name = "oper_day")
    private Date operDay;

    @Column(name = "dneygod")
    private Short dneygod;

    @Column(name = "kapital")
    private BigDecimal kapital;

    @Column(name = "aktiv")
    private BigDecimal aktiv;

    @Column(name = "aktiv30")
    private BigDecimal aktiv30;

    @Column(name = "passiv")
    private BigDecimal passiv;

    @Column(name = "lskomiss", nullable = false, columnDefinition = "nchar")
    private String lskomiss;

    @Column(name = "lspeni", nullable = false, columnDefinition = "nchar")
    private String lspeni;

    @Column(name = "dats_vnedr")
    private Date datsVnedr;

    @Column(name = "mesdep")
    private Date mesdep;

    @Column(name = "meskred")
    private Date meskred;

    @Column(name = "grafkred")
    private Byte grafkred;

    @Column(name = "grafdep")
    private Byte grafdep;

    @Column(name = "rejnach")
    private Byte rejnach;

    @Column(name = "creatrezerv")
    private Byte creatrezerv;

    @Column(name = "ls_kontrrezerv_kred", nullable = false, columnDefinition = "nchar")
    private String lsKontrrezervKred;

    @Column(name = "ls_kontrrezerv_liz", nullable = false, columnDefinition = "nchar")
    private String lsKontrrezervLiz;

    @Column(name = "vers1")
    private Byte vers1;

    @Column(name = "vers2")
    private Short vers2;

    @Column(name = "ls_kontrvne", nullable = false, columnDefinition = "nchar")
    private String lsKontrvne;

    @Column(name = "fio_rais")
    private String fioRais;

    @Column(name = "fio_direktor")
    private String fioDirektor;

    @Column(name = "fio_glbux")
    private String fioGlbux;

    @Column(name = "kol_vo_rab")
    private Integer kolVoRab;

    @Column(name = "kol_vo_kred_rab")
    private Integer kolVoKredRab;

    @Column(name = "kol_vo_punkt")
    private Integer kolVoPunkt;

    @Column(name = "kol_vo_filial")
    private Integer kolVoFilial;

    @Column(name = "path_otch")
    private String pathOtch;

    @Column(name = "path_otch_recv")
    private String pathOtchRecv;

    @Column(name = "dats_izm")
    private LocalDate datsIzm;

    @Column(name = "minzp")
    private BigDecimal minzp;

    @Column(name = "lsshtraf")
    private String lsshtraf;

    @Column(name = "autozalog")
    private Byte autozalog;

    @Column(name = "ls_kontrrezerv_sud")
    private String lsKontrrezervSud;

    @Column(name = "is4bal")
    private Byte is4bal;
}